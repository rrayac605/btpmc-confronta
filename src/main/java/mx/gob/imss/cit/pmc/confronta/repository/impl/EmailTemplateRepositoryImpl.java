package mx.gob.imss.cit.pmc.confronta.repository.impl;

import mx.gob.imss.cit.pmc.confronta.dto.EmailTemplateDTO;
import mx.gob.imss.cit.pmc.confronta.repository.EmailTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class EmailTemplateRepositoryImpl implements EmailTemplateRepository {

    @Autowired
    private MongoOperations mongoOperations;

    @Override
    public EmailTemplateDTO findByName(String name) {
        Query query = new Query(Criteria.where("name").is(name));
        return mongoOperations.findOne(query, EmailTemplateDTO.class);
    }
}
