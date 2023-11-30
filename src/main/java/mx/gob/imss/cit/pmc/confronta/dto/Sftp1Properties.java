package mx.gob.imss.cit.pmc.confronta.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "sftp1")
@Getter
@Setter
public class Sftp1Properties {

    private String host;
    private Integer port;
    private String user;
    private String pass;
    private String path;

}
