package mx.gob.imss.cit.pmc.confronta.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

public class AseguradoDTO extends FechasAuditoriaDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	@Setter
	@Getter
	private Integer cveCasoRegistro;
	@Setter
	@Getter
	private String cveCodigoError;
	@Setter
	@Getter
	private Integer cveDelegacionAtencion;
	@Setter
	@Getter
	private Integer cveDelegacionNss;
	@Setter
	@Getter
	private Integer cveEstadoRegistro;
	@Setter
	@Getter
	private Integer cveIdPersona;
	@Setter
	@Getter
	private String cveOcupacion;
	@Setter
	@Getter
	private Integer cveSubDelAtencion;
	@Setter
	@Getter
	private Integer cveSubdelNss;
	@Setter
	@Getter
	private Integer cveUmfAdscripcion;
	@Setter
	@Getter
	private Integer cveUmfExp;
	@Setter
	@Getter
	private Integer cveUmfPagadora;
	@Setter
	@Getter
	private String desCasoRegistro;
	@Setter
	@Getter
	private String desCodigoError;
	@Setter
	@Getter
	private String desDelegacionAtencion;
	@Setter
	@Getter
	private String desDelegacionNss;
	@Setter
	@Getter
	private String desEstadoRegistro;
	@Setter
	@Getter
	private String desOcupacion;
	@Setter
	@Getter
	private String desSubDelAtencion;
	@Setter
	@Getter
	private String desSubDelNss;
	@Setter
	@Getter
	private String desUmfAdscripcion;
	@Setter
	@Getter
	private String desUmfExp;
	@Setter
	@Getter
	private String desUmfPagadora;
	@Setter
	@Getter
	private String nomAsegurado;
	@Setter
	@Getter
	private String numCicloAnual;
	@Setter
	@Getter
	private Integer numIndice;
	@Setter
	@Getter
	private String numNss;
	@Setter
	@Getter
	private String numNssPasoAl;
	@Setter
	@Getter
	private BigDecimal numSalarioDiario;
	@Setter
	@Getter
	private String objectIdArchivo;
	@Setter
	@Getter
	private String refCurp;
	@Setter
	@Getter
	private String refFolioOriginal;
	@Setter
	@Getter
	private String refPrimerApellido;
	@Setter
	@Getter
	private String refSegundoApellido;
	
    @Setter
    @Getter
    private Boolean sinUMF;
    
	@Override
	public String toString() {
		return "AseguradoDTO [cveCasoRegistro=" + cveCasoRegistro + ", cveCodigoError=" + cveCodigoError
				+ ", cveDelegacionAtencion=" + cveDelegacionAtencion + ", cveDelegacionNss=" + cveDelegacionNss
				+ ", cveEstadoRegistro=" + cveEstadoRegistro + ", cveIdPersona=" + cveIdPersona + ", cveOcupacion="
				+ cveOcupacion + ", cveSubDelAtencion=" + cveSubDelAtencion + ", cveSubdelNss=" + cveSubdelNss
				+ ", cveUmfAdscripcion=" + cveUmfAdscripcion + ", cveUmfExp=" + cveUmfExp + ", cveUmfPagadora="
				+ cveUmfPagadora + ", desCasoRegistro=" + desCasoRegistro + ", desCodigoError=" + desCodigoError
				+ ", desDelegacionAtencion=" + desDelegacionAtencion + ", desDelegacionNss=" + desDelegacionNss
				+ ", desEstadoRegistro=" + desEstadoRegistro + ", desOcupacion=" + desOcupacion + ", desSubDelAtencion="
				+ desSubDelAtencion + ", desSubDelNss=" + desSubDelNss + ", desUmfAdscripcion=" + desUmfAdscripcion
				+ ", desUmfExp=" + desUmfExp + ", desUmfPagadora=" + desUmfPagadora + ", nomAsegurado=" + nomAsegurado
				+ ", numCicloAnual=" + numCicloAnual + ", numIndice=" + numIndice + ", numNss=" + numNss
				+ ", numSalarioDiario=" + numSalarioDiario + ", objectIdArchivo=" + objectIdArchivo + ", refCurp="
				+ refCurp + ", refFolioOriginal=" + refFolioOriginal + ", refPrimerApellido=" + refPrimerApellido
				+ ", refSegundoApellido=" + refSegundoApellido + "]";
	}
	
}
