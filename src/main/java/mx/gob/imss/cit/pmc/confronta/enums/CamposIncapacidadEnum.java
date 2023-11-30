package mx.gob.imss.cit.pmc.confronta.enums;

import lombok.Getter;

@Getter
public enum CamposIncapacidadEnum {

    CONSECUENCIA("incapacidadDTO.cveConsecuencia"),
    FEC_INICIO("incapacidadDTO.fecInicio"),
    FEC_ACCIDENTE("incapacidadDTO.fecAccidente"),
    FEC_ALTA("incapacidadDTO.fecAlta"),
    FEC_ALTA_INCAPACIDAD("incapacidadDTO.fecAltaIncapacidad"),
    FEC_INI_PENSION("incapacidadDTO.fecIniPension"),
    NUM_DIAS_SUBSIDIADOS("incapacidadDTO.numDiasSubsidiados"),
    FEC_FIN("incapacidadDTO.fecFin"),
    TIPO_RIESGO("incapacidadDTO.cveTipoRiesgo"),
    PORCENTAJE_INCAPACIDAD("incapacidadDTO.porPorcentajeIncapacidad");

    private final String desc;

    CamposIncapacidadEnum(String desc) {
        this.desc = desc;
    }

}
