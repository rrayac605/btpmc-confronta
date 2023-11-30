package mx.gob.imss.cit.pmc.confronta.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "MCT_AUDITORIA")
public class AuditDTO {

    @Getter
    @Setter
    private String nomUsuario;

    @Getter
    @Setter
    private String numFolioMovOriginal;

    @Getter
    @Setter
    private String desObservacionesAprobador;

    @Getter
    @Setter
    private String desObservacionesSol;

    @Getter
    @Setter
    private Integer cveIdAccionRegistro;

    @Getter
    @Setter
    private String desAccionRegistro;

    @Getter
    @Setter
    private String desCambio;

    @Getter
    @Setter
    private String accion;

    @Getter
    @Setter
    private String desSituacionRegistro;

    @Getter
    @Setter
    private Integer cveSituacionRegistro;

}
