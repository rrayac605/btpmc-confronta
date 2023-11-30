package mx.gob.imss.cit.pmc.confronta.dto;

import com.mongodb.lang.Nullable;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.math.BigDecimal;

@Document
public class PatronDTO extends FechasAuditoriaDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	@Setter
	@Getter
	@Nullable
	private Integer cveClase;
	@Setter
	@Getter	
	private String cveDelegacionAux;
	@Setter
	@Getter
	@Nullable
	private Integer cveDelRegPatronal;
	@Setter
	@Getter
	@Nullable
	private Integer cveFraccion;
	@Setter
	@Getter
	private String cveModalidad;
	@Setter
	@Getter
	@Nullable
	private Integer cveSubDelRegPatronal;
	@Setter
	@Getter	
	private String desClase;
	@Setter
	@Getter
	@Nullable
	private String desDelRegPatronal;
	@Setter
	@Getter
	@Nullable
	private String desFraccion;
	@Setter
	@Getter
	@Nullable
	private String desPrima;
	@Setter
	@Getter
	@Nullable
	private String desRazonSocial;
	@Setter
	@Getter
	@Nullable
	private String desRfc;
	@Setter
	@Getter
	@Nullable
	private String desSubDelRegPatronal;
	@Setter
	@Getter
	@Nullable
	private BigDecimal numPrima;
	
	@Setter
	@Getter
	@Nullable
	private String objectIdPatron;
	
	@Setter
	@Getter
	@Field
	@Nullable
	private String refRegistroPatronal;

	@Override
	public String toString() {
		return "PatronDTO [cveClase=" + cveClase + ", cveDelegacionAux=" + cveDelegacionAux + ", cveDelRegPatronal="
				+ cveDelRegPatronal + ", cveFraccion=" + cveFraccion + ", cveModalidad=" + cveModalidad
				+ ", cveSubDelRegPatronal=" + cveSubDelRegPatronal + ", desClase=" + desClase + ", desDelRegPatronal="
				+ desDelRegPatronal + ", desFraccion=" + desFraccion + ", desPrima=" + desPrima + ", desRazonSocial="
				+ desRazonSocial + ", desRfc=" + desRfc + ", desSubDelRegPatronal=" + desSubDelRegPatronal
				+ ", numPrima=" + numPrima + ", objectIdPatron=" + objectIdPatron + ", refRegistroPatronal="
				+ refRegistroPatronal + "]";
	}
	
	

}
