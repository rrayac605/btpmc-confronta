package mx.gob.imss.cit.pmc.confronta.tasklet;

import com.jcraft.jsch.JSchException;
import mx.gob.imss.cit.pmc.confronta.constants.ConfrontaConstants;
import mx.gob.imss.cit.pmc.confronta.dto.Sftp1Properties;
import mx.gob.imss.cit.pmc.confronta.dto.Sftp2Properties;
import mx.gob.imss.cit.pmc.confronta.enums.AuditActionEnum;
import mx.gob.imss.cit.pmc.confronta.enums.EmailStepsEnum;
import mx.gob.imss.cit.pmc.confronta.enums.EmailTemplateEnum;
import mx.gob.imss.cit.pmc.confronta.enums.ProcessActionEnum;
import mx.gob.imss.cit.pmc.confronta.repository.FileControlRepository;
import mx.gob.imss.cit.pmc.confronta.repository.ProcessAuditRepository;
import mx.gob.imss.cit.pmc.confronta.services.EmailService;
import mx.gob.imss.cit.pmc.confronta.sftp.SftpClient;
import mx.gob.imss.cit.pmc.confronta.sftp.SftpConfronta;
import mx.gob.imss.cit.pmc.confronta.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@StepScope
public class SftpUploadFileTasklet implements Tasklet{

	private static final Logger logger = LoggerFactory.getLogger(SftpUploadFileTasklet.class);
	
	@Autowired
	private Sftp1Properties sftp1Properties;

	@Autowired
	private Sftp2Properties sftp2Properties;

	@Autowired
	private FileControlRepository fileControlRepository;

	@Autowired
	private ProcessAuditRepository processAuditRepository;

	@Autowired
	private EmailService emailService;
		
	@Value("#{stepExecution}")
	private StepExecution stepExecution;
	
	private static int conteoReprocesos = 0;
	
	private static int cuentaReprocesos = 0;
	
	@Override
	public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception{
		boolean isReprocess = Boolean.TRUE.toString().equals(
				stepExecution.getJobParameters().getString(ConfrontaConstants.REPROCESS_FLAG));
		String fromStepName = stepExecution.getJobExecution().getExecutionContext().getString(ConfrontaConstants.PAST_STEP_PARAM);
		boolean isSftp1 = fromStepName.equals(ConfrontaConstants.FILE_VALIDATION_STEP);
		String action = isSftp1 ? ProcessActionEnum.FILE_STORAGE.getDesc() : ProcessActionEnum.BANK_FILE.getDesc();
		String auditAction = isSftp1 ? AuditActionEnum.FILES_STORAGE.getDesc() : AuditActionEnum.BANK_FILES.getDesc();
		String templateName = isSftp1 ? EmailTemplateEnum.UPLOAD_FILES_FAILED.getDesc() : EmailTemplateEnum.BANK_FILES_FAILED.getDesc();
		String steps = isSftp1 ? ConfrontaConstants.STEP_FOUR_TEMPLATE : ConfrontaConstants.STEP_FIVE_TEMPLATE;
		Map<Long, String> sftpPathMap = isSftp1 ?
				ConfrontaConstants.PATH_FTP1:
				ConfrontaConstants.PATH_FTP2;
		String basePath = isSftp1 ? sftp1Properties.getPath() : sftp2Properties.getPath();
		SftpClient sftpClient;
		Set<Long> keyList = fileControlRepository.findKeyListOfGeneratedFiles();
		Set<Long> llaves = fileControlRepository.findKeyListOfGeneratedFiles();
		Set<Long> actionKeyList = isSftp1 ? fileControlRepository.findKeyListOfStoredFiles() : fileControlRepository.findKeyListOfBankedFiles();
		if (actionKeyList.size() > 0) {
			keyList = keyList.stream().filter(key -> !actionKeyList.contains(key)).collect(Collectors.toSet());
		}
		Set<Long> llavesCorrectas = new HashSet<>();
		Set<Long> exceptionKeyList = new HashSet<>();
		try {
			sftpClient = isSftp1 ?
					SftpClient.getSftpClient(
							sftp1Properties.getHost(),
							sftp1Properties.getPort(),
							sftp1Properties.getUser(),
							sftp1Properties.getPass()
					) :
					SftpClient.getSftpClient(
							sftp2Properties.getHost(),
							sftp2Properties.getPort(),
							sftp2Properties.getUser(),
							sftp2Properties.getPass()
					);
		} catch (JSchException e) {
			for (Long key : keyList) {
				fileControlRepository.createError(action, key);
			}
			processAuditRepository.createIncorrect(auditAction, null);
			logger.error("Error de coneccion con el sftp", e);
			emailService.sendEmailToOOADList(templateName, keyList, llaves);
			return RepeatStatus.FINISHED;
		}		
		for (Long key : keyList) {
			try {
				//Agregar excepcion para EA04 y EA05
				String fullBasePath = StringUtils.buildFilePath(key, basePath, sftpPathMap);
				SftpConfronta.createBaseFolder(fullBasePath);
				logger.info("Ruta donde se depositara el archivo {}", fullBasePath);
				logger.info("Nombre del archivo {} para la key {}", StringUtils.nombreArchivo(key), key);
				sftpClient.uploadFile(StringUtils.buildFileName(key, ConfrontaConstants.BASE_PATH_SERVER, ConfrontaConstants.PATH_FTP1),
						fullBasePath.concat(StringUtils.nombreArchivo(key)));
				llavesCorrectas.add(key);
				fileControlRepository.createCorrect(action, key);
			} catch (Exception e) {
				exceptionKeyList.add(key);
				fileControlRepository.createError(action, key);
				logger.error("Error al subir el archivo para la key {}", key);
			}
		};
		if (exceptionKeyList.size() > 0) {
			processAuditRepository.createIncorrect(auditAction, null);
			emailService.sendEmailToOOADList(templateName, exceptionKeyList, llavesCorrectas);
		} else {
			emailService.sendEmailStepFourFive(steps);
		}
		sftpClient.disconnect();		
		return RepeatStatus.FINISHED;
	}

}
