package mx.gob.imss.cit.pmc.confronta.tasklet;

import mx.gob.imss.cit.pmc.confronta.constants.ConfrontaConstants;
import mx.gob.imss.cit.pmc.confronta.repository.BackupRepository;
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
public class RollBackTasklet implements Tasklet {

    @Value("#{stepExecution}")
    private StepExecution stepExecution;

    @Autowired
    private BackupRepository backupRepository;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        try {
            Long key = stepExecution.getJobParameters().getLong(ConfrontaConstants.KEY_PARAM);
            backupRepository.deleteByKey(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RepeatStatus.FINISHED;
    }

}
