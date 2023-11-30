package mx.gob.imss.cit.pmc.confronta.dto;

import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document("MCB_RESPALDO_CIERRE_ANUAL_CASUISTICA")
public class BackupConfrontaDTO {

    @Id
    private ObjectId objectId;
    
    private ObjectId objectIdOrigen;
    
    private String 	nss;
    
    private Integer OOADNss;
    
    private Integer subDelegacionNss;
    
    private Integer UMFNss;
    
    private String regPatronal;
    
    private String razonSocial;
    
    private Integer OOADRegistroPatroal;
    
    private Integer subDelRegistroPatronal;
    
    private Integer claveConsecuencia;
    
    private String descripcionConsecuencia;
    
    private Date fechaInicioAccidente;
    
    private Integer diasSubsidiados;
    
    private Date fechaFinTermino;
    
    private Integer tipoRiesgo;
    
    private Integer porcentajeIncapacidad;
    
    private String nombreAsegurado;
    
    private String CURPAsegurado;
    
    private Integer anioEjecucionRespaldo;
    
    private Integer claveEstadoRiesgo;
    
    private String descripcionEstadoRiesgo;
    
    private Integer claveSituacionRiesgo;
    
    private String descripcionSituacionRiesgo;
    
    private  String origen;
    
    private Integer anioRevision;
    
    private Integer anioCicloCaso;
    
    private Long key;    
    
    private Integer claveAccionRegistro;
    
    private String desAccionRegistro;
}
