package mx.gob.imss.cit.pmc.confronta.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Document
public class IncapacidadDTO extends FechasAuditoriaDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	@Setter
	@Getter
	private Integer cveActoInseguro;
	@Setter
	@Getter
	private Integer cveConsecuencia;
	@Setter
	@Getter
	private Integer cveLaudo;
	@Setter
	@Getter
	private String cveNaturaleza;
	@Setter
	@Getter
	private String cveReevaluacion;
	@Setter
	@Getter
	private Integer cveTipoIncapacidad;
	@Setter
	@Getter
	private Integer cveTipoRiesgo;
	@Setter
	@Getter
	private String desActoInseguro;
	@Setter
	@Getter
	private String desCausaExterna;
	@Setter
	@Getter
	private String desCodigoDiagnostico;
	@Setter
	@Getter
	private String desConsecuencia;
	@Setter
	@Getter
	private String desLaudo;
	@Setter
	@Getter
	private String desNaturaleza;
	@Setter
	@Getter
	private String desOcupacion;
	@Setter
	@Getter
	private String desRiesgoFisico;
	@Setter
	@Getter
	private String desTipoIncapacidad;
	@Setter
	@Getter
	private String desTipoRiesgo;
	@Setter
	@Getter
	private Date fecAccidente;
	@Setter
	@Getter
	private Date fecAltaIncapacidad;
	@Setter
	@Getter
	private Date fecAtencion;
	@Setter
	@Getter
	private Date fecExpDictamen;
	@Setter
	@Getter
	private Date fecFin;
	@Setter
	@Getter
	private Date fecInicio;
	@Setter
	@Getter
	private Date fecIniPension;
	@Setter
	@Getter
	private Integer numActoInseguro;
	@Setter
	@Getter
	private String numCausaExterna;
	@Setter
	@Getter
	private String numCodigoDiagnostico;
	@Setter
	@Getter
	private Integer numDiasSubsidiados;
	@Setter
	@Getter
	private String numMatMedAutCdst;
	@Setter
	@Getter
	private String numMatMedTratante;	
	@Setter
	@Getter
	private Integer numRiesgoFisico;
	@Setter
	@Getter
	private String objectIdIncapacidad;
	@Setter
	@Getter
	private BigDecimal porPorcentajeIncapacidad;
	@Override
	public String toString() {
		return "IncapacidadDTO [cveActoInseguro=" + cveActoInseguro + ", cveConsecuencia=" + cveConsecuencia
				+ ", cveLaudo=" + cveLaudo + ", cveNaturaleza=" + cveNaturaleza + ", cveReevaluacion=" + cveReevaluacion
				+ ", cveTipoIncapacidad=" + cveTipoIncapacidad + ", cveTipoRiesgo=" + cveTipoRiesgo
				+ ", desActoInseguro=" + desActoInseguro + ", desCausaExterna=" + desCausaExterna
				+ ", desCodigoDiagnostico=" + desCodigoDiagnostico + ", desConsecuencia=" + desConsecuencia
				+ ", desLaudo=" + desLaudo + ", desNaturaleza=" + desNaturaleza + ", desOcupacion=" + desOcupacion
				+ ", desRiesgoFisico=" + desRiesgoFisico + ", desTipoIncapacidad=" + desTipoIncapacidad
				+ ", desTipoRiesgo=" + desTipoRiesgo + ", fecAccidente=" + fecAccidente + ", fecAltaIncapacidad="
				+ fecAltaIncapacidad + ", fecAtencion=" + fecAtencion + ", fecExpDictamen=" + fecExpDictamen
				+ ", fecFin=" + fecFin + ", fecInicio=" + fecInicio + ", fecIniPension=" + fecIniPension
				+ ", numActoInseguro=" + numActoInseguro + ", numCausaExterna=" + numCausaExterna
				+ ", numCodigoDiagnostico=" + numCodigoDiagnostico + ", numDiasSubsidiados=" + numDiasSubsidiados
				+ ", numMatMedAutCdst=" + numMatMedAutCdst + ", numMatMedTratante=" + numMatMedTratante
				+ ", numRiesgoFisico=" + numRiesgoFisico + ", objectIdIncapacidad=" + objectIdIncapacidad
				+ ", porPorcentajeIncapacidad=" + porPorcentajeIncapacidad + "]";
	}
	
	

}
