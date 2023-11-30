package mx.gob.imss.cit.pmc.confronta.enums;

import lombok.Getter;

@Getter
public enum ProcessAuditEnum {

    CORRECT("Correcto"),
    INCORRECT("Incorrecto"),
    ACTION("accion"),
    KEY("key"),
    ACTION_DATE("fechaAccion");

    private final String desc;

    ProcessAuditEnum(String desc) {
        this.desc = desc;
    }

}
