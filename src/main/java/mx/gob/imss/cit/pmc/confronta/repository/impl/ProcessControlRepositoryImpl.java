package mx.gob.imss.cit.pmc.confronta.repository.impl;

import mx.gob.imss.cit.pmc.confronta.constants.ConfrontaConstants;
import mx.gob.imss.cit.pmc.confronta.dto.ProcessControlDTO;
import mx.gob.imss.cit.pmc.confronta.enums.FileControlEnum;
import mx.gob.imss.cit.pmc.confronta.enums.ProcessControlEnum;
import mx.gob.imss.cit.pmc.confronta.repository.ProcessControlRepository;
import mx.gob.imss.cit.pmc.confronta.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class ProcessControlRepositoryImpl implements ProcessControlRepository {

    @Autowired
    private MongoOperations mongoOperations;

    @Override
    public void createError(String action, Long key) {
        ProcessControlDTO processControlDTO = exist(action, key);
        if (processControlDTO != null) {
            processControlDTO.setFechaActualizacion(DateUtils.getCurrentMexicoDate());
            processControlDTO.setControl(ProcessControlEnum.FAILED.getDesc());
        } else {
            processControlDTO = buildProcessControl(action, ProcessControlEnum.FAILED.getDesc(), key);
        }
        mongoOperations.save(processControlDTO);
    }

    @Override
    public void createCorrect(String action, Long key) {
        ProcessControlDTO processControlDTO = exist(action, key);
        if (processControlDTO != null) {
            processControlDTO.setFechaActualizacion(DateUtils.getCurrentMexicoDate());
            processControlDTO.setControl(ProcessControlEnum.CORRECT.getDesc());                                   
        } else {
            processControlDTO = buildProcessControl(action, ProcessControlEnum.CORRECT.getDesc(), key);
        }
        mongoOperations.save(processControlDTO);
    }

    private ProcessControlDTO buildProcessControl(String action, String control, Long key) {
        ProcessControlDTO processControlDTO = new ProcessControlDTO();
        processControlDTO.setAccion(action);
        processControlDTO.setFecha(DateUtils.getCurrentMexicoDate());
        processControlDTO.setControl(control);
        processControlDTO.setKey(key);
        processControlDTO.setAnioRevision(Integer.parseInt(DateUtils.getCurrentYear()) -1 );
        return processControlDTO;
    }

    @Override
    public boolean validateAction(String action) {
        ProcessControlDTO processControlDTO = mongoOperations.findOne(buildValidationQuery(action), ProcessControlDTO.class);
        return processControlDTO != null && processControlDTO.getControl().equals(ProcessControlEnum.CORRECT.getDesc());
    }

    private Query buildValidationQuery(String action) {
        Date beginDate = DateUtils.calculateBeginDate(DateUtils.getCurrentYear(), ConfrontaConstants.FIRST_MONTH,
                ConfrontaConstants.FIRST_DAY);
        Date endDate = DateUtils.calculateEndDate(DateUtils.getCurrentYear(), ConfrontaConstants.LAST_MONTH,
                ConfrontaConstants.LAST_DECEMBER_DAY);
        return new Query(new Criteria().andOperator(
                Criteria.where(ProcessControlEnum.ACTION.getDesc()).is(action),
                Criteria.where(ProcessControlEnum.CONTROL.getDesc()).is(ProcessControlEnum.CORRECT.getDesc()),
                Criteria.where(ProcessControlEnum.DATE.getDesc()).gt(beginDate).lt(endDate)
        ));
    }

    @Override
    public List<ProcessControlDTO> findAllError(List<String> actions) {
        return mongoOperations.find(buildFindAllErrorQuery(actions), ProcessControlDTO.class);
    }

    private Query buildFindAllErrorQuery(List<String> actions) {
        Date beginDate = DateUtils.calculateBeginDate(DateUtils.getCurrentYear(), ConfrontaConstants.FIRST_MONTH,
                ConfrontaConstants.FIRST_DAY);
        Date endDate = DateUtils.calculateEndDate(DateUtils.getCurrentYear(), ConfrontaConstants.LAST_MONTH,
                ConfrontaConstants.LAST_DECEMBER_DAY);
        return new Query(new Criteria().andOperator(
                Criteria.where(ProcessControlEnum.ACTION.getDesc()).in(actions),
                Criteria.where(ProcessControlEnum.CONTROL.getDesc()).is(FileControlEnum.FAILED.getDesc()),
                Criteria.where(ProcessControlEnum.DATE.getDesc()).gt(beginDate).lt(endDate)
        ));
    }

    private ProcessControlDTO exist(String action, Long key) {
        return mongoOperations.findOne(buildExistQuery(action, key), ProcessControlDTO.class);
    }

    private Query buildExistQuery(String action, Long key) {
        Date beginDate = DateUtils.calculateBeginDate(DateUtils.getCurrentYear(), ConfrontaConstants.FIRST_MONTH,
                ConfrontaConstants.FIRST_DAY);
        Date endDate = DateUtils.calculateEndDate(DateUtils.getCurrentYear(), ConfrontaConstants.LAST_MONTH,
                ConfrontaConstants.LAST_DECEMBER_DAY);
        return new Query(new Criteria().andOperator(
                Criteria.where(ProcessControlEnum.ACTION.getDesc()).is(action),
                Criteria.where(ProcessControlEnum.KEY.getDesc()).is(key),
                Criteria.where(ProcessControlEnum.DATE.getDesc()).gt(beginDate).lt(endDate)
        ));
    }

}
