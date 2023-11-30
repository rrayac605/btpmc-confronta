package mx.gob.imss.cit.pmc.confronta.constants;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.StepExecution;
import org.springframework.data.domain.Sort;

import com.google.common.collect.ImmutableMap;

import mx.gob.imss.cit.pmc.confronta.enums.AuditActionEnum;
import mx.gob.imss.cit.pmc.confronta.enums.ProcessActionEnum;

public class ConfrontaConstants {

	public static final String CTROL_LLAVE_CTL = "000000000000000000000000000";

	public static final String CTROL_HEADER = "000000";

	public static final String CTROL_NOMB_ARCH_CTL = "TSRT-A-CSQ";

	public static final String CTROL_CVE_CTL = "652";

	public static final String CTROL_TOTAL_REG_CTL = "0000000000";

	public static final String FILLER = "0000000000000000000000000000";

	public static final String KEY_PARAM = "key";

	public static final String NOT_ERROR_FLAG_PARAM = "not_error_flag";

	public static final String REPROCESS_FLAG = "reprocess_flag";

	public static final String OOAD_LIST_TEMPLATE = "${ooadList}";

	public static final String ACTION_PARAM = "action";

	public static final String ACTION_AUDIT_PARAM = "action_audit";

	public static final String PAST_STEP_PARAM = "past_step";

	public static final String IS_TERMINATED_ONLY = "is_terminated_only";

	public static final String EXECUTION_DATE = "execution_date";

	public static final String CONFRONTA_JOB = "confrontaJob";

	public static final String FILE_VALIDATION_STEP = "fileValidationStep";

	public static final String SFTP1_UPLOAD_FILE_STEP = "sftp1UploadFileStep";

	public static final String SFTP2_UPLOAD_FILE_STEP = "sftp2UploadFileStep";

	public static final String SFTP_UPLOAD_FILE_STEP = "sftpUploadFileStep";

	public static final String HEADER_STEP = "headerStep";

	public static final String FOOTER_STEP = "footerStep";

	public static final Long WILL_BE_INJECTED_LONG = null;

	public static final String WILL_BE_INJECTED_STRING = null;

	public static final StepExecution WILL_BE_INJECTED_SE = null;

	public static final String PATTERN_YYYYMMDD = "yyyyMMdd";

	public static final String PATTERN_TIME = "HHmmss";

	public static final String BASE_PATH_SERVER = "/opt/jboss/archivoscierreanual/respaldo/{year}";

	public static final String FILE_NAME = "TSRCASU203.T";

	public static final String FILE_NAME_ONE_SUBDEL = "TSRCOCA206.T";

	public static final String USER = "Sistema PMC";

	public static final String ENCODING = "UTF-8";

	public static final String FOOTER_IMG = "footer.png";

	public static final String HEADER_IMG = "header.png";

	public static final String IMAGE_PATH = "/static/images/";
	
	public static final String CONFRONTA_TEMPLATE = "CONFRONTA_PROCESS_SUCCESSFUL";

	public static final String GET_INFO_FAILED_TEMPLATE = "GET_INFO_FAILED";

	public static final String BACKUP_DATA_FAILED_TEMPLATE = "BACKUP_DATA_FAILED";

	public static final String FILE_GENERATION_FAILED_TEMPLATE = "FILE_GENERATION_FAILED";

	public static final String UPLOAD_FILES_FAILED_TEMPLATE = "UPLOAD_FILES_FAILED";

	public static final String BANK_FILES_FAILED_TEMPLATE = "BANK_FILES_FAILED";

	public static final String STEP_ONE_TEMPLATE = "STEP_ONE_CONFRONTA";

	public static final String STEP_TWO_TEMPLATE = "STEP_TWO_CONFRONTA";

	public static final String STEP_THREE_TEMPLATE = "STEP_THREE_CONFRONTA";

	public static final String STEP_FOUR_TEMPLATE = "STEP_FOUR_CONFRONTA";

	public static final String STEP_FIVE_TEMPLATE = "STEP_FIVE_CONFRONTA";

