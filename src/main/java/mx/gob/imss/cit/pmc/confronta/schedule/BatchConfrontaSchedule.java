package mx.gob.imss.cit.pmc.confronta.schedule;

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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Component
public class BatchConfrontaSchedule {

    private static final Logger logger = LoggerFactory.getLogger(BatchConfrontaSchedule.class);

    @Autowired
    @Qualifier("jobLauncherSchedule")
    private SimpleJobLauncher jobLauncherSchedule;

    @Autowired
    @Qualifier("confrontaJob")
    private Job confrontaJob;

    @Autowired
    @Qualifier("confrontaNextJob")
    private Job confrontaNextJob;

    @Autowired
    private ConfrontaService confrontaService;

    @Bean
    public SimpleJobLauncher jobLauncherSchedule(JobRepository jobRepository) {
        SimpleJobLauncher launcher = new SimpleJobLauncher();
        launcher.setJobRepository(jobRepository);
        return launcher;
    }

    @Scheduled(cron = "${confrontaBatchExecute}")
    public void startJob() {
        logger.info("Se inicia la ejecucion del proceso");
        List<Boolean> jobExecutionStatus = new ArrayList<>();
        try {
            for (int key : ConfrontaConstants.DEL_SUBDEL.keySet()) {
                JobParameters parameters = new JobParametersBuilder()
                        .addLong(ConfrontaConstants.KEY_PARAM, (long) key)
                        .addDate(ConfrontaConstants.EXECUTION_DATE, Objects.requireNonNull(DateUtils.getCurrentMexicoDate()))
                        .addString(ConfrontaConstants.REPROCESS_FLAG, Boolean.FALSE.toString())
                        .toJobParameters();
                JobExecution jobExecution = jobLauncherSchedule.run(confrontaJob, parameters);
                logger.info("Se proceso el archivo de la delegacion {} con estatus {} y key {}",
                        NumberUtils.getDel((long) key),
                        jobExecution.getExitStatus(),
                        key);
                jobExecutionStatus.add(ExitStatus.COMPLETED.getExitCode().equals(jobExecution.getExitStatus().getExitCode()));
            }
            JobParameters parameters = new JobParametersBuilder()
                    .addDate(ConfrontaConstants.EXECUTION_DATE, Objects.requireNonNull(DateUtils.getCurrentMexicoDate()))
                    .addString(ConfrontaConstants.NOT_ERROR_FLAG_PARAM, jobExecutionStatus.stream()
                            .reduce(Boolean.TRUE, Boolean::logicalAnd).toString())
                    .addString(ConfrontaConstants.REPROCESS_FLAG, Boolean.FALSE.toString())
                    .toJobParameters();
            JobExecution jobExecutionNext = jobLauncherSchedule.run(confrontaNextJob, parameters);
            logger.info("Se realizo el envio de los archivos a los servidores sftp con status {}", jobExecutionNext.getExitStatus());
        } catch (Exception e) {
            logger.error("Error al ejecutar la tarea", e);
        }
    }

    @Scheduled(cron = "${confrontaBatchReprocess}")
    public void reprocessJob() {
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
                    JobExecution jobExecution = jobLauncherSchedule.run(confrontaJob, parameters);
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
                JobExecution jobExecutionNext = jobLauncherSchedule.run(confrontaNextJob, parameters);
                logger.info("Se realizo el envio de los archivos a los servidores sftp con status {}", jobExecutionNext.getExitStatus());
            } catch (Exception e) {
                logger.error("Error al ejecutar la tarea", e);
            }
        } else {
            logger.info("El proceso de confronta ya ha finalizado");
        }
    }

}
