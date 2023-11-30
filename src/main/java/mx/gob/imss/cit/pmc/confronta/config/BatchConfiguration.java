package mx.gob.imss.cit.pmc.confronta.config;

import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.builder.MongoItemReaderBuilder;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.SneakyThrows;
import mx.gob.imss.cit.pmc.confronta.constants.ConfrontaConstants;
import mx.gob.imss.cit.pmc.confronta.dto.BackupConfrontaDTO;
import mx.gob.imss.cit.pmc.confronta.dto.ChangeDTO;
import mx.gob.imss.cit.pmc.confronta.dto.MovementDTO;
import mx.gob.imss.cit.pmc.confronta.listener.JobConfrontaListener;
import mx.gob.imss.cit.pmc.confronta.listener.MongoProcessorListener;
import mx.gob.imss.cit.pmc.confronta.listener.MongoReaderListener;
import mx.gob.imss.cit.pmc.confronta.listener.MongoWriterListener;
import mx.gob.imss.cit.pmc.confronta.listener.StepConfrontaListener;
import mx.gob.imss.cit.pmc.confronta.processor.BackupChangeProcessor;
import mx.gob.imss.cit.pmc.confronta.processor.BackupChangeProcessorDescartados;
import mx.gob.imss.cit.pmc.confronta.processor.BackupMovementProcessor;
import mx.gob.imss.cit.pmc.confronta.processor.ConfrontaChangeProcessor;
import mx.gob.imss.cit.pmc.confronta.processor.ConfrontaMovementProcessor;
import mx.gob.imss.cit.pmc.confronta.tasklet.AuditFailedTasklet;
import mx.gob.imss.cit.pmc.confronta.tasklet.AuditTasklet;
import mx.gob.imss.cit.pmc.confronta.tasklet.EmailOOADTasklet;
import mx.gob.imss.cit.pmc.confronta.tasklet.EmailStepFiveTasklet;
import mx.gob.imss.cit.pmc.confronta.tasklet.EmailStepFourTasklet;
import mx.gob.imss.cit.pmc.confronta.tasklet.EmailStepOneTasklet;
import mx.gob.imss.cit.pmc.confronta.tasklet.EmailStepThreeTasklet;
import mx.gob.imss.cit.pmc.confronta.tasklet.EmailStepTwoTasklet;
import mx.gob.imss.cit.pmc.confronta.tasklet.EmailTasklet;
import mx.gob.imss.cit.pmc.confronta.tasklet.FileControlFailedTasklet;
import mx.gob.imss.cit.pmc.confronta.tasklet.FileValidationTasklet;
import mx.gob.imss.cit.pmc.confronta.tasklet.FooterTasklet;
import mx.gob.imss.cit.pmc.confronta.tasklet.GenerateFileSuccessTasklet;
import mx.gob.imss.cit.pmc.confronta.tasklet.HeaderTasklet;
import mx.gob.imss.cit.pmc.confronta.tasklet.ProcessControlFailedTasklet;
import mx.gob.imss.cit.pmc.confronta.tasklet.ProcessControlTasklet;
import mx.gob.imss.cit.pmc.confronta.tasklet.RollBackTasklet;
import mx.gob.imss.cit.pmc.confronta.tasklet.SftpUploadFileTasklet;
import mx.gob.imss.cit.pmc.confronta.tasklet.SuccessProcessValidatorTasklet;
import mx.gob.imss.cit.pmc.confronta.utils.ExitStatusConfronta;
import mx.gob.imss.cit.pmc.confronta.utils.NumberUtils;
import mx.gob.imss.cit.pmc.confronta.utils.ReaderUtils;
import mx.gob.imss.cit.pmc.confronta.utils.StringUtils;

