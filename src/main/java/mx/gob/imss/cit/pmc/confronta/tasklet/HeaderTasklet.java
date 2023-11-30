package mx.gob.imss.cit.pmc.confronta.tasklet;

import lombok.SneakyThrows;
import mx.gob.imss.cit.pmc.confronta.constants.ConfrontaConstants;
import mx.gob.imss.cit.pmc.confronta.utils.DateUtils;
import mx.gob.imss.cit.pmc.confronta.utils.NumberUtils;
import mx.gob.imss.cit.pmc.confronta.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Component
@StepScope
public class HeaderTasklet implements Tasklet {

    private static final Logger logger = LoggerFactory.getLogger(HeaderTasklet.class);
    
    private static int conteoReprocesos = 0;

    @Value("#{stepExecution}")
    private StepExecution stepExecution;

    @SneakyThrows
    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) {
        try {            
            Long key  = stepExecution.getJobParameters().getLong(ConfrontaConstants.KEY_PARAM);
            Integer delFinal = NumberUtils.getDel(key);
            List<Integer> delegaciones = NumberUtils.getSubDelList(key);
            String subDel = StringUtils.subDelToString(delegaciones);
            
            
            //Agregar exception para error al generar archivo
            String filePath = StringUtils.buildFilePath(key, ConfrontaConstants.BASE_PATH_SERVER, ConfrontaConstants.PATH_FTP1);
            File directory = new File(filePath);
            if (directory.exists() || directory.mkdirs()) {
                assert stepExecution != null;
                String header = ConfrontaConstants.CTROL_LLAVE_CTL
                        .concat(ConfrontaConstants.CTROL_HEADER)
                        .concat(StringUtils.getDelegacionFiller(delFinal, subDel))
                        .concat(ConfrontaConstants.CTROL_NOMB_ARCH_CTL)
                        .concat(ConfrontaConstants.CTROL_CVE_CTL)
                        .concat(DateUtils.getAACCC())
                        .concat(DateUtils.getFileFormattedDate(new Date()))
                        .concat(DateUtils.getFileFormattedHour(new Date()))
                        .concat(ConfrontaConstants.CTROL_TOTAL_REG_CTL)
                        .concat(ConfrontaConstants.FILLER)
                        .concat("\n");
                System.out.println("header -->    "+ header);
                BufferedWriter writter = new BufferedWriter(new FileWriter(StringUtils
                        .buildFileName(key, ConfrontaConstants.BASE_PATH_SERVER, ConfrontaConstants.PATH_FTP1)));
                writter.write(header);
                writter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            stepExecution.getJobExecution().getExecutionContext()
                    .putString(ConfrontaConstants.IS_TERMINATED_ONLY, Boolean.TRUE.toString());
        }
        return RepeatStatus.FINISHED;
    }

}
