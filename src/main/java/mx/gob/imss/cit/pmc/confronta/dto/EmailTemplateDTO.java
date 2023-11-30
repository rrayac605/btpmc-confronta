package mx.gob.imss.cit.pmc.confronta.dto;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document("MCC_EMAIL_TEMPLATE")
public class EmailTemplateDTO {

    @Id
    private ObjectId objectId;

    private String template;

    private String name;

    private String subject;

    private String from;

    private String[] to;

}
