package mx.gob.imss.cit.pmc.confronta.repository.impl;

import mx.gob.imss.cit.pmc.confronta.constants.ConfrontaConstants;
import mx.gob.imss.cit.pmc.confronta.dto.FileControlDTO;
import mx.gob.imss.cit.pmc.confronta.enums.FileControlEnum;
import mx.gob.imss.cit.pmc.confronta.enums.ProcessActionEnum;
import mx.gob.imss.cit.pmc.confronta.repository.FileControlRepository;
import mx.gob.imss.cit.pmc.confronta.utils.DateUtils;
import mx.gob.imss.cit.pmc.confronta.utils.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class FileControlRepositoryImpl implements FileControlRepository {

    @Autowired
    MongoOperations mongoOperations;

    @Override
    public void createError(String action, Long key) {
        FileControlDTO fileControl = exist(action, key);
        if (fileControl != null) {
            fileControl.setFechaActualizacion(DateUtils.getCurrentMexicoDate());
            fileControl.setControl(FileControlEnum.FAILED.getDesc());
        } else {
            fileControl = buildFileControl(action, key, FileControlEnum.FAILED.getDesc());
        }
        mongoOperations.save(fileControl);
    }

    @Override
    public void createCorrect(String action, Long key) {
        FileControlDTO fileControl = exist(action, key);
        if (fileControl != null) {
            fileControl.setFechaActualizacion(DateUtils.getCurrentMexicoDate());
            fileControl.setControl(FileControlEnum.CORRECT.getDesc());
        } else {
            fileControl = buildFileControl(action, key, FileControlEnum.CORRECT.getDesc());
        }
        mongoOperations.save(fileControl);
    }

    private FileControlDTO buildFileControl(String action, Long key, String control) {
        Integer del = NumberUtils.getDel(key);
        List<Integer> subDelList = NumberUtils.getSubDelList(key);
        FileControlDTO fileControlDTO = new FileControlDTO();
        fileControlDTO.setDel(del);
        fileControlDTO.setSubDel(subDelList);
        fileControlDTO.setAccion(action);
        fileControlDTO.setFecCreacion(DateUtils.getCurrentMexicoDate());
        fileControlDTO.setControl(control);
        fileControlDTO.setKey(key);
        return fileControlDTO;
    }

    @Override
    public boolean validate() {
        List<FileControlDTO> fileControlList = mongoOperations
                .find(buildValidationQuery(ProcessActionEnum.FILE_GENERATION.getDesc()), FileControlDTO.class);
        return ConfrontaConstants.DEL_SUBDEL.size() == fileControlList.size();
    }

    @Override
    public boolean validateUpload(String action) {
        List<FileControlDTO> fileControlList = mongoOperations
                .find(buildValidationQuery(action), FileControlDTO.class);
        return ConfrontaConstants.DEL_SUBDEL.size() == fileControlList.size();
    }

    private Query buildValidationQuery(String action) {
        Date beginDate = DateUtils.calculateBeginDate(DateUtils.getCurrentYear(), ConfrontaConstants.FIRST_MONTH,
                ConfrontaConstants.FIRST_DAY);
        Date endDate = DateUtils.calculateEndDate(DateUtils.getCurrentYear(), ConfrontaConstants.LAST_MONTH,
                ConfrontaConstants.LAST_DECEMBER_DAY);
        return new Query(new Criteria().andOperator(
                Criteria.where(FileControlEnum.ACTION.getDesc()).is(action),
                Criteria.where(FileControlEnum.CONTROL.getDesc()).is(FileControlEnum.CORRECT.getDesc()),
                Criteria.where(FileControlEnum.CREATION_DATE.getDesc()).gt(beginDate).lt(endDate)
        ));
    }

    @Override
    public Set<Long> findKeyListOfGeneratedFiles() {
        List<FileControlDTO> processControlList = mongoOperations.find(
                buildValidationQuery(ProcessActionEnum.FILE_GENERATION.getDesc()), FileControlDTO.class);
        return processControlList.stream().map(FileControlDTO::getKey).collect(Collectors.toSet());
    }

    @Override
    public Set<Long> findKeyListOfStoredFiles() {
        List<FileControlDTO> processControlList = mongoOperations.find(
                buildValidationQuery(ProcessActionEnum.FILE_STORAGE.getDesc()), FileControlDTO.class);
        return processControlList.stream().map(FileControlDTO::getKey).collect(Collectors.toSet());
    }

    @Override
    public Set<Long> findKeyListOfBankedFiles() {
        List<FileControlDTO> processControlList = mongoOperations.find(
                buildValidationQuery(ProcessActionEnum.BANK_FILE.getDesc()), FileControlDTO.class);
        return processControlList.stream().map(FileControlDTO::getKey).collect(Collectors.toSet());
    }

    @Override
    public List<FileControlDTO> findAllError(List<String> actions) {
        return mongoOperations.find(buildFindAllErrorQuery(actions), FileControlDTO.class);
    }
    
    @Override
    public List<FileControlDTO> encuentraCorrectos(List<String> accion) {
    	return mongoOperations.find(construyeEncuentraCorrectosQuery(accion), FileControlDTO.class);
    }

    private Query buildFindAllErrorQuery(List<String> actions) {
        Date beginDate = DateUtils.calculateBeginDate(DateUtils.getCurrentYear(), ConfrontaConstants.FIRST_MONTH,
                ConfrontaConstants.FIRST_DAY);
        Date endDate = DateUtils.calculateEndDate(DateUtils.getCurrentYear(), ConfrontaConstants.LAST_MONTH,
                ConfrontaConstants.LAST_DECEMBER_DAY);
        return new Query(new Criteria().andOperator(
                Criteria.where(FileControlEnum.ACTION.getDesc()).in(actions),
                Criteria.where(FileControlEnum.CONTROL.getDesc()).is(FileControlEnum.FAILED.getDesc()),
                Criteria.where(FileControlEnum.CREATION_DATE.getDesc()).gt(beginDate).lt(endDate)
        ));
    }
    
    private Query construyeEncuentraCorrectosQuery(List<String> accion) {
    	Date primerDia = DateUtils.calculateBeginDate(DateUtils.getCurrentYear(), ConfrontaConstants.FIRST_MONTH,
                ConfrontaConstants.FIRST_DAY);
    	Date ultimoDia =DateUtils.calculateEndDate(DateUtils.getCurrentYear(), ConfrontaConstants.LAST_MONTH,
                ConfrontaConstants.LAST_DECEMBER_DAY);
    	return new Query(new Criteria().andOperator(
    			Criteria.where(FileControlEnum.ACTION.getDesc()).in(accion),
    			Criteria.where(FileControlEnum.CONTROL.getDesc()).is(FileControlEnum.CORRECT.getDesc()),
    			Criteria.where(FileControlEnum.CREATION_DATE.getDesc()).gt(primerDia).lt(ultimoDia)
    	));
    }

    private FileControlDTO exist(String action, Long key) {
        return mongoOperations.findOne(buildExistQuery(action, key), FileControlDTO.class);
    }

    private Query buildExistQuery(String action, Long key) {
        Date beginDate = DateUtils.calculateBeginDate(DateUtils.getCurrentYear(), ConfrontaConstants.FIRST_MONTH,
                ConfrontaConstants.FIRST_DAY);
        Date endDate = DateUtils.calculateEndDate(DateUtils.getCurrentYear(), ConfrontaConstants.LAST_MONTH,
                ConfrontaConstants.LAST_DECEMBER_DAY);
        return new Query(new Criteria().andOperator(
                Criteria.where(FileControlEnum.ACTION.getDesc()).is(action),
                Criteria.where(FileControlEnum.KEY.getDesc()).is(key),
                Criteria.where(FileControlEnum.CREATION_DATE.getDesc()).gt(beginDate).lt(endDate)
        ));
    }

}
