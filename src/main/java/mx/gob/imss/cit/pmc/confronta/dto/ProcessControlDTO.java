package mx.gob.imss.cit.pmc.confronta.dto;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Document("CONTROL_PROCESO")
public class ProcessControlDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private ObjectId objectId;

    private String accion;

    private Date fecha;

    private Date fechaActualizacion;
    
    private Integer anioRevision;

    private String control;

    private Long key;

    public ProcessControlDTO() {}

    public ProcessControlDTO(FileControlDTO fileControl) {
        setObjectId(fileControl.getObjectId());
        setAccion(fileControl.getAccion());
        setFecha(fileControl.getFecCreacion());
        setControl(fileControl.getControl());
        setKey(fileControl.getKey());
        setFechaActualizacion(fileControl.getFechaActualizacion());
    }

}
