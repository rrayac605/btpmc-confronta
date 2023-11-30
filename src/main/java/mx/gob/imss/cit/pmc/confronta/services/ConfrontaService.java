package mx.gob.imss.cit.pmc.confronta.services;

import java.util.Map;
import java.util.Set;

public interface ConfrontaService {

    Map<String, Set<Integer>> getFirstJobFailedParams(boolean isReprocess);

    Set<Long> getFirstJobFailedKeyList();

    boolean processIsFinished();

	Map<String, Set<Integer>> obtenerCorrectos(boolean isReprocess);

	Set<Long> listaLlavesCorrectas();
	
	boolean processDescartadosIsFinished();

}
