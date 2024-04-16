package cloud.notebook.transcript.service.aws.s3;

import lombok.NonNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "notebook.transcript.service.s3")
public record S3Properties(@NonNull @DefaultValue("audio-resources") String bucket,
                           @NonNull @DefaultValue("/transcripts") String prefix) {
}
