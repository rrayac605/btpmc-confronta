package mx.gob.imss.cit.pmc.confronta.listener;

import lombok.SneakyThrows;
import mx.gob.imss.cit.pmc.confronta.constants.ConfrontaConstants;
import mx.gob.imss.cit.pmc.confronta.repository.ProcessAuditRepository;
import mx.gob.imss.cit.pmc.confronta.repository.ProcessControlRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@StepScope
public class MongoWriterListener<T> implements ItemWriteListener<T> {

    private static final Logger logger = LoggerFactory.getLogger(MongoWriterListener.class);

    private static Long key;

    @Autowired
    private ProcessControlRepository processControlRepository;

    @Autowired
    private ProcessAuditRepository processAuditRepository;

    @Value("#{stepExecution}")
    private StepExecution stepExecution;

    @Override
    public void beforeWrite(List<? extends T> items) {

    }

    @Override
    public void afterWrite(List<? extends T> items) {

    }

    @SneakyThrows
    @Override
    public void onWriteError(Exception e, List<? extends T> items) {
        stepExecution.getJobExecution().getExecutionContext()
                .putString(ConfrontaConstants.IS_TERMINATED_ONLY, Boolean.TRUE.toString());
    }
}
