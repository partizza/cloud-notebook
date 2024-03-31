package cloud.notebook.audio.service.aws.s3;

import lombok.NonNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "notebook.audio.service.s3")
public record S3Properties(@NonNull @DefaultValue("audio-resources") String bucket,
                           @NonNull @DefaultValue("/audio") String prefix) {
}
