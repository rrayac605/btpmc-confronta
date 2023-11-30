package mx.gob.imss.cit.pmc.confronta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication()
@EnableScheduling
public class BatchConfrontaApplication {

    public static void main(String[] args) {
        SpringApplication.run(BatchConfrontaApplication.class, args);
    }

}
