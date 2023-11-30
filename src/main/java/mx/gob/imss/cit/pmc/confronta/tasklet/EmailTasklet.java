package mx.gob.imss.cit.pmc.confronta.tasklet;

import mx.gob.imss.cit.pmc.confronta.constants.ConfrontaConstants;
import mx.gob.imss.cit.pmc.confronta.services.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class EmailTasklet implements Tasklet {

    private static final Logger logger = LoggerFactory.getLogger(EmailTasklet.class);

    @Value("#{stepExecution}")
    private StepExecution stepExecution;

    @Autowired
    private EmailService emailService;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        try {
            String fromStepName = stepExecution.getJobExecution().getExecutionContext().getString(ConfrontaConstants.PAST_STEP_PARAM);
            String templateName = ConfrontaConstants.FROM_STEP_TEMPLATE_NAME.get(fromStepName);
            emailService.sendEmail(templateName, ConfrontaConstants.DEL_DESCRIPTION_MAP.keySet());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RepeatStatus.FINISHED;
    }

}
