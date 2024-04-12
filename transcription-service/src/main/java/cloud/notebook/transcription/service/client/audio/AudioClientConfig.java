package cloud.notebook.transcription.service.client.audio;

import cloud.notebook.transcription.service.client.audio.impl.RestAudioClient;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class AudioClientConfig {

    @Bean
    public AudioClient audioClient(@NonNull @Value("${notebook.audio.service.endpoint}") String endpoint) {
        return new RestAudioClient(RestClient.create(), endpoint);
    }
}
