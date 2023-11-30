package mx.gob.imss.cit.pmc.confronta.controller;

import mx.gob.imss.cit.pmc.confronta.constants.ConfrontaConstants;
import mx.gob.imss.cit.pmc.confronta.services.ConfrontaService;
import mx.gob.imss.cit.pmc.confronta.utils.DateUtils;
import mx.gob.imss.cit.pmc.confronta.utils.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping("/msBatchConfronta")
public class BatchConfrontaControllerImpl {

    Logger logger = LoggerFactory.getLogger("BatchConfrontaControllerImpl");

    @Autowired
    @Qualifier("jobLauncherController")
    private SimpleJobLauncher jobLauncherController;

    @Autowired
    @Qualifier("descartadosJob")
    private Job descartadosJob;
    
    @Autowired
    @Qualifier("confrontaJob")
    private Job confrontaJob;

    @Autowired
    @Qualifier("confrontaNextJob")
    private Job confrontaNextJob;

    @Autowired
    private ConfrontaService confrontaService;

    @Bean
    public SimpleJobLauncher jobLauncherController(JobRepository jobRepository) {
        SimpleJobLauncher launcher = new SimpleJobLauncher();
        launcher.setJobRepository(jobRepository);
        return launcher;
    }

    @PostMapping(value = "/execute", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> execute() {
        logger.info("Se inicia la ejecucion del proceso");
        ResponseEntity<?> response = null;
        List<Boolean> jobExecutionStatus = new ArrayList<>();
        try {
            for (int key : ConfrontaConstants.DEL_SUBDEL.keySet()) {
                JobParameters parameters = new JobParametersBuilder()
                        .addLong(ConfrontaConstants.KEY_PARAM, (long) key)
                        .addDate(ConfrontaConstants.EXECUTION_DATE, Objects.requireNonNull(DateUtils.getCurrentMexicoDate()))
                        .addString(ConfrontaConstants.REPROCESS_FLAG, Boolean.FALSE.toString())
                        .toJobParameters();
                JobExecution jobExecution = jobLauncherController.run(confrontaJob, parameters);
                logger.info("Se proceso el archivo de la delegacion {} con estatus {}",
                        NumberUtils.getDel((long) key),
                        jobExecution.getExitStatus());
                jobExecutionStatus.add(ExitStatus.COMPLETED.getExitCode().equals(jobExecution.getExitStatus().getExitCode()));
            }
            JobParameters parameters = new JobParametersBuilder()
                    .addDate(ConfrontaConstants.EXECUTION_DATE, Objects.requireNonNull(DateUtils.getCurrentMexicoDate()))
                    .addString(ConfrontaConstants.NOT_ERROR_FLAG_PARAM, jobExecutionStatus.stream()
                            .reduce(Boolean.TRUE, Boolean::logicalAnd).toString())
                    .addString(ConfrontaConstants.REPROCESS_FLAG, Boolean.FALSE.toString())
                    .toJobParameters();
            JobExecution jobExecutionNext = jobLauncherController.run(confrontaNextJob, parameters);
            logger.info("Se realizo el envio de los archivos a los servidores sftp con status {}", jobExecutionNext.getExitStatus());
            response = new ResponseEntity<>("Proceso de confronta ejecutado correctamente", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error al ejecutar la tarea", e);
            response = new ResponseEntity<>("Error al ejecutar la tarea", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }
    
    @PostMapping(value = "/reprocess/descartados", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> descartadosReproceso() {
        ResponseEntity<?> response;
        if (!confrontaService.processDescartadosIsFinished()) {
            logger.info("Se inicia la ejecucion del reproceso descartados");
            List<Boolean> jobExecutionStatus = new ArrayList<>();
            try {

                for (int key : ConfrontaConstants.DEL_SUBDEL.keySet()) {
                    JobParameters parameters = new JobParametersBuilder()
                            .addLong(ConfrontaConstants.KEY_PARAM, (long) key)
                            .addDate(ConfrontaConstants.EXECUTION_DATE, Objects.requireNonNull(DateUtils.getCurrentMexicoDate()))
                            .addString(ConfrontaConstants.REPROCESS_FLAG, Boolean.TRUE.toString())
                            .toJobParameters();
                    JobExecution jobExecution = jobLauncherController.run(descartadosJob, parameters);
                    logger.info("Se proceso el archivo de la delegacion descartadosJob {} con estatus {}",
                            NumberUtils.getDel((long) key),
                            jobExecution.getExitStatus());
                    jobExecutionStatus.add(ExitStatus.COMPLETED.getExitCode().equals(jobExecution.getExitStatus().getExitCode()));
                }
                response = new ResponseEntity<>("Proceso de confronta ejecutado correctamente", HttpStatus.OK);
            } catch (Exception e) {
                e.printStackTrace();
                response = new ResponseEntity<>("Error al ejecutar la tarea", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            logger.info("El proceso de descartados ya ha finalizado");
            response = new ResponseEntity<>("El proceso de confronta ya ha finalizado", HttpStatus.OK);
        }
        return response;
    }

    @PostMapping(value = "/reprocess", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> reprocess() {
        ResponseEntity<?> response;
        if (!confrontaService.processIsFinished()) {
            logger.info("Se inicia la ejecucion del reproceso");
            List<Boolean> jobExecutionStatus = new ArrayList<>();
            try {
                Set<Long> keyList = confrontaService.getFirstJobFailedKeyList();
                for (long key : keyList) {
                    JobParameters parameters = new JobParametersBuilder()
                            .addLong(ConfrontaConstants.KEY_PARAM, key)
                            .addDate(ConfrontaConstants.EXECUTION_DATE, Objects.requireNonNull(DateUtils.getCurrentMexicoDate()))
                            .addString(ConfrontaConstants.REPROCESS_FLAG, Boolean.TRUE.toString())
                            .toJobParameters();
                    JobExecution jobExecution = jobLauncherController.run(confrontaJob, parameters);
                    logger.info("Se proceso el archivo de la delegacion {} con estatus {}",
                            NumberUtils.getDel(key),
                            jobExecution.getExitStatus());
                    jobExecutionStatus.add(ExitStatus.COMPLETED.getExitCode().equals(jobExecution.getExitStatus().getExitCode()));
                }
                JobParameters parameters = new JobParametersBuilder()
                        .addDate(ConfrontaConstants.EXECUTION_DATE, Objects.requireNonNull(DateUtils.getCurrentMexicoDate()))
                        .addString(ConfrontaConstants.NOT_ERROR_FLAG_PARAM, jobExecutionStatus.stream()
                                .reduce(Boolean.TRUE, Boolean::logicalAnd).toString())
                        .addString(ConfrontaConstants.REPROCESS_FLAG, Boolean.TRUE.toString())
                        .toJobParameters();
                JobExecution jobExecutionNext = jobLauncherController.run(confrontaNextJob, parameters);
                logger.info("Se realizo el envio de los archivos a los servidores sftp con status {}", jobExecutionNext.getExitStatus());
                response = new ResponseEntity<>("Proceso de confronta ejecutado correctamente", HttpStatus.OK);
            } catch (Exception e) {
                e.printStackTrace();
                response = new ResponseEntity<>("Error al ejecutar la tarea", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            logger.info("El proceso de confronta ya ha finalizado");
            response = new ResponseEntity<>("El proceso de confronta ya ha finalizado", HttpStatus.OK);
        }
        return response;
    }

}
