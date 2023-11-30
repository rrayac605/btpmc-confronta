package mx.gob.imss.cit.pmc.confronta.tasklet;

import mx.gob.imss.cit.pmc.confronta.constants.ConfrontaConstants;
import mx.gob.imss.cit.pmc.confronta.enums.ProcessActionEnum;
import mx.gob.imss.cit.pmc.confronta.repository.ProcessControlRepository;
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
public class ProcessControlFailedTasklet implements Tasklet {

    @Autowired
    private ProcessControlRepository processControlRepository;

    @Value("#{stepExecution}")
    private StepExecution stepExecution;

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        String fromStepName = stepExecution.getJobExecution().getExecutionContext().getString(ConfrontaConstants.PAST_STEP_PARAM);
        if (!(fromStepName.equals(ConfrontaConstants.HEADER_STEP) || fromStepName.equals(ConfrontaConstants.FOOTER_STEP))) {
            String action = ConfrontaConstants.FROM_STEP_FAILED_ACTION_NAME.get(fromStepName);
            Long key = stepExecution.getJobParameters().getLong(ConfrontaConstants.KEY_PARAM);
            processControlRepository.createError(action, key);
            processControlRepository.createError(ProcessActionEnum.CONFRONTA_PROCESS.getDesc(), null);
        }
        return RepeatStatus.FINISHED;
    }
}