	public static final String DIA_EJECUCION = "${diaEjecucion}";

	public static final String DIA_FIN = "${diaFin}";

	public static final String DIA_SIG = "${diaSiguiente}";

	public static final String HORA_FIN = "${horaFin}";

	public static final String HORA_EJECUCION = "${horaEjecucion}";

	public static final String ANIO_REVISION = "${anioRevision}";

	public static final String TOTAL_REGS = "${totalRegs}";

	public static final String LISTA_OOAD = "${listaOOADS}";

	public static final String OOAD_ERROR = "${ooadError}";

	public static final String OOAD_EXITO = "${ooadExito}";

	public static final String TOTAL_RIESGOS = "${totalRiesgos}";

	public static final String DEFUNCIONES = "${defunciones}";

	public static final String PORCENTAJES = "${porcentajeIncapacidad}";

	public static final String DIAS = "${diasSubsidiados}";

	public static final String CORRECTOS = "${correctos}";

	public static final String CORRECTOS_OTRAS = "${correctosOtras}";

	public static final String SUS = "${susceptibles}";

	public static final String SUS_OTRAS = "${susceptiblesOtras}";

	public static final String TOTALES_CONFRONTA = "${totales}";
	
	public static final Integer CVE_CORRECTOS = 1;

	public static final Integer CVE_CORRECTOS_OTRAS = 5;

	public static final Integer CVE_SUSCEPTIBLES = 4;

	public static final Integer CVE_SUSCEPTIBLES_OTRAS = 8;

	public static final Integer CVE_CONSECUENCIA_DEFUNCION = 4;

	public static final String FORMATO_DIA_EJECUCION = "dd-MM-YYYY";

	public static final String FORMATO_HORA_FIN = "HH:mm:ss";
	
	public static final String FORMATO_HORA_EJEC = "HH:mm";

	public static final String LAST_HOUR_MINUTE_SECOND = "23:59:59";
	
	public static final String HEADER_IMG_ESP = "headerEsp.png";

	public static final String FOOTER_IMG_ESP = "footerEsp.png";

    public static final Map<Integer, List<Integer>> DEL_SUBDEL = ImmutableMap.<Integer, List<Integer>>builder()
            .put(1, Arrays.asList(1, 1, 19))
            .put(2, Arrays.asList(2, 1, 2, 3, 4))
            .put(3, Arrays.asList(3, 1, 8))
            .put(4, Arrays.asList(4, 1, 4))
            .put(5, Arrays.asList(8, 1, 3, 5, 8, 22, 60))
            .put(6, Arrays.asList(8, 10))
            .put(7, Arrays.asList(6, 1, 3, 7))
            .put(8, Arrays.asList(5, 3, 11, 12, 17, 23))
            .put(9, Arrays.asList(7, 1, 2))
            .put(10, Arrays.asList(39, 11, 16, 54, 56, 57))
            .put(11, Arrays.asList(40, 1, 6, 11, 54, 58))
            .put(12, Arrays.asList(10, 1, 13))
            .put(13, Arrays.asList(12, 1, 2, 3, 13))
            .put(14, Arrays.asList(11, 1, 5, 8, 14, 17))
            .put(15, Arrays.asList(27, 1))
            .put(16, Arrays.asList(13, 1, 5, 7, 10))
            .put(17, Arrays.asList(14, 12, 15, 22, 38, 39, 40, 50))
            .put(18, Arrays.asList(17, 3, 9, 13, 17, 27))
            .put(19, Arrays.asList(16, 1, 5))
            .put(20, Arrays.asList(20, 6, 8, 31, 32, 33, 34))
            .put(21, Arrays.asList(18, 1, 11, 15))
            .put(22, Arrays.asList(29, 19))
            .put(23, Arrays.asList(26, 5))
            .put(24, Arrays.asList(19, 1))
            .put(25, Arrays.asList(21, 2, 3, 4, 53))
            .put(26, Arrays.asList(22, 1, 5, 6, 8, 22))
            .put(27, Arrays.asList(24, 1, 2, 7))
            .put(28, Arrays.asList(23, 1, 3))
            .put(29, Arrays.asList(26, 1, 3, 4))
            .put(30, Arrays.asList(25, 1, 3, 5, 60))
            .put(31, Arrays.asList(27, 3, 7, 10, 13, 51, 57, 70))
            .put(32, Arrays.asList(29, 1, 4, 10, 13, 18))
            .put(33, Arrays.asList(28, 1, 2))
	        .put(34, Arrays.asList(2, 5))
	        .put(35, Arrays.asList(15, 6, 54, 80))
            .put(36, Arrays.asList(5, 9))
            .put(37, Arrays.asList(30, 1))
            .put(38, Arrays.asList(31, 2, 7, 9, 25))
            .put(39, Arrays.asList(31, 12))
            .put(40, Arrays.asList(32, 2, 3, 38, 45))
            .put(41, Arrays.asList(33, 1, 33))
            .put(42, Arrays.asList(34, 1, 9))
            .build();

