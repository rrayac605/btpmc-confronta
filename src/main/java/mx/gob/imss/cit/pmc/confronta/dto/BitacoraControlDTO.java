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
@Document("BITACORA_CONTROL")
public class BitacoraControlDTO {
		
	@Id
	private ObjectId objectId;
	
	private Integer del;
	
	private List<Integer> subDel;
	
	private String accion;
	
	private Date fecEjecucion;
	
	private String control;
	
	private Integer key;

}
