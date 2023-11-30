package mx.gob.imss.cit.pmc.confronta.tasklet;

import lombok.SneakyThrows;
import mx.gob.imss.cit.pmc.confronta.constants.ConfrontaConstants;
import mx.gob.imss.cit.pmc.confronta.dto.ChangeDTO;
import mx.gob.imss.cit.pmc.confronta.dto.MovementDTO;
import mx.gob.imss.cit.pmc.confronta.repository.CountRepository;
import mx.gob.imss.cit.pmc.confronta.utils.DateUtils;
import mx.gob.imss.cit.pmc.confronta.utils.NumberUtils;
import mx.gob.imss.cit.pmc.confronta.utils.ReaderUtils;
import mx.gob.imss.cit.pmc.confronta.utils.StringUtils;
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
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Component
@StepScope
public class FooterTasklet implements Tasklet {

    @Value("#{stepExecution}")
    private StepExecution stepExecution;

    @Autowired
    private CountRepository countRepository;

    private static final Logger logger = LoggerFactory.getLogger(FooterTasklet.class);

    @SneakyThrows
    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) {
        try {
            Long key = stepExecution.getJobParameters().getLong(ConfrontaConstants.KEY_PARAM);
            List<Integer> subDelList = NumberUtils.getSubDelList(key);
            Integer del = NumberUtils.getDel(key);
            boolean isReprocess = Boolean.TRUE.toString().equals(
                    stepExecution.getJobParameters().getString(ConfrontaConstants.REPROCESS_FLAG));            
            TypedAggregation<MovementDTO> movementsAggregation = ReaderUtils.buildMovementsCountAggregation(del, subDelList, isReprocess);
            TypedAggregation<ChangeDTO> changesAggregation = ReaderUtils.buildChangesCountAggregation(del, subDelList, isReprocess);
            logger.info("Aggregacion de movimientos {}", movementsAggregation);
            logger.info("Aggregacion de cambios {}", changesAggregation);
            long count = countRepository.count(movementsAggregation) + countRepository.count(changesAggregation);
            logger.info("Registros insertados {}", count);
            String header = ConfrontaConstants.CTROL_LLAVE_CTL
                    .concat(ConfrontaConstants.CTROL_HEADER)
                    .concat(StringUtils.safeAddZero(del, 2))
                    .concat(ConfrontaConstants.CTROL_NOMB_ARCH_CTL)
                    .concat(ConfrontaConstants.CTROL_CVE_CTL)
                    .concat(DateUtils.getAACCC())
                    .concat(DateUtils.getFileFormattedDate(new Date()))
                    .concat(DateUtils.getFileFormattedHour(new Date()))
                    .concat(StringUtils.safeAddZero((int) count, 10))
                    .concat(ConfrontaConstants.FILLER);
            BufferedWriter writter = new BufferedWriter(new FileWriter(StringUtils
                    .buildFileName(key, ConfrontaConstants.BASE_PATH_SERVER, ConfrontaConstants.PATH_FTP1), Boolean.TRUE));
            writter.write(header);
            writter.close();
            return RepeatStatus.FINISHED;
        } catch (IOException e) {
            stepExecution.getJobExecution().getExecutionContext()
                    .putString(ConfrontaConstants.IS_TERMINATED_ONLY, Boolean.TRUE.toString());
            e.printStackTrace();
            return RepeatStatus.CONTINUABLE;
        }
    }
}
