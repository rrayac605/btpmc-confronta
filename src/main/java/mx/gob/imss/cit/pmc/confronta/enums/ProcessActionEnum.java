package mx.gob.imss.cit.pmc.confronta.enums;

import lombok.Getter;

@Getter
public enum ProcessActionEnum {

    GET_INFO("Obtener información"),
    BACKUP_INFO("Respaldar información"),
    CONFRONTA_PROCESS("Proceso de cierre anual casuística"),
    FILE_GENERATION("Generación de archivo"),
    GENERATE_FILES("Generar archivos"),
    FILE_STORAGE("Almacenar archivo"),
    FILES_STORAGE("Almacenar archivos"),
    BANK_FILE("Depositar archivo"),
    BANK_FILES("Depositar archivos"),
    READ_INFO_DESCARTADOS("Respaldar información"),
	BACKUP_INFO_DESCARTADOS("Obtener información descartada");
	
    private final String desc;

    ProcessActionEnum(String desc) {
        this.desc = desc;
    }

}
