package cloud.notebook.transcription.service.client.audio.impl;

import cloud.notebook.transcription.service.client.audio.AudioClient;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Log4j2
public class RestAudioClient implements AudioClient {

    private final RestClient restClient;
    private final String endpoint;

    public RestAudioClient(@NonNull RestClient client, @NonNull String endpoint) {
        this.restClient = client;
        this.endpoint = endpoint;
    }

    @Override
    public byte[] get(@NonNull String id) {
        LOG.info("Requesting audio by id: {}", id);

        return restClient.get()
                .uri(UriComponentsBuilder.fromHttpUrl(endpoint)
                        .path("/audio/{uuid}")
                        .build(id))
                .accept(MediaType.APPLICATION_OCTET_STREAM)
                .retrieve()
                .body(byte[].class);
    }
}