@Configuration
@EnableBatchProcessing
@ComponentScan(basePackages = { "mx.gob.imss.cit.pmc.confronta" })
public class BatchConfiguration extends DefaultBatchConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(BatchConfiguration.class);
    
    @Autowired
    private BackupChangeProcessorDescartados backupChangeProcessorDescartados;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private MongoOperations mongoOperations;

    @Autowired
    private HeaderTasklet headerTasklet;

    @Autowired
    private FooterTasklet footerTasklet;
    
    @Autowired
    private SftpUploadFileTasklet sftpUploadFileTasklet;

    @Autowired
    private ProcessControlTasklet processControlTasklet;

    @Autowired
    private ProcessControlFailedTasklet processControlFailedTasklet;

    @Autowired
    private AuditTasklet auditTasklet;

    @Autowired
    private AuditFailedTasklet auditFailedTasklet;

    @Autowired
    private GenerateFileSuccessTasklet generateFileSuccessTasklet;

    @Autowired
    private FileControlFailedTasklet fileControlFailedTasklet;

    @Autowired
    private FileValidationTasklet fileValidationTasklet;

    @Autowired
    private EmailTasklet emailTasklet;
    
    @Autowired
    private EmailStepOneTasklet emailStepOneTasklet;
    
    @Autowired
    private EmailStepTwoTasklet emailStepTwoTasklet;
    
    @Autowired
    private EmailStepThreeTasklet emailStepThreeTasklet;
    
    @Autowired
    private EmailStepFourTasklet emailStepFourTasklet;
    
    @Autowired
    private EmailStepFiveTasklet emailStepFiveTasklet;

    @Autowired
    private EmailOOADTasklet emailOOADTasklet;

    @Autowired
    private SuccessProcessValidatorTasklet successProcessValidatorTasklet;

    @Autowired
    private RollBackTasklet rollBackTasklet;

    @Autowired
    private MongoReaderListener<MovementDTO> movementReaderListener;

    @Autowired
    private MongoReaderListener<ChangeDTO> changeReaderListener;

    @Autowired
    private MongoProcessorListener<MovementDTO, String> movementProcessorListener;

    @Autowired
    private MongoProcessorListener<ChangeDTO, String> changeProcessorListener;

    @Autowired
    private MongoProcessorListener<MovementDTO, BackupConfrontaDTO> backupMovementProcessorListener;

    @Autowired
    private MongoProcessorListener<ChangeDTO, BackupConfrontaDTO> backupChangeProcessorListener;

    @Autowired
    private MongoWriterListener<String> fileWriterListener;

    @Autowired
    private MongoWriterListener<MovementDTO> movementWriterListener;

    @Autowired
    private MongoWriterListener<ChangeDTO> changeWriterListener;

    @Autowired
    private ConfrontaMovementProcessor confrontaMovementProcessor;

    @Autowired
    private ConfrontaChangeProcessor confrontaChangeProcessor;

    @Autowired
    private BackupMovementProcessor backupMovementProcessor;

    @Autowired
    private BackupChangeProcessor backupChangeProcessor;

    @Autowired
    private StepConfrontaListener stepConfrontaListener;

    @Autowired
    private JobConfrontaListener jobConfrontaListener;
        

    @Bean("batchTransactionManager")
    @NonNull
    public PlatformTransactionManager getTransactionManager() {
        return new ResourcelessTransactionManager();
    }

    @SneakyThrows
    @NonNull
    @Bean("batchJobRepository")
    public JobRepository getJobRepository() {
        return Objects.requireNonNull(new MapJobRepositoryFactoryBean(getTransactionManager()).getObject());
    }

    @Bean(name = "confrontaJob")
    public Job confrontaJob() {
        return jobBuilderFactory.get("confrontaJob")
                .incrementer(new RunIdIncrementer())
                .listener(jobConfrontaListener)
                .start(fileCreationFlow())
                .on(ExitStatus.FAILED.getExitCode())
                .to(failToCreateFileFlow())
                .from(fileCreationFlow())
                .on(ExitStatus.COMPLETED.getExitCode())
                .to(backupDataFlow())
                .on(ExitStatus.FAILED.getExitCode())
                .to(failToBackupDataFlow())
                .from(backupDataFlow())
                .on(ExitStatus.COMPLETED.getExitCode())
                .to(generateFileSuccessFlow())
                .from(generateFileSuccessFlow())
                .on(ExitStatus.COMPLETED.getExitCode())
                .to(backupDescartadosFlow())
                .from(backupDescartadosFlow())
                .on(ExitStatus.COMPLETED.getExitCode())
                .to(backupDescartadosAnteriorPosteriorFlow())
                .end()
                .build();
    }
    
    @Bean(name = "descartadosJob")
    public Job descartadosJob() {
        return jobBuilderFactory.get("descartadosJob")
                .incrementer(new RunIdIncrementer())
                .listener(jobConfrontaListener)
                .start(backupDescartadosFlow())
                .end()
                .build();
    }
    
    @Bean(name = "descartadosAnteriorPosteriorJob")
    public Job descartadosAnteriorPosteriorJob() {
        return jobBuilderFactory.get("descartadosAnteriorPosteriorJob")
                .incrementer(new RunIdIncrementer())
                .listener(jobConfrontaListener)
                .start(backupDescartadosAnteriorPosteriorFlow())
                .end()
                .build();
    }

    @Bean
    public Flow fileCreationFlow() {
        return new FlowBuilder<Flow>("fileCreationFlow")
                .start(headerStep())
                .next(movementStep())
                .next(changeStep())
                .next(footerStep())
                .next(processControlStep())
                .end();

    }
    
    @Bean
    public Flow backupDescartadosFlow() {
        return new FlowBuilder<Flow>("backupDescartadosFlow")
                .start(backupMovementDescartadosStep())
                .next(backupDescartadosChangeStep())                
                .next(processControlStep())
                .next(auditStep())
                .end();

    }
    
    @Bean
    public Flow backupDescartadosAnteriorPosteriorFlow() {
        return new FlowBuilder<Flow>("backupDescartadosAnteriorPosteriorFlow")
                .start(backupMovementAnteriorDescartadosStep())
                .next(backupMovementPosteriorDescartadosStep())
                .next(backupDescartadosAnteriorChangeStep()) 
                .next(backupDescartadosPosteriorChangeStep()) 
                .next(processControlStep())
                .next(auditStep())
                .end();

    }

    @Bean
    public Flow failToCreateFileFlow() {
        return new FlowBuilder<Flow>("failToCreateFileFlow")
                .start(processControlFailedStep())
                .next(fileControlFailedStep())
                .next(auditFailedStep())
                .end();
    }

    @Bean
    public Flow backupDataFlow() {
        return new FlowBuilder<Flow>("backupDataFlow")
                .start(backupMovementStep())
                .next(backupChangeStep())
                .next(processControlStep())
                .next(auditStep())
                .end();
    }

    @Bean
    public Flow failToBackupDataFlow() {
        return new FlowBuilder<Flow>("failToBackupDataFlow")
                .start(rollBackBackupStep())
                .next(processControlFailedStep())
                .next(auditFailedStep())
                .end();
    }

    @Bean
    public Flow generateFileSuccessFlow() {
        return new FlowBuilder<Flow>("generateFileSuccessFlow")
                .start(generateFileSuccessStep())
                .end();
    }

    @Bean
    public Step headerStep() {
        return stepBuilderFactory.get("headerStep")
                .tasklet(headerTasklet)
                .listener(stepConfrontaListener)
                .build();
    }

    @Bean
    public Step movementStepDescartados() {
        return stepBuilderFactory.get("movementStepDescartados")
                .<MovementDTO, String>chunk(ConfrontaConstants.CHUNK_SIZE)
                .reader(movementReader(ConfrontaConstants.WILL_BE_INJECTED_LONG, ConfrontaConstants.WILL_BE_INJECTED_STRING))
                .processor(confrontaMovementProcessor)
                .listener(movementProcessorListener)
                .writer(fileWriter(ConfrontaConstants.WILL_BE_INJECTED_SE, ConfrontaConstants.WILL_BE_INJECTED_LONG))
                .listener(fileWriterListener)
                .listener(stepConfrontaListener)
                .taskExecutor(taskExecutor())
                .throttleLimit(ConfrontaConstants.POOL_SIZE)
                .build();
    }
    
    @Bean
    public Step movementStep() {
        return stepBuilderFactory.get("movementStep")
                .<MovementDTO, String>chunk(ConfrontaConstants.CHUNK_SIZE)
                .reader(movementReader(ConfrontaConstants.WILL_BE_INJECTED_LONG, ConfrontaConstants.WILL_BE_INJECTED_STRING))
                .listener(movementReaderListener)
                .processor(confrontaMovementProcessor)
                .listener(movementProcessorListener)
                .writer(fileWriter(ConfrontaConstants.WILL_BE_INJECTED_SE, ConfrontaConstants.WILL_BE_INJECTED_LONG))
                .listener(fileWriterListener)
                .listener(stepConfrontaListener)
                .taskExecutor(taskExecutor())
                .throttleLimit(ConfrontaConstants.POOL_SIZE)
                .build();
    }
    
    @Bean
    public Step movementDescartadosStep() {
        return stepBuilderFactory.get("movementStep")
                .<MovementDTO, String>chunk(ConfrontaConstants.CHUNK_SIZE)
                .reader(movementDescartadosReader(ConfrontaConstants.WILL_BE_INJECTED_LONG, ConfrontaConstants.WILL_BE_INJECTED_STRING))
                .processor(confrontaMovementProcessor)
                .listener(movementProcessorListener)
                .writer(fileWriter(ConfrontaConstants.WILL_BE_INJECTED_SE, ConfrontaConstants.WILL_BE_INJECTED_LONG))
                .listener(fileWriterListener)
                .listener(stepConfrontaListener)
                .taskExecutor(taskExecutor())
                .throttleLimit(ConfrontaConstants.POOL_SIZE)
                .build();
    }

    @Bean
    @StepScope
    public MongoItemReader<MovementDTO> movementReader(@Value("#{jobParameters[key]}") Long key,
                                                       @Value("#{jobParameters[reprocess_flag]}") String reprocessFlag) {
        List<Integer> subDelList = NumberUtils.getSubDelList(key);
        Integer del = NumberUtils.getDel(key);
        boolean isReprocess = Boolean.TRUE.toString().equals(reprocessFlag);
        logger.info("Step query {}", ReaderUtils.buildMovementsJSONQuery(del, subDelList, isReprocess));
        return new MongoItemReaderBuilder<MovementDTO>()
                .name("movementReader")
                .template(mongoOperations)
                .targetType(MovementDTO.class)
                .sorts(ConfrontaConstants.READER_SORTER)
                .jsonQuery(ReaderUtils.buildMovementsJSONQuery(del, subDelList, isReprocess))
                .pageSize(ConfrontaConstants.CHUNK_SIZE)
                .build();
    }
    
    @Bean
    @StepScope
    public MongoItemReader<MovementDTO> movementDescartadosReader(@Value("#{jobParameters[key]}") Long key,
                                                       @Value("#{jobParameters[reprocess_flag]}") String reprocessFlag) {
        List<Integer> subDelList = NumberUtils.getSubDelList(key);
        Integer del = NumberUtils.getDel(key);
        boolean isReprocess = Boolean.TRUE.toString().equals(reprocessFlag);
        logger.info("Step query {}", ReaderUtils.buildMovementsDescartadoJSONQuery(del, subDelList, isReprocess));
        return new MongoItemReaderBuilder<MovementDTO>()
                .name("movementDescartadosReader")
                .template(mongoOperations)
                .targetType(MovementDTO.class)
                .sorts(ConfrontaConstants.READER_SORTER)
                .jsonQuery(ReaderUtils.buildMovementsDescartadoJSONQuery(del, subDelList, isReprocess))
                .pageSize(ConfrontaConstants.CHUNK_SIZE)
                .build();
    }
    
	@Bean
	@StepScope
	public MongoItemReader<MovementDTO> movementDescartadosAnteriorReader(@Value("#{jobParameters[key]}") Long key,
			@Value("#{jobParameters[reprocess_flag]}") String reprocessFlag) {
		List<Integer> subDelList = NumberUtils.getSubDelList(key);
		Integer del = NumberUtils.getDel(key);
		boolean isReprocess = Boolean.TRUE.toString().equals(reprocessFlag);
		logger.info("Step query {}",
				ReaderUtils.buildMovementsDescartadosAnteriorJSONQuery(del, subDelList, isReprocess));
		return new MongoItemReaderBuilder<MovementDTO>().name("movementDescartadosReader").template(mongoOperations)
				.targetType(MovementDTO.class).sorts(ConfrontaConstants.READER_SORTER)
				.jsonQuery(ReaderUtils.buildMovementsDescartadosAnteriorJSONQuery(del, subDelList, isReprocess))
				.pageSize(ConfrontaConstants.CHUNK_SIZE).build();
	}

	@Bean
	@StepScope
	public MongoItemReader<MovementDTO> movementDescartadosPosteriorReader(@Value("#{jobParameters[key]}") Long key,
			@Value("#{jobParameters[reprocess_flag]}") String reprocessFlag) {
		List<Integer> subDelList = NumberUtils.getSubDelList(key);
		Integer del = NumberUtils.getDel(key);
		boolean isReprocess = Boolean.TRUE.toString().equals(reprocessFlag);
		logger.info("Step query {}",
				ReaderUtils.buildMovementsDescartadosPosteriorJSONQuery(del, subDelList, isReprocess));
		return new MongoItemReaderBuilder<MovementDTO>().name("movementDescartadosReader").template(mongoOperations)
				.targetType(MovementDTO.class).sorts(ConfrontaConstants.READER_SORTER)
				.jsonQuery(ReaderUtils.buildMovementsDescartadosPosteriorJSONQuery(del, subDelList, isReprocess))
				.pageSize(ConfrontaConstants.CHUNK_SIZE).build();
	}

    @Bean
    public Step changeStep() {
        return stepBuilderFactory.get("changeStep")
                .<ChangeDTO, String>chunk(ConfrontaConstants.CHUNK_SIZE)
                .reader(changeReader(ConfrontaConstants.WILL_BE_INJECTED_LONG, ConfrontaConstants.WILL_BE_INJECTED_STRING))
                .listener(changeReaderListener)
                .processor(confrontaChangeProcessor)
                .listener(changeProcessorListener)
                .writer(fileWriter(ConfrontaConstants.WILL_BE_INJECTED_SE, ConfrontaConstants.WILL_BE_INJECTED_LONG))
                .listener(fileWriterListener)
                .listener(stepConfrontaListener)
                .taskExecutor(taskExecutor())
                .build();
    }
    
    @Bean
    public Step changeDescartadosStep() {
        return stepBuilderFactory.get("changeStep")
                .<ChangeDTO, String>chunk(ConfrontaConstants.CHUNK_SIZE)
                .reader(changeReader(ConfrontaConstants.WILL_BE_INJECTED_LONG, ConfrontaConstants.WILL_BE_INJECTED_STRING))
                .processor(confrontaChangeProcessor)
                .writer(fileWriter(ConfrontaConstants.WILL_BE_INJECTED_SE, ConfrontaConstants.WILL_BE_INJECTED_LONG))
                .listener(fileWriterListener)
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    @StepScope
    public MongoItemReader<ChangeDTO> changeReader(@Value("#{jobParameters[key]}") Long key,
                                                   @Value("#{jobParameters[reprocess_flag]}") String reprocessFlag) {
        List<Integer> subDelList = NumberUtils.getSubDelList(key);
        Integer del = NumberUtils.getDel(key);
        boolean isReprocess = Boolean.TRUE.toString().equals(reprocessFlag);
        logger.info("Step query {}", ReaderUtils.buildChangesJSONQuery(del, subDelList, isReprocess));
        return new MongoItemReaderBuilder<ChangeDTO>()
                .name("changeReader")
                .template(mongoOperations)
                .targetType(ChangeDTO.class)
                .sorts(ConfrontaConstants.READER_SORTER)
                .jsonQuery(ReaderUtils.buildChangesJSONQuery(del, subDelList, isReprocess))
                .pageSize(ConfrontaConstants.CHUNK_SIZE)
                .build();
    }
    
    @Bean
    @StepScope
    public MongoItemReader<ChangeDTO> changeDescartadosReader(@Value("#{jobParameters[key]}") Long key,
                                                   @Value("#{jobParameters[reprocess_flag]}") String reprocessFlag) {
        List<Integer> subDelList = NumberUtils.getSubDelList(key);
        Integer del = NumberUtils.getDel(key);
        boolean isReprocess = Boolean.TRUE.toString().equals(reprocessFlag);
        logger.info("Step query {}", ReaderUtils.buildChangesDescartadosJSONQuery(del, subDelList, isReprocess));
        return new MongoItemReaderBuilder<ChangeDTO>()
                .name("changeReader")
                .template(mongoOperations)
                .targetType(ChangeDTO.class)
                .sorts(ConfrontaConstants.READER_SORTER)
                .jsonQuery(ReaderUtils.buildChangesDescartadosJSONQuery(del, subDelList, isReprocess))
                .pageSize(ConfrontaConstants.CHUNK_SIZE)
                .build();
    }
    
	@Bean
	@StepScope
	public MongoItemReader<ChangeDTO> changeDescartadosAnteriorReader(@Value("#{jobParameters[key]}") Long key,
			@Value("#{jobParameters[reprocess_flag]}") String reprocessFlag) {
		List<Integer> subDelList = NumberUtils.getSubDelList(key);
		Integer del = NumberUtils.getDel(key);
		boolean isReprocess = Boolean.TRUE.toString().equals(reprocessFlag);
		logger.info("Step query {}",
				ReaderUtils.buildChangesDescartadosAnteriorJSONQuery(del, subDelList, isReprocess));
		return new MongoItemReaderBuilder<ChangeDTO>().name("changeReader").template(mongoOperations)
				.targetType(ChangeDTO.class).sorts(ConfrontaConstants.READER_SORTER)
				.jsonQuery(ReaderUtils.buildChangesDescartadosAnteriorJSONQuery(del, subDelList, isReprocess))
				.pageSize(ConfrontaConstants.CHUNK_SIZE).build();
	}

	@Bean
	@StepScope
	public MongoItemReader<ChangeDTO> changeDescartadosPosteriorReader(@Value("#{jobParameters[key]}") Long key,
			@Value("#{jobParameters[reprocess_flag]}") String reprocessFlag) {
		List<Integer> subDelList = NumberUtils.getSubDelList(key);
		Integer del = NumberUtils.getDel(key);
		boolean isReprocess = Boolean.TRUE.toString().equals(reprocessFlag);
		logger.info("Step query {}",
				ReaderUtils.buildChangesDescartadosPosteriorJSONQuery(del, subDelList, isReprocess));
		return new MongoItemReaderBuilder<ChangeDTO>().name("changeReader").template(mongoOperations)
				.targetType(ChangeDTO.class).sorts(ConfrontaConstants.READER_SORTER)
				.jsonQuery(ReaderUtils.buildChangesDescartadosPosteriorJSONQuery(del, subDelList, isReprocess))
				.pageSize(ConfrontaConstants.CHUNK_SIZE).build();
	}

    @Bean
    @StepScope
    public FlatFileItemWriter<String> fileWriter(@Value("#{stepExecution}")@Nullable StepExecution stepExecution,
                                                 @Value("#{jobParameters[key]}") Long key) {
        FlatFileItemWriter<String> writer = new FlatFileItemWriterBuilder<String>()
                .name("fileWriter")
                .resource(new FileSystemResource(StringUtils
                        .buildFileName(key, ConfrontaConstants.BASE_PATH_SERVER,ConfrontaConstants.PATH_FTP1)))
                .append(Boolean.TRUE)
                .lineAggregator(lineAggregator())
                .build();
        assert stepExecution != null;
        writer.open(stepExecution.getExecutionContext());
        return writer;
    }
    
    @Bean
    @StepScope
    public FlatFileItemWriter<String> fileDescartadosWriter(@Value("#{stepExecution}")@Nullable StepExecution stepExecution,
                                                 @Value("#{jobParameters[key]}") Long key) {
        FlatFileItemWriter<String> writer = new FlatFileItemWriterBuilder<String>()
                .name("fileWriter")
                .resource(new FileSystemResource(StringUtils
                        .buildFileName(key, ConfrontaConstants.BASE_PATH_SERVER,ConfrontaConstants.PATH_FTP1)))
                .append(Boolean.TRUE)
                .lineAggregator(lineAggregator())
                .build();
        assert stepExecution != null;
        writer.open(stepExecution.getExecutionContext());
        return writer;
    }

    @Bean
    public LineAggregator<String> lineAggregator() {
        DelimitedLineAggregator<String> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(",");
        return lineAggregator;
    }

    @Bean
    public Step footerStep() {
        return stepBuilderFactory.get("footerStep")
                .tasklet(footerTasklet)
                .listener(stepConfrontaListener)
                .build();
    }

    @Bean
    public Step processControlStep() {
        return stepBuilderFactory.get("processControlStep")
                .tasklet(processControlTasklet)
                .build();
    }

    @Bean
    public Step processControlFailedStep() {
        return stepBuilderFactory.get("processControlFailedStep")
                .tasklet(processControlFailedTasklet)
                .build();
    }

    @Bean
    public Step auditStep() {
        return stepBuilderFactory.get("auditStep")
                .tasklet(auditTasklet)
                .build();
    }

    @Bean
    public Step auditFailedStep() {
        return stepBuilderFactory.get("auditFailedStep")
                .tasklet(auditFailedTasklet)
                .build();
    }

    @Bean
    public Step generateFileSuccessStep() {
        return stepBuilderFactory.get("generateFileSuccessStep")
                .tasklet(generateFileSuccessTasklet)
                .build();
    }

    @Bean
    public Step fileControlFailedStep() {
        return stepBuilderFactory.get("fileControlFailedStep")
                .tasklet(fileControlFailedTasklet)
                .build();
    }
        
    @Bean
    public Step backupMovementStep() {
        return stepBuilderFactory.get("backupMovementStep")
                .<MovementDTO, BackupConfrontaDTO>chunk(ConfrontaConstants.CHUNK_SIZE)
                .reader(movementReader(ConfrontaConstants.WILL_BE_INJECTED_LONG, ConfrontaConstants.WILL_BE_INJECTED_STRING))
                .listener(movementReaderListener)
                .processor(backupMovementProcessor)
                .listener(backupMovementProcessorListener)
                .writer(backupWriter())
                .listener(movementWriterListener)
                .listener(stepConfrontaListener)
                .taskExecutor(taskExecutor())
                .build();
    }
    
    @Bean
    public Step backupMovementDescartadosStep() {
        return stepBuilderFactory.get("backupMovementDescartadosStep")
                .<MovementDTO, BackupConfrontaDTO>chunk(ConfrontaConstants.CHUNK_SIZE)
                .reader(movementDescartadosReader(ConfrontaConstants.WILL_BE_INJECTED_LONG, ConfrontaConstants.WILL_BE_INJECTED_STRING))
                .listener(movementReaderListener)
                .processor(backupMovementProcessor)
                .listener(backupMovementProcessorListener)
                .writer(backupDescartadosWriter())
                .listener(movementWriterListener)
                .listener(stepConfrontaListener)
                .taskExecutor(taskExecutor())
                .build();
    }
    
    @Bean
    public Step backupMovementAnteriorDescartadosStep() {
        return stepBuilderFactory.get("backupMovementAnteriorDescartadosStep")
                .<MovementDTO, BackupConfrontaDTO>chunk(ConfrontaConstants.CHUNK_SIZE)
                .reader(movementDescartadosAnteriorReader(ConfrontaConstants.WILL_BE_INJECTED_LONG, ConfrontaConstants.WILL_BE_INJECTED_STRING))
                .listener(movementReaderListener)
                .processor(backupMovementProcessor)
                .listener(backupMovementProcessorListener)
                .writer(backupDescartadosWriter())
                .listener(movementWriterListener)
                .listener(stepConfrontaListener)
                .taskExecutor(taskExecutor())
                .build();
    }
    
    @Bean
    public Step backupMovementPosteriorDescartadosStep() {
        return stepBuilderFactory.get("backupMovementDescartadosStep")
                .<MovementDTO, BackupConfrontaDTO>chunk(ConfrontaConstants.CHUNK_SIZE)
                .reader(movementDescartadosPosteriorReader(ConfrontaConstants.WILL_BE_INJECTED_LONG, ConfrontaConstants.WILL_BE_INJECTED_STRING))
                .listener(movementReaderListener)
                .processor(backupMovementProcessor)
                .listener(backupMovementProcessorListener)
                .writer(backupDescartadosWriter())
                .listener(movementWriterListener)
                .listener(stepConfrontaListener)
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public Step backupChangeStep() {
        return stepBuilderFactory.get("backupChangeStep")
                .<ChangeDTO, BackupConfrontaDTO>chunk(ConfrontaConstants.CHUNK_SIZE)
                .reader(changeReader(ConfrontaConstants.WILL_BE_INJECTED_LONG, ConfrontaConstants.WILL_BE_INJECTED_STRING))
                .listener(changeReaderListener)
                .processor(backupChangeProcessor)
                .listener(backupChangeProcessorListener)
                .writer(backupWriter())
                .listener(changeWriterListener)
                .listener(stepConfrontaListener)
                .taskExecutor(taskExecutor())
                .build();
    }
    
    @Bean
    public Step backupDescartadosChangeStep() {
        return stepBuilderFactory.get("backupDescartadosChangeStep")
                .<ChangeDTO, BackupConfrontaDTO>chunk(ConfrontaConstants.CHUNK_SIZE)
                .reader(changeDescartadosReader(ConfrontaConstants.WILL_BE_INJECTED_LONG, ConfrontaConstants.WILL_BE_INJECTED_STRING))
                .listener(changeReaderListener)
                .processor(backupChangeProcessorDescartados)
                .listener(backupChangeProcessorListener)
                .writer(backupDescartadosWriter())
                .listener(changeWriterListener)
                .listener(stepConfrontaListener)
                .taskExecutor(taskExecutor())
                .build();
    }
    
    @Bean
    public Step backupDescartadosAnteriorChangeStep() {
        return stepBuilderFactory.get("backupDescartadosChangeStep")
                .<ChangeDTO, BackupConfrontaDTO>chunk(ConfrontaConstants.CHUNK_SIZE)
                .reader(changeDescartadosAnteriorReader(ConfrontaConstants.WILL_BE_INJECTED_LONG, ConfrontaConstants.WILL_BE_INJECTED_STRING))
                .listener(changeReaderListener)
                .processor(backupChangeProcessorDescartados)
                .listener(backupChangeProcessorListener)
                .writer(backupDescartadosWriter())
                .listener(changeWriterListener)
                .listener(stepConfrontaListener)
                .taskExecutor(taskExecutor())
                .build();
    }
    
    @Bean
    public Step backupDescartadosPosteriorChangeStep() {
        return stepBuilderFactory.get("backupDescartadosChangeStep")
                .<ChangeDTO, BackupConfrontaDTO>chunk(ConfrontaConstants.CHUNK_SIZE)
                .reader(changeDescartadosPosteriorReader(ConfrontaConstants.WILL_BE_INJECTED_LONG, ConfrontaConstants.WILL_BE_INJECTED_STRING))
                .listener(changeReaderListener)
                .processor(backupChangeProcessorDescartados)
                .listener(backupChangeProcessorListener)
                .writer(backupDescartadosWriter())
                .listener(changeWriterListener)
                .listener(stepConfrontaListener)
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean MongoItemWriter<BackupConfrontaDTO> backupWriter() {
        return new MongoItemWriterBuilder<BackupConfrontaDTO>()
                .template(mongoOperations)
                .collection("MCB_RESPALDO_CIERRE_ANUAL_CASUISTICA")
                .build();
    }
    
    @Bean MongoItemWriter<BackupConfrontaDTO> backupDescartadosWriter() {
        return new MongoItemWriterBuilder<BackupConfrontaDTO>()
                .template(mongoOperations)
                .collection("MCB_RESPALDO_RIESGOS_DESCARTADOS")
                .build();
    }

    @Bean
    public Step rollBackBackupStep() {
        return stepBuilderFactory.get("rollBackBackupStep")
                .tasklet(rollBackTasklet)
                .build();
    }

    @Bean
    public Step sftpUploadFileStep() {
        return stepBuilderFactory.get("sftpUploadFileStep")
                .tasklet(sftpUploadFileTasklet)
                .listener(stepConfrontaListener)
                .build();
    }

    @Bean("confrontaNextJob")
    public Job confrontaNextJob() {
        return jobBuilderFactory.get("confrontaNextJob")
                .incrementer(new RunIdIncrementer())
                .listener(jobConfrontaListener)
                .start(emailOOADStep())
                .next(fileValidationStep())
                .on(ExitStatus.COMPLETED.getExitCode())
                .to(validationSuccessFlow())
                .from(fileValidationStep())
                .on(ExitStatusConfronta.GENERATION_FILE_FAILED.getExitCode())
                .to(validationFailedFlow())
                .end()
                .build();
    }    

    @Bean
    public Step emailOOADStep() {
        return stepBuilderFactory.get("emailOOADStep")
                .tasklet(emailOOADTasklet)
                .build();
    }

    @Bean
    public Step fileValidationStep() {
        return stepBuilderFactory.get("fileValidationStep")
                .tasklet(fileValidationTasklet)
                .listener(stepConfrontaListener)
                .build();
    }

    @Bean
    public Flow validationSuccessFlow() {
        return new FlowBuilder<Flow>("validationSuccessFlow")
                .start(processControlStep())
                .next(uploadFilesFlow())
                .end();
    }

    @Bean
    public Flow validationFailedFlow() {
        return new FlowBuilder<Flow>("validationFailedFlow")
                .start(processControlFailedStep())
                .next(auditFailedStep())
                .next(uploadFilesFlow())
                .end();
    }

    @Bean
    public Flow uploadFilesFlow() {
        return new FlowBuilder<Flow>("validationSuccessFlow")
                .start(sftpUploadFileStep())
                .next(fileValidationStep())
                .on(ExitStatus.COMPLETED.getExitCode())
                .to(uploadSuccessFlow())
                .from(fileValidationStep())
                .on(ExitStatusConfronta.UPLOAD_SFTP1_FAILED.getExitCode())
                .to(uploadFailedFlow())
                .end();
    }

    @Bean
    public Flow uploadSuccessFlow() {
        return new FlowBuilder<Flow>("uploadSuccessFlow")
                .start(processControlStep())
                .next(auditStep())
                .next(bankFilesFlow())
                .end();
    }

    @Bean
    public Flow uploadFailedFlow() {
        return new FlowBuilder<Flow>("uploadFailedFlow")
                .start(processControlFailedStep())
                .next(auditFailedStep())
                .next(bankFilesFlow())
                .end();
    }

    @Bean
    public Flow bankFilesFlow() {
        return new FlowBuilder<Flow>("uploadSuccessFlow")
                .start(sftpUploadFileStep())
                .next(fileValidationStep())
                .on(ExitStatus.COMPLETED.getExitCode())
                .to(bankSuccessFlow())
                .from(fileValidationStep())
                .on(ExitStatusConfronta.UPLOAD_SFTP2_FAILED.getExitCode())
                .to(bankFailedFlow())
                .build();
    }

    @Bean
    public Flow bankSuccessFlow() {
        return new FlowBuilder<Flow>("bankSuccessFlow")
                .start(processControlStep())
                .next(auditStep())
                .next(emailStep())
                .next(successProcessValidationStep())
                .on(ExitStatus.COMPLETED.getExitCode())
                .to(processSuccessFlow())
                .build();
    }

    @Bean
    public Flow bankFailedFlow() {
        return new FlowBuilder<Flow>("bankFailedFlow")
                .start(processControlFailedStep())
                .next(auditFailedStep())
                .end();
    }

    @Bean
    public Step emailStep() {
        return stepBuilderFactory.get("emailStep")
                .tasklet(emailTasklet)
                .build();
    }
    
    @Bean
    public Step emailStepOne() {
    	return stepBuilderFactory.get("emailStepOne")
    			.tasklet(emailStepOneTasklet)
    			.build();
    }
    
    @Bean
    public Step emailStepTwo() {
    	return stepBuilderFactory.get("emailStepTwo")
    			.tasklet(emailStepTwoTasklet)
    			.build();
    }
    
    @Bean
    public Step emailStepThree() {
    	return stepBuilderFactory.get("emailStepThree")
    			.tasklet(emailStepThreeTasklet)
    			.build();
    }
    
    @Bean
    public Step emailStepFour() {
    	return stepBuilderFactory.get("emailStepFour")
    			.tasklet(emailStepFourTasklet)
    			.build();
    }
    
    @Bean
    public Step emailStepFive() {
    	return stepBuilderFactory.get("emailStepFive")
    			.tasklet(emailStepFiveTasklet)
    			.build();
    }

    @Bean
    public Step successProcessValidationStep() {
        return stepBuilderFactory.get("successProcessValidationStep")
                .tasklet(successProcessValidatorTasklet)
                .listener(stepConfrontaListener)
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public Flow processSuccessFlow() {
        return new FlowBuilder<Flow>("processSuccessFlow")
                .start(processControlStep())
                .build();
    }

    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(ConfrontaConstants.POOL_SIZE);
        taskExecutor.setMaxPoolSize(ConfrontaConstants.POOL_SIZE);
        taskExecutor.afterPropertiesSet();
        return taskExecutor;
    }

}
