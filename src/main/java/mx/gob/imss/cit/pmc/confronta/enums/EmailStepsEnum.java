package mx.gob.imss.cit.pmc.confronta.enums;

import lombok.Getter;

@Getter
public enum EmailStepsEnum {
	
	STEP_ONE("STEP_ONE_CONFRONTA"),
	STEP_TWO("STEP_TWO_CONFRONTA"),
	STEP_THREE("STEP_THREE_CONFRONTA"),
	STEP_FOUR("STEP_FOUR_CONFRONTA"),
	STEP_FIVE("STEP_FIVE_CONFRONTA");
	
	private final String desc;
	
	EmailStepsEnum(String desc) {
		this.desc = desc;
	}

}
