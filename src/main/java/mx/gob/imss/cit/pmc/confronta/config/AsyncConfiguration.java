package mx.gob.imss.cit.pmc.confronta.config;

import mx.gob.imss.cit.pmc.confronta.constants.ConfrontaConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfiguration {

    @Bean(name = "taskExecutorController")
    public Executor taskExecutorController() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(ConfrontaConstants.POOL_SIZE);
        taskExecutor.setMaxPoolSize(ConfrontaConstants.POOL_SIZE);
        taskExecutor.afterPropertiesSet();
        return taskExecutor;
    }

}
