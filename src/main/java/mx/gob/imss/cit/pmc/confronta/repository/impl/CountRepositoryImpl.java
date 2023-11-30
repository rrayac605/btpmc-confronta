package mx.gob.imss.cit.pmc.confronta.repository.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.stereotype.Repository;

import mx.gob.imss.cit.pmc.confronta.dto.CountDTO;
import mx.gob.imss.cit.pmc.confronta.repository.CountRepository;

@Repository
public class CountRepositoryImpl implements CountRepository {

    @Autowired
    private MongoOperations mongoOperations;
    
    private static final Logger logger = LoggerFactory.getLogger(CountRepositoryImpl.class);

    @Override
    public long count(TypedAggregation<?> aggregation) {
    	CountDTO countDTO = null;
    	try {
    		countDTO = mongoOperations.aggregate(aggregation, CountDTO.class).getUniqueMappedResult();
    	}
    	catch (Exception e) {
    	}
        return countDTO != null && countDTO.getCount() != null ? countDTO.getCount() : 0L;
    }
}
