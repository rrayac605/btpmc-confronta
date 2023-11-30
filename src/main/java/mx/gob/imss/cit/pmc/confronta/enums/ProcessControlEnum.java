package mx.gob.imss.cit.pmc.confronta.enums;

import lombok.Getter;

@Getter
public enum ProcessControlEnum {

    CORRECT("Correcto"),
    FAILED("Erróneo"),
    ACTION("accion"),
    CONTROL("control"),
    DATE("fecha"),
    KEY("key");

    private final String desc;

    ProcessControlEnum(String desc) {
        this.desc = desc;
    }

}
