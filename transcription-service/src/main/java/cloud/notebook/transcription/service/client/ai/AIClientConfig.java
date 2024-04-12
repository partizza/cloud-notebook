package cloud.notebook.transcription.service.client.ai;

import cloud.notebook.transcription.service.client.ai.impl.OpenAIClient;
import cloud.notebook.transcription.service.client.ai.impl.OpenAIProperties;
import lombok.NonNull;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@EnableConfigurationProperties(OpenAIProperties.class)
public class AIClientConfig {

    @Bean
    public AIClient openAIClient(@NonNull OpenAIProperties props) {
        return new OpenAIClient(RestClient.create(), props);
    }
}
