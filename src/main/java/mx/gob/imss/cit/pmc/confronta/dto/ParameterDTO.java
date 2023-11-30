package mx.gob.imss.cit.pmc.confronta.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(collection = "MCC_PARAMETRO")
@JsonPropertyOrder({ "cveIdParametro", "cveIdParametro" })
public class ParameterDTO<T> extends FechasAuditoriaDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	@Getter
	@Setter
	private String cveIdParametro;
	@Getter
	@Setter
	private T desParametro;

}
