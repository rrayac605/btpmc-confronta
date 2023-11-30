package mx.gob.imss.cit.pmc.confronta.enums;

import lombok.Getter;

@Getter
public enum EmailTemplateEnum {

    PROCESS_SUCCESSFUL("CONFRONTA_PROCESS_SUCCESSFUL"),
    GET_INFO_FAILED("GET_INFO_FAILED"),
    FILE_GENERATION_FAILED("FILE_GENERATION_FAILED"),
    BACKUP_DATA_FAILED("BACKUP_DATA_FAILED"),
    UPLOAD_FILES_FAILED("UPLOAD_FILES_FAILED"),
    BANK_FILES_FAILED("BANK_FILES_FAILED");

    private final String desc;

    EmailTemplateEnum(String desc) {
        this.desc = desc;
    }
}
