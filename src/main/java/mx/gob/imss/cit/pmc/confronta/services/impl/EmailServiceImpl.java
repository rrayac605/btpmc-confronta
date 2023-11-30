package mx.gob.imss.cit.pmc.confronta.services.impl;

import mx.gob.imss.cit.pmc.confronta.constants.ConfrontaConstants;
import mx.gob.imss.cit.pmc.confronta.dto.BackupConfrontaDTO;
import mx.gob.imss.cit.pmc.confronta.dto.DiasSubsidiadosDTO;
import mx.gob.imss.cit.pmc.confronta.dto.EmailTemplateDTO;
import mx.gob.imss.cit.pmc.confronta.dto.PorcentajeIncapacidadDTO;
import mx.gob.imss.cit.pmc.confronta.repository.CountRepository;
import mx.gob.imss.cit.pmc.confronta.repository.EmailTemplateRepository;
import mx.gob.imss.cit.pmc.confronta.services.EmailService;
import mx.gob.imss.cit.pmc.confronta.utils.DateUtils;
import mx.gob.imss.cit.pmc.confronta.utils.ReaderUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Autowired
    private EmailTemplateRepository emailTemplateRepository;

    @Autowired
    private JavaMailSender javaMailSender;
    
    @Autowired
    private CountRepository countRepository;
    
    @Autowired
    private MongoOperations mongoOperations;

    @Override
    public void sendEmail(String templateName, Set<Long> keyList) {
        try {
            EmailTemplateDTO emailTemplate = emailTemplateRepository.findByName(templateName);
            logger.info("Template a enviar -> {}", templateName);
            llenaExito(emailTemplate, keyList);
            MimeMessageHelper mimeMessageHelper = construyeCorreoExito(emailTemplate);
            javaMailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (MessagingException | MailException e) {
            logger.error("Error al enviar el email", e);
        }
    }
    
    @Override
    public void sendEmailGetBackupInfoFailedList(String templateName, Set<Long> keyList) {
        try {
            EmailTemplateDTO emailTemplate = emailTemplateRepository.findByName(templateName);
            logger.info("Template a enviar -> {}", templateName);
            if(templateName.equals(ConfrontaConstants.GET_INFO_FAILED_TEMPLATE)) {
            	llenaGetInfoFailed(emailTemplate);
            }else {
            	llenaBackupDataFailed(emailTemplate);
            }
            MimeMessageHelper mimeMessageHelper = buildEmailMessageHelper(emailTemplate);
            javaMailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (MessagingException | MailException e) {
            logger.error("Error al enviar el email", e);
        }
    }

    @Override
    public void sendEmailToOOADList(String templateName, Set<Long> keyList, Set<Long> llaves) {
        try {
            EmailTemplateDTO emailTemplate = emailTemplateRepository.findByName(templateName);
            logger.info("Template a enviar -> {}", templateName);
            if(templateName.equals(ConfrontaConstants.GET_INFO_FAILED_TEMPLATE)) {
            	llenaGetInfoFailed(emailTemplate);
            }else if(templateName.equals(ConfrontaConstants.BACKUP_DATA_FAILED_TEMPLATE)) {
            	llenaBackupDataFailed(emailTemplate);
            }else if(templateName.equals(ConfrontaConstants.FILE_GENERATION_FAILED_TEMPLATE)) {
            	llenaFileGenerationFailed(emailTemplate, keyList, llaves);
            }else if(templateName.equals(ConfrontaConstants.UPLOAD_FILES_FAILED_TEMPLATE)) {
            	llenaUploadFilesFailed(emailTemplate, keyList, llaves);
            }else {
            	llenaBankFilesFailed(emailTemplate, keyList, llaves);
            }
            MimeMessageHelper mimeMessageHelper = buildEmailMessageHelper(emailTemplate);
            javaMailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (MessagingException | MailException e) {
            logger.error("Error al enviar el email", e);
        }
    }
    
    @Override
    public void sendEmailStepOne(String templateName) {
    	try {
    		EmailTemplateDTO emailTemplate = emailTemplateRepository.findByName(templateName);
    		logger.info("Voy a enviar correo paso 1");
    		llenaStepOne(emailTemplate);
    		MimeMessageHelper mimeMessageHelper = buildEmailMessageHelper(emailTemplate);
    		javaMailSender.send(mimeMessageHelper.getMimeMessage());
    	} catch(MessagingException | MailException e) {
    		logger.error("Error al enviar email", e);
    	}
    }
    
    @Override
    public void sendEmailStepTwo(String templateName) {
    	try {
    		EmailTemplateDTO emailTemplate = emailTemplateRepository.findByName(templateName);
    		logger.info("Voy a enviar correo paso 2");
    		llenaStepsTwoThreeFourFive(emailTemplate);
    		MimeMessageHelper mimeMessageHelper = buildEmailMessageHelper(emailTemplate);
    		javaMailSender.send(mimeMessageHelper.getMimeMessage());
    	} catch(MessagingException | MailException e) {
    		logger.error("Error al enviar email", e);
    	}
    }
    
    @Override
    public void sendEmailStepThree(String templateName) {
    	try {
    		EmailTemplateDTO emailTemplate = emailTemplateRepository.findByName(templateName);
    		logger.info("Voy a enviar correo paso 3");
    		llenaStepsTwoThreeFourFive(emailTemplate);
    		MimeMessageHelper mimeMessageHelper = buildEmailMessageHelper(emailTemplate);
    		javaMailSender.send(mimeMessageHelper.getMimeMessage());
    	} catch(MessagingException | MailException e) {
    		logger.error("Error al enviar email", e);
    	}
    }
    
    @Override
    public void sendEmailStepFourFive(String templateName) {
    	try {
    		EmailTemplateDTO emailTemplate = emailTemplateRepository.findByName(templateName);
    		logger.info("Voy a enviar correo paso 4-5");
    		llenaStepsTwoThreeFourFive(emailTemplate);
    		MimeMessageHelper mimeMessageHelper = buildEmailMessageHelper(emailTemplate);
    		javaMailSender.send(mimeMessageHelper.getMimeMessage());
    	} catch(MessagingException | MailException e) {
    		logger.error("Error al enviar email", e);
    	}
    }
    
    private MimeMessageHelper buildEmailMessageHelper(EmailTemplateDTO emailTemplate) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, Boolean.TRUE, ConfrontaConstants.ENCODING);
        mimeMessageHelper.setSubject(emailTemplate.getSubject());
        mimeMessageHelper.setFrom(emailTemplate.getFrom());
        mimeMessageHelper.setTo(emailTemplate.getTo());
        mimeMessageHelper.setText(emailTemplate.getTemplate(), Boolean.TRUE);
        mimeMessageHelper.addInline(ConfrontaConstants.HEADER_IMG,
                new ClassPathResource(ConfrontaConstants.IMAGE_PATH.concat(ConfrontaConstants.HEADER_IMG)));
        mimeMessageHelper.addInline(ConfrontaConstants.FOOTER_IMG,
                new ClassPathResource(ConfrontaConstants.IMAGE_PATH.concat(ConfrontaConstants.FOOTER_IMG)));
        return mimeMessageHelper;
    }
    
    private MimeMessageHelper construyeCorreoExito(EmailTemplateDTO emailTemplate) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, Boolean.TRUE, ConfrontaConstants.ENCODING);
        mimeMessageHelper.setSubject(emailTemplate.getSubject());
        mimeMessageHelper.setFrom(emailTemplate.getFrom());
        mimeMessageHelper.setTo(emailTemplate.getTo());
        mimeMessageHelper.setText(emailTemplate.getTemplate(), Boolean.TRUE);
        mimeMessageHelper.addInline(ConfrontaConstants.HEADER_IMG_ESP,
                new ClassPathResource(ConfrontaConstants.IMAGE_PATH.concat(ConfrontaConstants.HEADER_IMG_ESP)));
        mimeMessageHelper.addInline(ConfrontaConstants.FOOTER_IMG_ESP,
                new ClassPathResource(ConfrontaConstants.IMAGE_PATH.concat(ConfrontaConstants.FOOTER_IMG_ESP)));
        return mimeMessageHelper;
    }
    
    private void llenaExito(EmailTemplateDTO emailTemplateDTO, Set<Long> keyList) {
    	logger.info("Entre al exito");
    	
    	DecimalFormat df = new DecimalFormat("###,###");
    	
    	TypedAggregation<BackupConfrontaDTO> registrosEnviados = ReaderUtils.contarRegsEnviados();
    	
    	String diaEjec = DateUtils.obtenerDiaEjecucion(new Date());
    	String diaFin = DateUtils.obtenerDiaFin();
    	String horaFin = DateUtils.obtenerHoraFin();
    	String anioRev = String.valueOf(Integer.parseInt(DateUtils.getCurrentYear()) - 1);
    	String ooads = "";
    	String totales = "";
    	
    	long totalRegsEnviados = countRepository.count(registrosEnviados);
    	long totalCorrectos = 0L;
    	long totalCorrectosOtras = 0L;
    	long totalSusceptibles = 0L;
    	long totalSusceptiblesOtras = 0L;
    	long totalDefunsiones = 0L;
    	long totalPorcentajes = 0L;
    	long totalDias = 0L;
    	
    	for(Long key : keyList) {
    		
    		TypedAggregation<BackupConfrontaDTO> cuentaCorrectos = ReaderUtils.contarCorrectos(key);
    		TypedAggregation<BackupConfrontaDTO> cuentaCorrectosOtras = ReaderUtils.contarCorrectosOtras(key);
    		TypedAggregation<BackupConfrontaDTO> cuentaSusceptibles = ReaderUtils.contarSusceptibles(key);
    		TypedAggregation<BackupConfrontaDTO> cuentaSusceptiblesOtras = ReaderUtils.contarSusceptiblesOtras(key);
    		TypedAggregation<BackupConfrontaDTO> cuentaDefunciones = ReaderUtils.contarDefunciones(key);
    		TypedAggregation<BackupConfrontaDTO> sumarPorcentajes = ReaderUtils.sumarPorcentajes(key);
    		TypedAggregation<BackupConfrontaDTO> sumarDias = ReaderUtils.sumarDias(key);
    		
    		AggregationResults<PorcentajeIncapacidadDTO> aggregationPorcentajes = mongoOperations.aggregate(sumarPorcentajes, PorcentajeIncapacidadDTO.class);
    		AggregationResults<DiasSubsidiadosDTO> aggregationDias = mongoOperations.aggregate(sumarDias, DiasSubsidiadosDTO.class);
    		
    		List<PorcentajeIncapacidadDTO> listaPorcentajes = aggregationPorcentajes.getMappedResults();
    		List<DiasSubsidiadosDTO> listaDias = aggregationDias.getMappedResults();
    		
    		long correctos = countRepository.count(cuentaCorrectos);
    		long correctosOtras = countRepository.count(cuentaCorrectosOtras);
    		long susceptibles = countRepository.count(cuentaSusceptibles);
    		long susceptiblesOtras = countRepository.count(cuentaSusceptiblesOtras);
    		long defunciones = countRepository.count(cuentaDefunciones);
    		long porcentajes = listaPorcentajes.get(0).getPorcentajeIncapacidad();
    		long dias = listaDias.get(0).getDiasSubsidiados();
    		
    		   			
    		ooads = ooads.concat("<tr id='trOOAD'>");
    		ooads = ooads.concat("<td style='border:1px solid black'>").concat(ConfrontaConstants.DEL_DESCRIPTION_MAP.get(key)).concat("</td>");
    		ooads = ooads.concat("<td style='border:1px solid black'>").concat(String.valueOf(df.format(correctos))).concat("</td>");
    		ooads = ooads.concat("<td style='border:1px solid black'>").concat(String.valueOf(df.format(correctosOtras))).concat("</td>");
    		ooads = ooads.concat("<td style='border:1px solid black'>").concat(String.valueOf(df.format(susceptibles))).concat("</td>");
    		ooads = ooads.concat("<td style='border:1px solid black'>").concat(String.valueOf(df.format(susceptiblesOtras))).concat("</td>");
    		ooads = ooads.concat("<td style='border:1px solid black'>").concat(String.valueOf(df.format(defunciones))).concat("</td>");
    		ooads = ooads.concat("<td style='border:1px solid black'>").concat(String.valueOf(df.format(porcentajes))).concat("</td>");
    		ooads = ooads.concat("<td style='border:1px solid black'>").concat(String.valueOf(df.format(dias))).concat("</td>");
    		ooads = ooads.concat("</tr>");
    		
    		totalCorrectos+=correctos;
    		totalCorrectosOtras+=correctosOtras;
    		totalSusceptibles+=susceptibles;
    		totalSusceptiblesOtras+=susceptiblesOtras;
    		totalDefunsiones+=defunciones;
    		totalPorcentajes+=porcentajes;
    		totalDias+=dias;
    		
    	}
   				
		totales = totales.concat("<td style='border:1px solid black'>").concat("TOTALES").concat("</td>");
		totales = totales.concat("<td style='border:1px solid black'>").concat(String.valueOf(df.format(totalCorrectos))).concat("</td>");
		totales = totales.concat("<td style='border:1px solid black'>").concat(String.valueOf(df.format(totalCorrectosOtras))).concat("</td>");
		totales = totales.concat("<td style='border:1px solid black'>").concat(String.valueOf(df.format(totalSusceptibles))).concat("</td>");
		totales = totales.concat("<td style='border:1px solid black'>").concat(String.valueOf(df.format(totalSusceptiblesOtras))).concat("</td>");
		totales = totales.concat("<td style='border:1px solid black'>").concat(String.valueOf(df.format(totalDefunsiones))).concat("</td>");
		totales = totales.concat("<td style='border:1px solid black'>").concat(String.valueOf(df.format(totalPorcentajes))).concat("</td>");
		totales = totales.concat("<td style='border:1px solid black'>").concat(String.valueOf(df.format(totalDias))).concat("</td>");
    	
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(ConfrontaConstants.DIA_EJECUCION, diaEjec));
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(ConfrontaConstants.DIA_FIN, diaFin));
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(ConfrontaConstants.HORA_FIN, horaFin));
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(ConfrontaConstants.ANIO_REVISION, anioRev));
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(ConfrontaConstants.TOTAL_REGS, String.valueOf(df.format(totalRegsEnviados))));
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(ConfrontaConstants.LISTA_OOAD, ooads));
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(ConfrontaConstants.TOTALES_CONFRONTA, totales));
    	
    }
    
    private void llenaGetInfoFailed(EmailTemplateDTO emailTemplateDTO) {
    	logger.info("Entre a get info failed");
    	
    	String diaEjecucion = DateUtils.obtenerDiaEjecucion(new Date());
    	String horaEjecucion = DateUtils.obtenerHoraFin();
    	String diaSiguiente = DateUtils.obtenerDiaSig(new Date());
    	
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(ConfrontaConstants.DIA_EJECUCION, diaEjecucion));
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(ConfrontaConstants.HORA_EJECUCION, horaEjecucion));
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(ConfrontaConstants.DIA_SIG, diaSiguiente));
    }
    
    private void llenaBackupDataFailed(EmailTemplateDTO emailTemplateDTO) {
    	logger.info("Entre a backup data failed");
    	
    	String diaEjecucion = DateUtils.obtenerDiaEjecucion(new Date());
    	String horaEjecucion = DateUtils.obtenerHoraFin();
    	String diaSiguiente = DateUtils.obtenerDiaSig(new Date());
    	
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(ConfrontaConstants.DIA_EJECUCION, diaEjecucion));
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(ConfrontaConstants.HORA_EJECUCION, horaEjecucion));
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(ConfrontaConstants.DIA_SIG, diaSiguiente));
    }
    
    private void llenaFileGenerationFailed(EmailTemplateDTO emailTemplateDTO, Set<Long> keyList, Set<Long> llaves) {
    	logger.info("Entre a file generation failed");
    	
    	String diaEjecucion = DateUtils.obtenerDiaEjecucion(new Date());
    	String horaEjecucion = DateUtils.obtenerHoraFin();
    	String diaSiguiente = DateUtils.obtenerDiaSig(new Date());
    	String anioRev = String.valueOf(Integer.parseInt(DateUtils.getCurrentYear()) - 1);
    	
    	String ooadError = "";
    	String ooadExito = "";
    	
    	for (Long key : keyList) {
    		ooadError = ooadError.concat("<p>")
                    .concat(ConfrontaConstants.DEL_DESCRIPTION_MAP.get(key))
                    .concat("</p>");
		}
    	
    	for(Long key : llaves) {
    		ooadExito = ooadExito.concat("<p>")
    				.concat(ConfrontaConstants.DEL_DESCRIPTION_MAP.get(key))
    				.concat("</p>");
    	}
    	
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(ConfrontaConstants.DIA_EJECUCION, diaEjecucion));
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(ConfrontaConstants.HORA_EJECUCION, horaEjecucion));
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(ConfrontaConstants.DIA_SIG, diaSiguiente));
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(ConfrontaConstants.ANIO_REVISION, anioRev));
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(ConfrontaConstants.OOAD_ERROR, ooadError));
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(ConfrontaConstants.OOAD_EXITO, ooadExito));
    }
    
    private void llenaUploadFilesFailed(EmailTemplateDTO emailTemplateDTO, Set<Long> keyList, Set<Long> llaves) {
    	logger.info("Entre a update files failed");
    	
    	String diaEjecucion = DateUtils.obtenerDiaEjecucion(new Date());
    	String horaEjecucion = DateUtils.obtenerHoraFin();
    	String diaSiguiente = DateUtils.obtenerDiaSig(new Date());
    	String anioRev = String.valueOf(Integer.parseInt(DateUtils.getCurrentYear()) - 1);
    	
    	String ooadError = "";
    	String ooadExito = "";
    	
    	for(Long key : keyList) {
    		ooadError = ooadError.concat("<p>")
    				.concat(ConfrontaConstants.DEL_DESCRIPTION_MAP.get(key))
    				.concat("</p>");
    	}
    	
    	for(Long key : llaves) {
    		ooadExito = ooadExito.concat("<p>")
    				.concat(ConfrontaConstants.DEL_DESCRIPTION_MAP.get(key))
    				.concat("</p>");
    	}
    	
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(ConfrontaConstants.DIA_EJECUCION, diaEjecucion));
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(ConfrontaConstants.HORA_EJECUCION, horaEjecucion));
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(ConfrontaConstants.DIA_SIG, diaSiguiente));
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(ConfrontaConstants.ANIO_REVISION, anioRev));
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(ConfrontaConstants.OOAD_ERROR, ooadError));
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(ConfrontaConstants.OOAD_EXITO, ooadExito));
    }
    
    private void llenaBankFilesFailed(EmailTemplateDTO emailTemplateDTO, Set<Long> keyList, Set<Long> llaves) {
    	logger.info("Entre a bank files failed");
    	
    	String diaEjecucion = DateUtils.obtenerDiaEjecucion(new Date());
    	String horaEjecucion = DateUtils.obtenerHoraFin();
    	String diaSiguiente = DateUtils.obtenerDiaSig(new Date());
    	String anioRev = String.valueOf(Integer.parseInt(DateUtils.getCurrentYear()) - 1);
    	
    	String ooadError = "";
    	String ooadExito = "";
    	
    	for(Long key : keyList) {
    		ooadError = ooadError.concat("<p>")
    				.concat(ConfrontaConstants.DEL_DESCRIPTION_MAP.get(key))
    				.concat("</p>");
    	}
    	
    	for(Long key : llaves) {
    		ooadExito = ooadExito.concat("<p>")
    				.concat(ConfrontaConstants.DEL_DESCRIPTION_MAP.get(key))
    				.concat("</p>");
    	}
    	
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(ConfrontaConstants.DIA_EJECUCION, diaEjecucion));
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(ConfrontaConstants.HORA_EJECUCION, horaEjecucion));
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(ConfrontaConstants.DIA_SIG, diaSiguiente));
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(ConfrontaConstants.ANIO_REVISION, anioRev));
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(ConfrontaConstants.OOAD_ERROR, ooadError));
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(ConfrontaConstants.OOAD_EXITO, ooadExito));
    }
    
    private void llenaStepOne(EmailTemplateDTO emailTemplateDTO) {
    	logger.info("Entre a step 1");
    	
    	DecimalFormat df = new DecimalFormat("###,###");
    	
    	TypedAggregation<BackupConfrontaDTO> riesgos = ReaderUtils.totalRiesgos();
    	TypedAggregation<BackupConfrontaDTO> defunciones = ReaderUtils.contarTotalDefunciones();
    	TypedAggregation<BackupConfrontaDTO> porcentaje = ReaderUtils.sumarTotalPorcentajes();
    	TypedAggregation<BackupConfrontaDTO> dias = ReaderUtils.sumarTotalDias();
    	TypedAggregation<BackupConfrontaDTO> correctos = ReaderUtils.contarTotalCorrectos();
    	TypedAggregation<BackupConfrontaDTO> correctosOtras = ReaderUtils.contarTotalCorrectosOtras();
    	TypedAggregation<BackupConfrontaDTO> susceptibles = ReaderUtils.contarTotalSusceptibles();
    	TypedAggregation<BackupConfrontaDTO> susceptiblesOtras = ReaderUtils.contarTotalSusceptiblesOtras();
    	
    	AggregationResults<PorcentajeIncapacidadDTO> aggregationPorcentaje = mongoOperations.aggregate(porcentaje, PorcentajeIncapacidadDTO.class);
    	AggregationResults<DiasSubsidiadosDTO> aggregationDias = mongoOperations.aggregate(dias, DiasSubsidiadosDTO.class);
    	
    	List<PorcentajeIncapacidadDTO> listaPorcentaje = aggregationPorcentaje.getMappedResults();
    	List<DiasSubsidiadosDTO> listaDias = aggregationDias.getMappedResults();
    	
    	long totRiesgos = countRepository.count(riesgos);
    	long totDefunciones = countRepository.count(defunciones);
    	long totPorcentajes = listaPorcentaje.get(0).getPorcentajeIncapacidad();
    	long totDias = listaDias.get(0).getDiasSubsidiados();
    	long totCorrectos = countRepository.count(correctos);
    	long totCorrectosOtras = countRepository.count(correctosOtras);
    	long totSus = countRepository.count(susceptibles);
    	long totSusOtras = countRepository.count(susceptiblesOtras);
    	
    	String anioRev = String.valueOf(Integer.parseInt(DateUtils.getCurrentYear()) - 1);
    	String totalRiesgos = "";
    	String totalDefunciones = "";
    	String totalPorcentajes = "";
    	String totalDias = "";
    	String totalCorrectos = "";
    	String totalCorrectosOtras = "";
    	String totalSus = "";
    	String totalSusOtras = "";
    	
    	totalRiesgos = totalRiesgos.concat("Total de riesgos: ").concat(String.valueOf(df.format(totRiesgos)));
    	totalDefunciones = totalDefunciones.concat("Defunciones: ").concat(String.valueOf(df.format(totDefunciones)));
    	totalPorcentajes = totalPorcentajes.concat("Porcentaje de incapacidad: ").concat(String.valueOf(df.format(totPorcentajes)));
    	totalDias = totalDias.concat("D&iacute;as subsidiados: ").concat(String.valueOf(df.format(totDias)));
    	totalCorrectos = totalCorrectos.concat("Correctos: ").concat(String.valueOf(df.format(totCorrectos)));
    	totalCorrectosOtras = totalCorrectosOtras.concat("Correctos otras delegaciones: ").concat(String.valueOf(df.format(totCorrectosOtras)));
    	totalSus = totalSus.concat("Susceptibles de ajuste: ").concat(String.valueOf(df.format(totSus)));
    	totalSusOtras = totalSusOtras.concat("Susceptible de ajuste otras delegaciones: ").concat(String.valueOf(df.format(totSusOtras)));
    	
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(ConfrontaConstants.ANIO_REVISION, anioRev));
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(ConfrontaConstants.TOTAL_RIESGOS, totalRiesgos));
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(ConfrontaConstants.DEFUNCIONES, totalDefunciones));
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(ConfrontaConstants.PORCENTAJES, totalPorcentajes));
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(ConfrontaConstants.DIAS, totalDias));
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(ConfrontaConstants.CORRECTOS, totalCorrectos));
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(ConfrontaConstants.CORRECTOS_OTRAS, totalCorrectosOtras));
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(ConfrontaConstants.SUS, totalSus));
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(ConfrontaConstants.SUS_OTRAS, totalSusOtras));
    	
    }
    
    private void llenaStepsTwoThreeFourFive(EmailTemplateDTO emailTemplateDTO) {
    	logger.info("Entre a steps 2-3-4-5");
    	
    	String anioRev = String.valueOf(Integer.parseInt(DateUtils.getCurrentYear()) - 1);
    	
    	emailTemplateDTO.setTemplate(emailTemplateDTO.getTemplate().replace(ConfrontaConstants.ANIO_REVISION, anioRev));
    }

}
