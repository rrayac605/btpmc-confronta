package mx.gob.imss.cit.pmc.confronta.processor;

import mx.gob.imss.cit.pmc.confronta.constants.ConfrontaConstants;

import mx.gob.imss.cit.pmc.confronta.dto.BackupConfrontaDTO;
import mx.gob.imss.cit.pmc.confronta.dto.ChangeDTO;
import mx.gob.imss.cit.pmc.confronta.utils.DateUtils;
import mx.gob.imss.cit.pmc.confronta.utils.NumberUtils;
import mx.gob.imss.cit.pmc.confronta.utils.StringUtils;

import org.bson.types.ObjectId;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class BackupChangeProcessor implements ItemProcessor<ChangeDTO, BackupConfrontaDTO> {

    @Value("#{stepExecution}")
    private StepExecution stepExecution;

    @Override
    public BackupConfrontaDTO process(ChangeDTO changeDTO) throws Exception {
        boolean isReprocess = Boolean.TRUE.toString().equals(
                stepExecution.getJobParameters().getString(ConfrontaConstants.REPROCESS_FLAG));
        Long key = stepExecution.getJobParameters().getLong(ConfrontaConstants.KEY_PARAM);
        BackupConfrontaDTO backupConfrontaDTO = new BackupConfrontaDTO();          
        backupConfrontaDTO.setObjectIdOrigen(changeDTO.getObjectIdCambio());        
        backupConfrontaDTO.setNss(changeDTO.getNumNss());
        backupConfrontaDTO.setOOADNss(changeDTO.getCveDelegacionNss());
        backupConfrontaDTO.setSubDelegacionNss(changeDTO.getCveSubdelNss());
        backupConfrontaDTO.setUMFNss(changeDTO.getCveUmfAdscripcion());
        backupConfrontaDTO.setRegPatronal(changeDTO.getRefRegistroPatronal());
        backupConfrontaDTO.setOOADRegistroPatroal(changeDTO.getCveDelRegPatronal());
        backupConfrontaDTO.setSubDelRegistroPatronal(changeDTO.getCveSubDelRegPatronal());        
        backupConfrontaDTO.setClaveConsecuencia(NumberUtils.processConsequence(
                changeDTO.getCveConsecuencia(), changeDTO.getNumDiasSubsidiados()));
        backupConfrontaDTO.setFechaInicioAccidente(DateUtils.orElse(changeDTO.getFecInicio(), changeDTO.getFecAccidente()));        
        backupConfrontaDTO.setDiasSubsidiados(changeDTO.getNumDiasSubsidiados());
        backupConfrontaDTO.setFechaFinTermino(DateUtils.orElse(changeDTO.getFecFin(), changeDTO.getFecIniPension(), changeDTO.getFecAltaIncapacidad()));        
        backupConfrontaDTO.setTipoRiesgo(NumberUtils.safeValidateInteger(changeDTO.getCveTipoRiesgo()));
        backupConfrontaDTO.setPorcentajeIncapacidad(NumberUtils.safeValidateInteger(changeDTO.getPorcentajeIncapacidad()));
        backupConfrontaDTO.setNombreAsegurado(StringUtils.concatFullName(changeDTO.getNomAsegurado(),
                changeDTO.getRefPrimerApellido(), changeDTO.getRefSegundoApellido(), ConfrontaConstants.EMPTY_SPACE));
        backupConfrontaDTO.setCURPAsegurado(StringUtils.safeValidateCurp(changeDTO.getRefCurp()));
        backupConfrontaDTO.setAnioEjecucionRespaldo(Integer.parseInt(DateUtils.getCurrentYear()));
        backupConfrontaDTO.setClaveEstadoRiesgo(changeDTO.getCveEstadoRegistro());
        backupConfrontaDTO.setDescripcionEstadoRiesgo(changeDTO.getDesEstadoRegistro());
        backupConfrontaDTO.setClaveSituacionRiesgo(changeDTO.getCveSituacionRegistro());
        backupConfrontaDTO.setDescripcionSituacionRiesgo(changeDTO.getDesSituacionRegistro());
        backupConfrontaDTO.setOrigen(changeDTO.getOrigenAlta());
        backupConfrontaDTO.setDescripcionConsecuencia(changeDTO.getDesConsecuencia());
        backupConfrontaDTO.setAnioRevision(Integer.parseInt(DateUtils.getCurrentYear()) - 1);
        backupConfrontaDTO.setRazonSocial(changeDTO.getDesRazonSocial());
        try {
        	backupConfrontaDTO.setAnioCicloCaso(Integer.parseInt(changeDTO.getNumCicloAnual()));
        }
        catch (NumberFormatException e) {
        	 backupConfrontaDTO.setAnioCicloCaso(null);
        }
        backupConfrontaDTO.setKey(key);
        
        backupConfrontaDTO.setKey(key);
        return backupConfrontaDTO;
    }

}
