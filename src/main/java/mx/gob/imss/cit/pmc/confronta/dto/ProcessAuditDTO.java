package mx.gob.imss.cit.pmc.confronta.dto;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@Document("AUDITORIA")
public class ProcessAuditDTO {

    @Id
    private ObjectId objectId;

    private Date fechaAccion;

    private Date fechaActualizacion;

    private String usuarioResponsable;

    private String accion;

    private String resultado;

    private Long key;

}
