package mx.gob.imss.cit.pmc.confronta.services.impl;

import mx.gob.imss.cit.pmc.confronta.constants.ConfrontaConstants;
import mx.gob.imss.cit.pmc.confronta.dto.ProcessControlDTO;
import mx.gob.imss.cit.pmc.confronta.enums.ProcessActionEnum;
import mx.gob.imss.cit.pmc.confronta.repository.FileControlRepository;
import mx.gob.imss.cit.pmc.confronta.repository.ProcessControlRepository;
import mx.gob.imss.cit.pmc.confronta.services.ConfrontaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ConfrontaServiceImpl implements ConfrontaService {

    @Autowired
    private ProcessControlRepository processControlRepository;

    @Autowired
    private FileControlRepository fileControlRepository;

    @Override
    public Map<String, Set<Integer>> getFirstJobFailedParams(boolean isReprocess) {
        List<ProcessControlDTO> processControlList;
        processControlList = getFirstJobFailedProcessList();
        return listToMap(processControlList);
    }
    
    @Override
    public Map<String, Set<Integer>> obtenerCorrectos(boolean isReprocess) {
    	List<ProcessControlDTO> controlProcesoList;
    	controlProcesoList = obtenerCorrectosList();
    	return listToMap(controlProcesoList);
    }

    private Map<String, Set<Integer>> listToMap(List<ProcessControlDTO> processControlList) {
        return processControlList.stream().collect(Collectors.toMap(
                processControl -> ConfrontaConstants.ACTION_TO_TEMPLATE_NAME.get(processControl.getAccion()),
                processControl -> Collections.singleton(processControl.getKey().intValue()),
                (existing, replacement) -> {
                    Set<Integer> mergedSet = new HashSet<>();
                    mergedSet.addAll(existing);
                    mergedSet.addAll(replacement);
                    return mergedSet;
                }
        ));
    }

    @Override
    public Set<Long> getFirstJobFailedKeyList() {
        List<ProcessControlDTO> processControlList = getFirstJobFailedProcessList();
        return processControlList.stream().map(ProcessControlDTO::getKey).collect(Collectors.toSet());
    }
    
    @Override
    public Set<Long> listaLlavesCorrectas() {
    	List<ProcessControlDTO> controlProcesoList = obtenerCorrectosList();
    	return controlProcesoList.stream().map(ProcessControlDTO::getKey).collect(Collectors.toSet());
    }

    public List<ProcessControlDTO> getFirstJobFailedProcessList() {
        List<ProcessControlDTO> processControlList = processControlRepository.findAllError(Arrays.asList(
                ProcessActionEnum.GET_INFO.getDesc(),
                ProcessActionEnum.BACKUP_INFO.getDesc()));
        List<ProcessControlDTO> fileControlList = fileControlRepository.findAllError(
                Collections.singletonList(ProcessActionEnum.FILE_GENERATION.getDesc()))
                .stream().map(ProcessControlDTO::new).collect(Collectors.toList());
        return Stream.concat(processControlList.stream(), fileControlList.stream()).collect(Collectors.toList());
    }
    
    public List<ProcessControlDTO> obtenerCorrectosList() {
    	List<ProcessControlDTO> archivosCorrectosList = fileControlRepository.encuentraCorrectos(
    			Collections.singletonList(ProcessActionEnum.FILE_GENERATION.getDesc()))
    			.stream().map(ProcessControlDTO::new).collect(Collectors.toList());
    	return archivosCorrectosList;
    }

    @Override
    public boolean processIsFinished() {
        return processControlRepository.validateAction(ProcessActionEnum.CONFRONTA_PROCESS.getDesc());
    }
    
    @Override
    public boolean processDescartadosIsFinished() {
        return processControlRepository.validateAction(ProcessActionEnum.BACKUP_INFO_DESCARTADOS.getDesc());
    }
}
