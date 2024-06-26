package cloud.notebook.audio.service;

import cloud.notebook.audio.service.aws.s3.S3Properties;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@Log4j2
@SpringBootApplication
@EnableConfigurationProperties(S3Properties.class)
public class AudioServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AudioServiceApplication.class, args);
    }

}
