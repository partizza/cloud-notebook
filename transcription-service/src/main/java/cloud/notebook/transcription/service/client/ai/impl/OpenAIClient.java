package cloud.notebook.transcription.service.client.ai.impl;

import cloud.notebook.transcription.service.client.ai.AIClient;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;


@Log4j2
public class OpenAIClient implements AIClient {

    private final RestClient restClient;
    private final OpenAIProperties props;

    public OpenAIClient(@NonNull RestClient restClient, @NonNull OpenAIProperties props) {
        this.restClient = restClient;
        this.props = props;
    }

    @Override
    public String transcribe(byte[] audio) {
        return restClient.post()
                .uri(props.uri())
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", props.key()))
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(createBody(audio))
                .retrieve()
                .toEntity(String.class)
                .getBody();
    }

    private MultiValueMap<String, Object> createBody(byte[] audio) {
        MultiValueMap<String, Object> bodyData = new LinkedMultiValueMap<>(3);
        bodyData.add("file", toByteArrayResource(audio));
        bodyData.add("model", props.model());
        bodyData.add("response_format", "text");

        return bodyData;
    }

    private ByteArrayResource toByteArrayResource(byte[] data) {
        return new ByteArrayResource(data) {
            @Override
            public String getFilename() {
                return "data.mp3"; // Filename has to be returned in order to be able to post.
            }
        };
    }
}