    public static final Map<Long, String> PATH_FTP1 = ImmutableMap.<Long, String>builder()
            .put(1L, "/01/00/AGUASCALIENTES/ARCH/")
            .put(2L, "/02/00/BAJA_CALIFOR_NTE/ARCH/")
            .put(3L, "/03/00/BAJA_CALIFOR_SUR/ARCH/")
            .put(4L, "/04/00/CAMPECHE/ARCH/")
            .put(5L, "/08/00/CHIHUAHUA/ARCH/")
            .put(6L, "/08/10/JUAREZ_1/ARCH/")
            .put(7L, "/06/00/COLIMA/ARCH/")
            .put(8L, "/05/00/COAHUILA/ARCH/")
            .put(9L, "/07/00/CHIAPAS/ARCH/")
            .put(10L, "/39/00/DF_NORTE/ARCH/")
            .put(11L, "/40/00/DF_SUR/ARCH/")
            .put(12L, "/10/00/DURANGO/ARCH/")
            .put(13L, "/12/00/GUERRERO/ARCH/")
            .put(14L, "/11/00/GUANAJUATO/ARCH/")
            .put(15L, "/27/01/HERMOSILLO/ARCH/")
            .put(16L, "/13/00/HIDALGO/ARCH/")
            .put(17L, "/14/00/JALISCO/ARCH/")
            .put(18L, "/17/00/MICHOACAN/ARCH/")
            .put(19L, "/16/00/MEX_PONIENTE/ARCH/")
            .put(20L, "/20/00/NUEVO_LEON/ARCH/")
            .put(21L, "/18/00/MORELOS/ARCH/")
            .put(22L, "/29/19/MATAMOROS/ARCH/")
            .put(23L, "/26/05/MAZATLAN/ARCH/")
            .put(24L, "/19/00/NAYARIT/ARCH/")
            .put(25L, "/21/00/OAXACA/ARCH/")
            .put(26L, "/22/00/PUEBLA/ARCH/")
            .put(27L, "/24/00/QUINTANA_ROO/ARCH/")
            .put(28L, "/23/00/QUERETARO/ARCH/")
            .put(29L, "/26/00/SINALOA/ARCH/")
            .put(30L, "/25/00/SAN_LUIS_POTOSI/ARCH/")
            .put(31L, "/27/00/SONORA/ARCH/")
            .put(32L, "/29/00/TAMAULIPAS/ARCH/")
            .put(33L, "/28/00/TABASCO/ARCH/")
            .put(34L, "/02/05/TIJUANA/ARCH/")
            .put(35L, "/15/00/MEX_ORIENTE/ARCH/")
            .put(36L, "/05/09/TORREON/ARCH/")
            .put(37L, "/30/00/TLAXCALA/ARCH/")
            .put(38L, "/31/00/VERACRUZ_NORTE/ARCH/")
            .put(39L, "/31/12/VERACRUZ_PUERTO/ARCH/")
            .put(40L, "/32/00/VERACRUZ_SUR/ARCH/")
            .put(41L, "/33/00/YUCATAN/ARCH/")
            .put(42L, "/34/00/ZACATECAS/ARCH/")
            .build();

