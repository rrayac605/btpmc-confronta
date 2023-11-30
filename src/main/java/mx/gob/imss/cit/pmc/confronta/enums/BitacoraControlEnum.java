package mx.gob.imss.cit.pmc.confronta.enums;

import lombok.Getter;

@Getter
public enum BitacoraControlEnum {
	
	CORRECT("Correcto"),
	DEL("del"),
	SUBDEL("subDel"),
	ACTION("accion"),
	FECHA("fecEjecucion"),
	CONTROL("control");
	
	private final String desc;
	
	BitacoraControlEnum(String desc) {
		this.desc = desc;
	}

}
