package mx.gob.imss.cit.pmc.confronta.repository;

import mx.gob.imss.cit.pmc.confronta.dto.EmailTemplateDTO;

public interface EmailTemplateRepository {

    EmailTemplateDTO findByName(String name);

}