    public static final Map<Long, String> PATH_FTP2 = ImmutableMap.<Long, String>builder()
            .put(1L, "/ag/DO/ARCH/")
            .put(2L, "/bn/DO/ARCH/")
            .put(3L, "/bs/DO/ARCH/")
            .put(4L, "/ca/DO/ARCH/")
            .put(5L, "/chihuahua/DO/ARCH/")
            .put(6L, "/cj/DO/ARCH/")
            .put(7L, "/cl/DO/ARCH/")
            .put(8L, "/coahuila/DO/ARCH/")
            .put(9L, "/cs/DO/ARCH/")
            .put(10L, "/d39/DO/ARCH/")
            .put(11L, "/d40/DO/ARCH/")
            .put(12L, "/durango/DO/ARCH/")
            .put(13L, "/gr/DO/ARCH/")
            .put(14L, "/gt/DO/ARCH/")
            .put(15L, "/he/DO/ARCH/")
            .put(16L, "/hg/DO/ARCH/")
            .put(17L, "/jl/DO/ARCH/")
            .put(18L, "/mc/DO/ARCH/")
            .put(19L, "/me/DO/ARCH/")
            .put(20L, "/monterrey/DO/ARCH/")
            .put(21L, "/mor/DO/ARCH/")
            .put(22L, "/mt/DO/ARCH/")
            .put(23L, "/mz/DO/ARCH/")
            .put(24L, "/ny/DO/ARCH/")
            .put(25L, "/oa/DO/ARCH/")
            .put(26L, "/pu/DO/ARCH/")
            .put(27L, "/qr/DO/ARCH/")
            .put(28L, "/qu/DO/ARCH/")
            .put(29L, "/sn/DO/ARCH/")
            .put(30L, "/sp/DO/ARCH/")
            .put(31L, "/sr/DO/ARCH/")
            .put(32L, "/tamaulipas/DO/ARCH/")
            .put(33L, "/tb/DO/ARCH/")
            .put(34L, "/tj/DO/ARCH/")
            .put(35L, "/tl/DO/ARCH/")
            .put(36L, "/tr/DO/ARCH/")
            .put(37L, "/tx/DO/ARCH/")
            .put(38L, "/vn/DO/ARCH/")
            .put(39L, "/vp/DO/ARCH/")
            .put(40L, "/vs/DO/ARCH/")
            .put(41L, "/yc/DO/ARCH/")
            .put(42L, "/za/DO/ARCH/")
            .build();

    public static final Map<Long, String> DEL_DESCRIPTION_MAP = ImmutableMap.<Long, String>builder()
            .put(1L, "AGUASCALIENTES")
            .put(2L, "BAJA CALIFORNIA NORTE")
            .put(3L, "BAJA CALIFORNIA SUR")
            .put(4L, "CAMPECHE")
            .put(5L, "CHIHUAHUA")
            .put(6L, "JUAREZ 1")
            .put(7L, "COLIMA")
            .put(8L, "COAHUILA")
            .put(9L, "CHIAPAS")
            .put(10L, "DISTRITO FEDERAL NORTE")
            .put(11L, "DISTRITO FEDERAL SUR")
            .put(12L, "DURANGO")
            .put(13L, "GUERRERO")
            .put(14L, "GUANAJUATO")
            .put(15L, "HERMOSILLO")
            .put(16L, "HIDALGO")
            .put(17L, "JALISCO")
            .put(18L, "MICHOACAN")
            .put(19L, "ESTADO DE MEXICO PONIENTE")
            .put(20L, "NUEVO LEON")
            .put(21L, "MORELOS")
            .put(22L, "MATAMOROS")
            .put(23L, "MAZATLAN")
            .put(24L, "NAYARIT")
            .put(25L, "OAXACA")
            .put(26L, "PUEBLA")
            .put(27L, "QUINTANA ROO")
            .put(28L, "QUERETARO")
            .put(29L, "SINALOA")
            .put(30L, "SAN LUIS POTOSI")
            .put(31L, "SONORA")
            .put(32L, "TAMAULIPAS")
            .put(33L, "TABASCO")
            .put(34L, "TIJUANA")
            .put(35L, "ESTADO DE MEXICO ORIENTE")
            .put(36L, "TORREON")
            .put(37L, "TLAXCALA")
            .put(38L, "VERACRUZ NORTE")
            .put(39L, "VERACRUZ PUERTO")
            .put(40L, "VERACRUZ SUR")
            .put(41L, "YUCATAN")
            .put(42L, "ZACATECAS")
            .build();

