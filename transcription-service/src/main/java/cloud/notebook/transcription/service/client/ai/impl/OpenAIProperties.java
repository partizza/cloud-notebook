package cloud.notebook.transcription.service.client.ai.impl;

import lombok.NonNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "notebook.openai")
public record OpenAIProperties(@NonNull @DefaultValue("https://api.openai.com/v1/audio/transcriptions") String uri,
                               @NonNull String key,
                               @NonNull @DefaultValue("whisper-1") String model) {
}
