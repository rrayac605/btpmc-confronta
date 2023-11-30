package mx.gob.imss.cit.pmc.confronta.enums;

import lombok.Getter;

@Getter
public enum MovementFieldsEnum {

    ORIGEN_ARCHIVO("cveOrigenArchivo"),
    ID("objectId"),
    ID_ARCHIVO("identificadorArchivo"),
    _ID("_id"),
    IS_PENDING("isPending");

    private final String desc;

    MovementFieldsEnum(String desc) {
        this.desc = desc;
    }

}
