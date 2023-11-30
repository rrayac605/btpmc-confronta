package mx.gob.imss.cit.pmc.confronta.enums;

import lombok.Getter;

@Getter
public enum BackupFieldsEnum {

    KEY("key"),
	CVE_EDO_REG("claveEstadoRiesgo"),
	CVE_CONSECUENCIA("claveConsecuencia"),
	ANIO_REVISION("anioRevision");

    private final String desc;

    BackupFieldsEnum(String desc) {
        this.desc = desc;
    }

}
