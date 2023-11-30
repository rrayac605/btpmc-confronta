package mx.gob.imss.cit.pmc.confronta.listener;

import mx.gob.imss.cit.pmc.confronta.constants.ConfrontaConstants;
import mx.gob.imss.cit.pmc.confronta.utils.DateUtils;
import mx.gob.imss.cit.pmc.confronta.utils.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@JobScope
public class JobConfrontaListener implements JobExecutionListener {

    private static final Logger logger = LoggerFactory.getLogger(JobConfrontaListener.class);

    @Override
    public void beforeJob(JobExecution jobExecution) {
        logger.info("Job name: {}", jobExecution.getJobInstance().getJobName());
        jobExecution.getExecutionContext().putString(ConfrontaConstants.PAST_STEP_PARAM, ConfrontaConstants.EMPTY);
        jobExecution.getExecutionContext().putString(ConfrontaConstants.IS_TERMINATED_ONLY, Boolean.FALSE.toString());
        if (ConfrontaConstants.CONFRONTA_JOB.equals(jobExecution.getJobInstance().getJobName())) {
            Long key = jobExecution.getJobParameters().getLong(ConfrontaConstants.KEY_PARAM);
            Integer del = NumberUtils.getDel(key);
            List<Integer> subDelList = NumberUtils.getSubDelList(key);
            logger.info("-------------------------{}-------------------------", DateUtils.getCurrentMexicoDate());
            logger.info("Comienza la ejecucion de el job con key {} para la delegacion {} y sub delegaciones {}", key, del, subDelList);
            logger.info("--------------------------------------------------------------------------");
        }
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (ConfrontaConstants.CONFRONTA_JOB.equals(jobExecution.getJobInstance().getJobName())) {
            Long key = jobExecution.getJobParameters().getLong(ConfrontaConstants.KEY_PARAM);
            Integer del = NumberUtils.getDel(key);
            List<Integer> subDelList = NumberUtils.getSubDelList(key);
            logger.info("-------------------------{}-------------------------", DateUtils.getCurrentMexicoDate());
            logger.info("Finaliza la ejecucion de el job con key {} para la delegacion {} y sub delegaciones {}", key, del, subDelList);
            logger.info("--------------------------------------------------------------------------");
        }
    }
}
