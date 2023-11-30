package mx.gob.imss.cit.pmc.confronta.enums;

import lombok.Getter;

@Getter
public enum CamposPatronEnum {

    REGISTRO_PATRONAL("patronDTO.refRegistroPatronal"),
    CVE_DEL_PATRON("patronDTO.cveDelRegPatronal"),
    CVE_SUBDEL_PATRON("patronDTO.cveSubDelRegPatronal");

    private final String desc;

    CamposPatronEnum(String desc) {
        this.desc = desc;
    }

}
