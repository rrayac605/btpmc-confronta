package mx.gob.imss.cit.pmc.confronta.repository.impl;

import mx.gob.imss.cit.pmc.confronta.constants.ConfrontaConstants;
import mx.gob.imss.cit.pmc.confronta.dto.ProcessAuditDTO;
import mx.gob.imss.cit.pmc.confronta.enums.ProcessAuditEnum;
import mx.gob.imss.cit.pmc.confronta.repository.ProcessAuditRepository;
import mx.gob.imss.cit.pmc.confronta.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class ProcessAuditRepositoryImpl implements ProcessAuditRepository {

    @Autowired
    private MongoOperations mongoOperations;

    @Override
    public void createIncorrect(String action, Long key) {
        ProcessAuditDTO processAuditDTO = exist(action, key);
        if (processAuditDTO != null) {
            processAuditDTO.setFechaActualizacion(DateUtils.getCurrentMexicoDate());
            processAuditDTO.setResultado(ProcessAuditEnum.INCORRECT.getDesc());
        } else {
            processAuditDTO = buildProcessControl(action, ProcessAuditEnum.INCORRECT.getDesc(), key);
        }
        mongoOperations.save(processAuditDTO);
    }

    @Override
    public void createCorrect(String action, Long key) {
        ProcessAuditDTO processAuditDTO = exist(action, key);
        if (processAuditDTO != null) {
            processAuditDTO.setFechaActualizacion(DateUtils.getCurrentMexicoDate());
            processAuditDTO.setResultado(ProcessAuditEnum.CORRECT.getDesc());
        } else {
            processAuditDTO = buildProcessControl(action, ProcessAuditEnum.CORRECT.getDesc(), key);
        }
        mongoOperations.save(processAuditDTO);
    }

    private ProcessAuditDTO buildProcessControl(String action, String result, Long key) {
        ProcessAuditDTO processAuditDTO = new ProcessAuditDTO();
        processAuditDTO.setAccion(action);
        processAuditDTO.setFechaAccion(DateUtils.getCurrentMexicoDate());
        processAuditDTO.setResultado(result);
        processAuditDTO.setUsuarioResponsable(ConfrontaConstants.USER);
        processAuditDTO.setKey(key);
        return processAuditDTO;
    }

    private ProcessAuditDTO exist(String action, Long key) {
        return mongoOperations.findOne(buildExistQuery(action, key), ProcessAuditDTO.class);
    }

    private Query buildExistQuery(String action, Long key) {
        Date beginDate = DateUtils.calculateBeginDate(DateUtils.getCurrentYear(), ConfrontaConstants.FIRST_MONTH,
                ConfrontaConstants.FIRST_DAY);
        Date endDate = DateUtils.calculateEndDate(DateUtils.getCurrentYear(), ConfrontaConstants.LAST_MONTH,
                ConfrontaConstants.LAST_DECEMBER_DAY);
        return new Query(new Criteria().andOperator(
                Criteria.where(ProcessAuditEnum.ACTION.getDesc()).is(action),
                Criteria.where(ProcessAuditEnum.KEY.getDesc()).is(key),
                Criteria.where(ProcessAuditEnum.ACTION_DATE.getDesc()).gt(beginDate).lt(endDate)
        ));
    }

}
