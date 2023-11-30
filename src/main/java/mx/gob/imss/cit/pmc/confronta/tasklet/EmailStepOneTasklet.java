package mx.gob.imss.cit.pmc.confronta.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mx.gob.imss.cit.pmc.confronta.constants.ConfrontaConstants;
import mx.gob.imss.cit.pmc.confronta.services.EmailService;

@Component
@StepScope
public class EmailStepOneTasklet implements Tasklet {
	
	@Autowired
	EmailService emailService;
	
	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunk) {
		try {
			String nombreTemplate = ConfrontaConstants.STEP_ONE_TEMPLATE;
			emailService.sendEmailStepOne(nombreTemplate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return RepeatStatus.FINISHED;
	}

}