	public static final Map<String, String> FROM_STEP_ACTION_NAME = ImmutableMap.<String, String>builder()
            .put("footerStep", ProcessActionEnum.GET_INFO.getDesc())
            .put("backupChangeStep", ProcessActionEnum.BACKUP_INFO.getDesc())
            .put("backupDescartadosChangeStep", ProcessActionEnum.READ_INFO_DESCARTADOS.getDesc())
            .put("backupMovementDescartadosStep", ProcessActionEnum.BACKUP_INFO_DESCARTADOS.getDesc())
            .put(FILE_VALIDATION_STEP, ProcessActionEnum.GENERATE_FILES.getDesc())
            .put(SFTP1_UPLOAD_FILE_STEP, ProcessActionEnum.FILES_STORAGE.getDesc())
            .put(SFTP2_UPLOAD_FILE_STEP, ProcessActionEnum.BANK_FILES.getDesc())
            .put("successProcessValidationStep", ProcessActionEnum.CONFRONTA_PROCESS.getDesc())
            .build();

    public static final Map<String, String> FROM_STEP_FAILED_ACTION_NAME = ImmutableMap.<String, String>builder()
            .put("movementStep", ProcessActionEnum.GET_INFO.getDesc())
            .put("changeStep", ProcessActionEnum.GET_INFO.getDesc())
            .put("backupDescartadosChangeStep", ProcessActionEnum.READ_INFO_DESCARTADOS.getDesc())
            .put("backupMovementDescartadosStep", ProcessActionEnum.BACKUP_INFO_DESCARTADOS.getDesc())
            .put(HEADER_STEP, ProcessActionEnum.FILE_GENERATION.getDesc())
            .put(FOOTER_STEP, ProcessActionEnum.FILE_GENERATION.getDesc())
            .put("backupMovementStep", ProcessActionEnum.BACKUP_INFO.getDesc())
            .put("backupChangeStep", ProcessActionEnum.BACKUP_INFO.getDesc())
            .put(FILE_VALIDATION_STEP, ProcessActionEnum.GENERATE_FILES.getDesc())
            .put(SFTP1_UPLOAD_FILE_STEP, ProcessActionEnum.FILES_STORAGE.getDesc())
            .put(SFTP2_UPLOAD_FILE_STEP, ProcessActionEnum.BANK_FILES.getDesc())
            .build();

    public static final Map<String, String> FROM_STEP_ACTION_AUDIT_NAME = ImmutableMap.<String, String>builder()
            .put("backupChangeStep", AuditActionEnum.BACKUP_INFO.getDesc())
            .put("backupDescartadosChangeStep", ProcessActionEnum.READ_INFO_DESCARTADOS.getDesc())
            .put("backupMovementDescartadosStep", ProcessActionEnum.BACKUP_INFO_DESCARTADOS.getDesc())
            .put(SFTP1_UPLOAD_FILE_STEP, AuditActionEnum.FILES_STORAGE.getDesc())
            .put(SFTP2_UPLOAD_FILE_STEP, AuditActionEnum.BANK_FILES.getDesc())
            .build();

