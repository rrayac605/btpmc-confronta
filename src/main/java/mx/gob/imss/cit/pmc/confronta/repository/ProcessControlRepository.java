package mx.gob.imss.cit.pmc.confronta.repository;

import mx.gob.imss.cit.pmc.confronta.dto.ProcessControlDTO;

import java.util.List;

public interface ProcessControlRepository {

    void createError(String action, Long key);

    void createCorrect(String action, Long key);

    boolean validateAction(String action);

    List<ProcessControlDTO> findAllError(List<String> actions);

}
