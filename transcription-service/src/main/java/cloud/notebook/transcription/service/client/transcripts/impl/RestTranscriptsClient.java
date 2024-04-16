package cloud.notebook.transcription.service.client.transcripts.impl;

import cloud.notebook.transcription.service.client.transcripts.TranscriptsClient;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Log4j2
public class RestTranscriptsClient implements TranscriptsClient {

    private final RestClient restClient;
    private final String endpoint;

    public RestTranscriptsClient(@NonNull RestClient restClient, @NonNull String endpoint) {
        this.restClient = restClient;
        this.endpoint = endpoint;
    }

    @Override
    public void put(@NonNull String id, byte[] data) {
        LOG.info("Uploading transcript with id: {}", id);

        restClient.put()
                .uri(UriComponentsBuilder.fromHttpUrl(endpoint)
                        .path("/transcript/{uuid}")
                        .build(id))
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(createBody(data))
                .retrieve();
    }

    private MultiValueMap<String, Object> createBody(byte[] data) {
        MultiValueMap<String, Object> bodyData = new LinkedMultiValueMap<>(3);
        bodyData.add("file", toByteArrayResource(data));

        return bodyData;
    }

    private ByteArrayResource toByteArrayResource(byte[] data) {
        return new ByteArrayResource(data) {
            @Override
            public String getFilename() {
                return "transcript.txt"; // Filename has to be returned in order to be able to post.
            }
        };
    }
}
