package mx.gob.imss.cit.pmc.confronta.dto;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

@Document("MCT_MOVIMIENTO")
public class MovementDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public MovementDTO() {}

	@Getter
	@Setter
	@Id
	private ObjectId objectId;

	@Getter
	@Setter
	private ObjectId identificadorArchivo;

	@Getter
	@Setter
	private String cveOrigenArchivo;

	@Getter
	@Setter
	private AseguradoDTO aseguradoDTO;

	@Getter
	@Setter
	private PatronDTO patronDTO;

	@Getter
	@Setter
	private IncapacidadDTO incapacidadDTO;
	
	@Getter
	@Setter
	private AseguradoDTO aseguradoPasoAl;	
	
	@Getter
	@Setter
	private List<AuditDTO> lstAuditoria;
	
	@Getter
	@Setter
	private List<AuditDTO> auditorias;

	/** Unicamente se utiliza para ejecutar la consulta, no se persiste este valor */
	@Getter
	private List<ChangeDTO> changeDTO;
	
	@Getter
	@Setter
	private boolean isPending;

	@Override
	public String toString() {
		return "DetalleRegistroDTO [objectId=" + objectId + ", identificadorArchivo=" + identificadorArchivo
				+ ", cveOrigenArchivo=" + cveOrigenArchivo + ", aseguradoDTO=" + aseguradoDTO + ", patronDTO="
				+ patronDTO + ", incapacidadDTO=" + incapacidadDTO + "]";
	}

}
