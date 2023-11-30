package mx.gob.imss.cit.pmc.confronta.enums;

import lombok.Getter;

@Getter
public enum CamposAseguradoEnum {

    FEC_ALTA("aseguradoDTO.fecAlta"),
    ESTADO_REGISTRO("aseguradoDTO.cveEstadoRegistro"),
    CASO_REGISTRO("aseguradoDTO.cveCasoRegistro"),
    CYCLE("aseguradoDTO.numCicloAnual"),
    NUM_NSS("aseguradoDTO.numNss"),
    NOMBRE("aseguradoDTO.nomAsegurado"),
    PRIMER_APELLIDO("aseguradoDTO.refPrimerApellido"),
    SEGUNDO_APELLIDO("aseguradoDTO.refSegundoApellido"),
    CURP("aseguradoDTO.refCurp"),
    DELEGACION_NSS("aseguradoDTO.cveDelegacionNss"),
    SUB_DELEGACION_NSS("aseguradoDTO.cveSubdelNss"),
    FEC_BAJA("aseguradoDTO.fecBaja");

    private final String desc;

    CamposAseguradoEnum(String desc) {
        this.desc = desc;
    }

}