    public static final Map<String, String> FROM_STEP_FAILED_ACTION_AUDIT_NAME = ImmutableMap.<String, String>builder()
            .put("movementStep", AuditActionEnum.GET_INFO.getDesc())
            .put("changeStep", AuditActionEnum.GET_INFO.getDesc())
            .put(FOOTER_STEP, AuditActionEnum.GENERATE_FILES.getDesc())
            .put(HEADER_STEP, AuditActionEnum.GENERATE_FILES.getDesc())
            .put("backupMovementStep", AuditActionEnum.BACKUP_INFO.getDesc())
            .put("backupChangeStep", AuditActionEnum.BACKUP_INFO.getDesc())
            .put(FILE_VALIDATION_STEP, AuditActionEnum.GENERATE_FILES.getDesc())
            .put(SFTP1_UPLOAD_FILE_STEP, AuditActionEnum.FILES_STORAGE.getDesc())
            .put(SFTP2_UPLOAD_FILE_STEP, AuditActionEnum.BANK_FILES.getDesc())
            .build();

    public static final Map<String, String> FROM_STEP_TEMPLATE_NAME = ImmutableMap.<String, String>builder()
            .put(SFTP2_UPLOAD_FILE_STEP, "CONFRONTA_PROCESS_SUCCESSFUL")
            .build();

    public static final Map<String, String> ACTION_TO_TEMPLATE_NAME = ImmutableMap.<String, String>builder()
            .put(ProcessActionEnum.GET_INFO.getDesc(), "GET_INFO_FAILED")
            .put(ProcessActionEnum.BACKUP_INFO.getDesc(), "BACKUP_DATA_FAILED")
            .put(ProcessActionEnum.FILE_GENERATION.getDesc(), "FILE_GENERATION_FAILED")
            .build();

    public static final Map<Integer, String> FILE_VALIDATION_EQUIVALENT = ImmutableMap.<Integer, String>builder()
            .put(1, FILE_VALIDATION_STEP)
            .put(2, SFTP1_UPLOAD_FILE_STEP)
            .put(3, SFTP2_UPLOAD_FILE_STEP)
            .build();

    public static final Map<Integer, String> UPLOAD_FILE_EQUIVALENT = ImmutableMap.<Integer, String>builder()
            .put(1, SFTP1_UPLOAD_FILE_STEP)
            .put(2, SFTP2_UPLOAD_FILE_STEP)
            .build();

    public static final String PATTERN_DDMMYYYY_TIME = "ddMMyyyy HH:mm:ss";

    public static final String PATTERN_YYYY_MM_DD_TIME = "yyyy-MM-dd";

    public static final Integer CHUNK_SIZE = 300;

    public static final String MANUAL = "MN";

    public static final Integer POOL_SIZE = 64;

    public static final String MISSING_CURP = "000000000000000000";

    public static final String MISSING_NSS_RP = "0000000000";

    public static final Object[] CRITERIA_ESTADO_REGISTRO_CORRECTO = { 1, 5 };
    
    public static final Object[] CRITERIA_ESTADO_REGISTRO_CORRECTO_NVO = { 1 };

    public static final Object[] CRITERIA_ESTADO_REGISTRO_SUSCEPTIBLE = { 4, 8 };

    public static final Object[] CRITERIA_CONSECUENCIA = { null, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };

    public static final Object CRITERIA_SITUACION_REGISTRO = 1;

    public static final Integer FIRST = 1;

    public static final Integer FIFTEEN = 15;

    public static final Integer ZERO = 0;

    public static final String ZERO_STRING = "0";

    public static final String FIRST_DAY = "1";

    public static final String FIRST_MONTH = "1";

    public static final String LAST_MONTH = "12";

    public static final String LAST_DECEMBER_DAY = "31";

    public static final String BEGIN_HOURS = " 00:00:00";

    public static final String END_HOURS = " 18:59:59";

    public static final String ISO_TIMEZONE = "UTC";

    public static final Integer HOURS_TO_ADD = 6;

    public static final Integer TEN = 10;

    public static final String EMPTY = "";

    public static final String EMPTY_SPACE = " ";    

    public static final Map<String, Sort.Direction> READER_SORTER = ImmutableMap.of("_id", Sort.Direction.ASC);

    public static final Map<Integer, Integer> CONSEQUENCE_EQUIVALENCE = ImmutableMap.<Integer, Integer>builder()
            .put(0, 1)
            .put(10, 2)
            .put(11, 3)
            .put(12, 3)
            .build();

}
