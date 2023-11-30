package mx.gob.imss.cit.pmc.confronta.enums;

import lombok.Getter;

@Getter
public enum AuditActionEnum {

    GET_INFO("Obtener información"),
    BACKUP_INFO("Respaldar información cierre anual casuística"),
    FILES_STORAGE("Almacenar archivos cierre anual casuística"),
    BANK_FILES("Depositar archivos cierre anual casuística"),
    GENERATE_FILES("Generar archivos cierre anual casuística");

    private String desc;

    AuditActionEnum(String desc) {
        this.desc = desc;
    }

}
