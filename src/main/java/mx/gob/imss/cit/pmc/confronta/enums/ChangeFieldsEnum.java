package mx.gob.imss.cit.pmc.confronta.enums;

import lombok.Getter;

@Getter
public enum ChangeFieldsEnum {

    ID_ORIGEN("objectIdOrigen"),
    ORIGEN_ARCHIVO("cveOrigenArchivo"),
    FEC_CARGA("fecProcesoCarga"),
    FEC_ALTA("fecAlta"),
    FEC_BAJA("fecBaja"),
    ESTADO_REGISTRO("cveEstadoRegistro"),
    SITUACION_REGISTRO("cveSituacionRegistro"),
    CONSECUENCIA("cveConsecuencia"),
    CASO_REGISTRO("cveCasoRegistro"),
    CYCLE("numCicloAnual"),
    COLLECTION("MCT_CAMBIOS"),
    DTO_NAME("changeDTO"),
    DEL_PATRON("cveDelRegPatronal"),
    SUB_DEL_PATRON("cveSubDelRegPatronal"),
    IS_PENDING("isPending");

    private final String desc;

    ChangeFieldsEnum(String desc) {
        this.desc = desc;
    }

}
