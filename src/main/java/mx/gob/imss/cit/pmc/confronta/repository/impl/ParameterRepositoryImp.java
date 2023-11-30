package mx.gob.imss.cit.pmc.confronta.repository.impl;

import mx.gob.imss.cit.pmc.confronta.dto.ParameterDTO;
import mx.gob.imss.cit.pmc.confronta.repository.ParameterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ParameterRepositoryImp implements ParameterRepository {

    @Autowired
    private MongoOperations mongoOperations;

    @Override
    @SuppressWarnings("unchecked")
    public Optional<ParameterDTO<String>> findOneByCve(String cve) {
        Query query = new Query(Criteria.where("cveIdParametro").is(cve));
        return Optional.ofNullable(mongoOperations.findOne(query, ParameterDTO.class));
    }

    @Override
    @SuppressWarnings("unchecked")
    public Optional<ParameterDTO<List<String>>> findListByCve(String cve) {
        Query query = new Query(Criteria.where("cveIdParametro").is(cve));
        return Optional.ofNullable(mongoOperations.findOne(query, ParameterDTO.class));
    }
}
