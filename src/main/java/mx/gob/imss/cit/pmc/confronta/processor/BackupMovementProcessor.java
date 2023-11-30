package mx.gob.imss.cit.pmc.confronta.processor;

import lombok.SneakyThrows;
import mx.gob.imss.cit.pmc.confronta.constants.ConfrontaConstants;
import mx.gob.imss.cit.pmc.confronta.dto.*;
import mx.gob.imss.cit.pmc.confronta.utils.DateUtils;
import mx.gob.imss.cit.pmc.confronta.utils.NumberUtils;
import mx.gob.imss.cit.pmc.confronta.utils.StringUtils;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class BackupMovementProcessor implements ItemProcessor<MovementDTO, BackupConfrontaDTO> {
	
	private static final Logger logger = LoggerFactory.getLogger(BackupMovementProcessor.class);

    @Value("#{stepExecution}")
    private StepExecution stepExecution;
    
    private static int conteoReprocesos = 0;

    @SneakyThrows
    @Override
    public BackupConfrontaDTO process(MovementDTO movementDTO) {
        boolean isReprocess = Boolean.TRUE.toString().equals(
                stepExecution.getJobParameters().getString(ConfrontaConstants.REPROCESS_FLAG));
        Long key = stepExecution.getJobParameters().getLong(ConfrontaConstants.KEY_PARAM);
        //Agregar exception para respaldar información
        BackupConfrontaDTO backupConfrontaDTO = new BackupConfrontaDTO();
        AseguradoDTO asegurado = movementDTO.getAseguradoDTO();
        PatronDTO patron = movementDTO.getPatronDTO();
        IncapacidadDTO incapacidad = movementDTO.getIncapacidadDTO();
        backupConfrontaDTO.setObjectIdOrigen(movementDTO.getObjectId());   
        backupConfrontaDTO.setNss(asegurado.getNumNss());
        backupConfrontaDTO.setOOADNss(asegurado.getCveDelegacionNss());
        backupConfrontaDTO.setSubDelegacionNss(asegurado.getCveSubdelNss());
        backupConfrontaDTO.setUMFNss(asegurado.getCveUmfAdscripcion());
        backupConfrontaDTO.setRegPatronal(patron.getRefRegistroPatronal());
        backupConfrontaDTO.setOOADRegistroPatroal(patron.getCveDelRegPatronal());
        backupConfrontaDTO.setSubDelRegistroPatronal(patron.getCveSubDelRegPatronal());        
        backupConfrontaDTO.setClaveConsecuencia(NumberUtils.processConsequence(
                incapacidad.getCveConsecuencia(), incapacidad.getNumDiasSubsidiados()));        
        backupConfrontaDTO.setFechaInicioAccidente(DateUtils.orElse(incapacidad.getFecInicio(), incapacidad.getFecAccidente()));        
        backupConfrontaDTO.setDiasSubsidiados(incapacidad.getNumDiasSubsidiados());
        backupConfrontaDTO.setFechaFinTermino(DateUtils.orElse(incapacidad.getFecFin(), incapacidad.getFecIniPension(), incapacidad.getFecAltaIncapacidad()));        
        backupConfrontaDTO.setTipoRiesgo(NumberUtils.safeValidateInteger(incapacidad.getCveTipoRiesgo()));
        backupConfrontaDTO.setPorcentajeIncapacidad(NumberUtils.safetyParseBigDecimal(incapacidad.getPorPorcentajeIncapacidad()));
        backupConfrontaDTO.setNombreAsegurado(StringUtils.concatFullName(asegurado.getNomAsegurado(),
                asegurado.getRefPrimerApellido(), asegurado.getRefSegundoApellido(), ConfrontaConstants.EMPTY_SPACE));
        backupConfrontaDTO.setCURPAsegurado(StringUtils.safeValidateCurp(asegurado.getRefCurp()));
        backupConfrontaDTO.setAnioEjecucionRespaldo(Integer.parseInt(DateUtils.getCurrentYear()));
        backupConfrontaDTO.setClaveEstadoRiesgo(asegurado.getCveEstadoRegistro());
        backupConfrontaDTO.setDescripcionEstadoRiesgo(asegurado.getDesEstadoRegistro());
        backupConfrontaDTO.setDescripcionConsecuencia(incapacidad.getDesConsecuencia());
        backupConfrontaDTO.setOrigen(movementDTO.getCveOrigenArchivo());
        backupConfrontaDTO.setAnioRevision(Integer.parseInt(DateUtils.getCurrentYear()) - 1);
        backupConfrontaDTO.setRazonSocial(movementDTO.getPatronDTO().getDesRazonSocial());
		try {
			backupConfrontaDTO.setAnioCicloCaso(Integer.parseInt(asegurado.getNumCicloAnual()));
		} catch (NumberFormatException e) {
			backupConfrontaDTO.setAnioCicloCaso(null);
		}
		if (movementDTO.isPending()) {
			backupConfrontaDTO.setClaveSituacionRiesgo(2);
			backupConfrontaDTO.setDescripcionSituacionRiesgo("Pendiente de aprobar");
		} else {
			if (movementDTO.getAuditorias() != null && movementDTO.getAuditorias().size() > 0) {
				backupConfrontaDTO.setClaveSituacionRiesgo(movementDTO.getAuditorias()
						.get(movementDTO.getAuditorias().size() - 1).getCveSituacionRegistro());
				backupConfrontaDTO.setDescripcionSituacionRiesgo(movementDTO.getAuditorias()
						.get(movementDTO.getAuditorias().size() - 1).getDesSituacionRegistro());
			}
		}
        backupConfrontaDTO.setKey(key);
        return backupConfrontaDTO;
    }

}
