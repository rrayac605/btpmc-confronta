package mx.gob.imss.cit.pmc.confronta.enums;

import lombok.Getter;

@Getter
public enum FileControlEnum {

    CORRECT("Correcto"),
    FAILED("Erróneo"),
    ACTION("accion"),
    CONTROL("control"),
    CREATION_DATE("fecCreacion"),
    KEY("key");

    private final String desc;

    FileControlEnum(String desc) {
        this.desc = desc;
    }

}
