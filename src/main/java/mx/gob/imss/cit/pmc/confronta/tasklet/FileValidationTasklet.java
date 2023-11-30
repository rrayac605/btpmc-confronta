package mx.gob.imss.cit.pmc.confronta.tasklet;

import mx.gob.imss.cit.pmc.confronta.constants.ConfrontaConstants;
import mx.gob.imss.cit.pmc.confronta.enums.ProcessActionEnum;
import mx.gob.imss.cit.pmc.confronta.repository.FileControlRepository;
import mx.gob.imss.cit.pmc.confronta.utils.ExitStatusConfronta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
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
public class FileValidationTasklet implements Tasklet {

    private static final Logger logger = LoggerFactory.getLogger(FileValidationTasklet.class);

    @Value("#{stepExecution}")
    private StepExecution stepExecution;

    @Autowired
    private FileControlRepository fileControlRepository;

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        String fromStep = stepExecution.getJobExecution().getExecutionContext().getString(ConfrontaConstants.PAST_STEP_PARAM);
        boolean isSftp1 = ConfrontaConstants.SFTP1_UPLOAD_FILE_STEP.equals(fromStep);
        boolean isSftp2 = ConfrontaConstants.SFTP2_UPLOAD_FILE_STEP.equals(fromStep);
        String action = isSftp1 ? ProcessActionEnum.FILE_STORAGE.getDesc() : ProcessActionEnum.BANK_FILE.getDesc();
        boolean isValid = isSftp1 || isSftp2 ?
                fileControlRepository.validateUpload(action) :
                fileControlRepository.validate();
        if (isSftp2 && !isValid) {
            stepExecution.getJobExecution().getExecutionContext()
                    .putString(ConfrontaConstants.IS_TERMINATED_ONLY, Boolean.TRUE.toString());
        }
        ExitStatusConfronta exitStatusConfronta = isSftp1 ? ExitStatusConfronta.UPLOAD_SFTP1_FAILED :
                                        isSftp2 ? ExitStatusConfronta.UPLOAD_SFTP2_FAILED :
                                        ExitStatusConfronta.GENERATION_FILE_FAILED;
        stepExecution.setExitStatus(isValid ? ExitStatus.COMPLETED : exitStatusConfronta);
        return RepeatStatus.FINISHED;
    }
}
