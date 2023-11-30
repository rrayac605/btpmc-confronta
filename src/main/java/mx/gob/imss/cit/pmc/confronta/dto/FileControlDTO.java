package mx.gob.imss.cit.pmc.confronta.dto;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Document("MCB_BITACORA_CONTROL_ARCHIVOS_CIERRE_ANUAL_CASUISTICA")
public class FileControlDTO {

    @Id
    private ObjectId objectId;

    private Integer del;

    private List<Integer> subDel;

    private String accion;

    private Date fecCreacion;

    private Date fechaActualizacion;

    private String control;

    private Long key;

}
