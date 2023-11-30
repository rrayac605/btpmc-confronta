package mx.gob.imss.cit.pmc.confronta.listener;

import mx.gob.imss.cit.pmc.confronta.constants.ConfrontaConstants;
import mx.gob.imss.cit.pmc.confronta.repository.ProcessAuditRepository;
import mx.gob.imss.cit.pmc.confronta.repository.ProcessControlRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class MongoProcessorListener<T, S> implements ItemProcessListener<T, S> {

    private static final Logger logger = LoggerFactory.getLogger(MongoProcessorListener.class);

    private static Long key;

    @Autowired
    private ProcessControlRepository processControlRepository;

    @Autowired
    private ProcessAuditRepository processAuditRepository;

    @Value("#{stepExecution}")
    private StepExecution stepExecution;

    @Override
    public void beforeProcess(T o) {
    }

    @Override
    public void onProcessError(T o, Exception e) {
        stepExecution.getJobExecution().getExecutionContext()
                .putString(ConfrontaConstants.IS_TERMINATED_ONLY, Boolean.TRUE.toString());
    }

    @Override
    public void afterProcess(T o, S o2) {
    }
}
