package mx.gob.imss.cit.pmc.confronta.tasklet;

import mx.gob.imss.cit.pmc.confronta.constants.ConfrontaConstants;
import mx.gob.imss.cit.pmc.confronta.enums.ProcessActionEnum;
import mx.gob.imss.cit.pmc.confronta.repository.FileControlRepository;
import mx.gob.imss.cit.pmc.confronta.services.EmailService;

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
public class GenerateFileSuccessTasklet implements Tasklet {

    @Autowired
    private FileControlRepository fileControlRepository;

    @Value("#{stepExecution}")
    private StepExecution stepExecution;
    
    @Autowired
    private EmailService emailService;
    
    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        Long key = stepExecution.getJobParameters().getLong(ConfrontaConstants.KEY_PARAM);
        fileControlRepository.createCorrect(ProcessActionEnum.FILE_GENERATION.getDesc(), key);
        if(key.equals(42L)) {
        	emailService.sendEmailStepOne(ConfrontaConstants.STEP_ONE_TEMPLATE);
        	emailService.sendEmailStepTwo(ConfrontaConstants.STEP_TWO_TEMPLATE);
        	emailService.sendEmailStepThree(ConfrontaConstants.STEP_THREE_TEMPLATE);
        }
        return RepeatStatus.FINISHED;
    }

}
