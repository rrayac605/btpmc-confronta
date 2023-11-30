package mx.gob.imss.cit.pmc.confronta.processor;

import mx.gob.imss.cit.pmc.confronta.constants.ConfrontaConstants;
import mx.gob.imss.cit.pmc.confronta.dto.ChangeDTO;
import mx.gob.imss.cit.pmc.confronta.utils.DateUtils;
import mx.gob.imss.cit.pmc.confronta.utils.NumberUtils;
import mx.gob.imss.cit.pmc.confronta.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class ConfrontaChangeProcessor implements ItemProcessor<ChangeDTO, String> {

    private static final Logger logger = LoggerFactory.getLogger(ConfrontaMovementProcessor.class);

    @Override
    public String process(ChangeDTO changeDTO) {
        return StringUtils.safeSubString(changeDTO.getNumNss(), ConfrontaConstants.TEN)
                .concat(StringUtils.safeSubString(changeDTO.getRefRegistroPatronal(), ConfrontaConstants.TEN))
                .concat(NumberUtils.processConsequence(
                        changeDTO.getCveConsecuencia(), changeDTO.getNumDiasSubsidiados()).toString()
                )
                .concat(DateUtils.getFileFormattedDateChanges( 
                		DateUtils.orElse(changeDTO.getFecInicio(), changeDTO.getFecAccidente())
                ))
                .concat(StringUtils.safeAddZero(changeDTO.getNumDiasSubsidiados(), 4))
                .concat(DateUtils.getFileFormattedDateChanges(
                		DateUtils.orElse(changeDTO.getFecFin(), changeDTO.getFecIniPension(), changeDTO.getFecAltaIncapacidad())
                ))
                .concat(NumberUtils.safeValidateInteger(changeDTO.getCveTipoRiesgo()).toString())
                .concat(StringUtils.safeAddZero(changeDTO.getPorcentajeIncapacidad(), 3))
                .concat(StringUtils.concatFullNameFile(changeDTO.getNomAsegurado(), changeDTO.getRefPrimerApellido(), changeDTO.getRefSegundoApellido()))
                .concat("0000000")
                .concat(StringUtils.safeValidateCurp(changeDTO.getRefCurp()))
                .concat("0000000000000000000000");
    }

}
