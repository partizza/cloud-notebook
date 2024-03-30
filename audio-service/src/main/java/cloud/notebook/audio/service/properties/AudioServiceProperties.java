package cloud.notebook.audio.service.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "notebook.audio.service")
public record AudioServiceProperties(S3Properties s3, StoredAudioSnsProperties storedAudioTopic) {
}
