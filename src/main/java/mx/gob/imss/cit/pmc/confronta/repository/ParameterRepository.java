package mx.gob.imss.cit.pmc.confronta.repository;

import mx.gob.imss.cit.pmc.confronta.dto.ParameterDTO;

import java.util.List;
import java.util.Optional;

public interface ParameterRepository {

    Optional<ParameterDTO<String>> findOneByCve(String cve);

    Optional<ParameterDTO<List<String>>> findListByCve(String cve);

}
