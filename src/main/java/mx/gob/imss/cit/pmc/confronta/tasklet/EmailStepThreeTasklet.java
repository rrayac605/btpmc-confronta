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
public class EmailStepThreeTasklet implements Tasklet {
	
	@Autowired
	private EmailService emailService;
	
	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunk) throws Exception {
		try {
			String nombreTemplate = ConfrontaConstants.STEP_THREE_TEMPLATE;
			emailService.sendEmailStepThree(nombreTemplate);
		} catch(Exception exc) {
			exc.printStackTrace();
		}
		return RepeatStatus.FINISHED;
	}
}
