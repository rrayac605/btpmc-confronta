package mx.gob.imss.cit.pmc.confronta.repository;

import mx.gob.imss.cit.pmc.confronta.dto.FileControlDTO;

import java.util.List;
import java.util.Set;

public interface FileControlRepository {

    void createError(String action, Long key);

    void createCorrect(String action, Long key);

    boolean validate();

    boolean validateUpload(String action);

    Set<Long> findKeyListOfGeneratedFiles();

    Set<Long> findKeyListOfStoredFiles();

    Set<Long> findKeyListOfBankedFiles();

    List<FileControlDTO> findAllError(List<String> actions);

	List<FileControlDTO> encuentraCorrectos(List<String> accion);

}
