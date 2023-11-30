package mx.gob.imss.cit.pmc.confronta.dto;

import com.mongodb.lang.Nullable;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

public class FechasAuditoriaDTO {

	@Setter
	@Getter
	@Nullable
	private Date fecAlta;
	@Setter
	@Getter
	@Nullable
	private Date fecBaja;
	@Setter
	@Getter
	@Nullable
	private Date fecActualizacion;

}
