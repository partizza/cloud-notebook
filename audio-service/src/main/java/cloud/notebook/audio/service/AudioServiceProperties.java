package cloud.notebook.audio.service;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "notebook.audio.service")
public record AudioServiceProperties(S3Properties s3) {
}
