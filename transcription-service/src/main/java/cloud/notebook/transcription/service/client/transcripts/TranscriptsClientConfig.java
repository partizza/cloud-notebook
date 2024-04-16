package cloud.notebook.transcription.service.client.transcripts;

import cloud.notebook.transcription.service.client.transcripts.impl.RestTranscriptsClient;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class TranscriptsClientConfig {

    @Bean
    public TranscriptsClient transcriptsClient(@NonNull @Value("${notebook.transcript.service.endpoint}") String endpoint) {
        return new RestTranscriptsClient(RestClient.create(), endpoint);
    }
}
