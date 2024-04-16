package cloud.notebook.transcription.service.client.transcripts.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.web.client.MockServerRestClientCustomizer;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest
@AutoConfigureMockRestServiceServer(enabled = false)
class RestTranscriptsClientTest {

    private static final String ENDPOINT = "http://host:88";

    @Autowired
    private RestClient.Builder clientBuilder;

    private MockRestServiceServer server;

    private RestTranscriptsClient sut;

    @BeforeEach
    void init() {
        MockServerRestClientCustomizer mockServerCustomizer = new MockServerRestClientCustomizer();
        mockServerCustomizer.customize(clientBuilder);
        server = mockServerCustomizer.getServer(clientBuilder);

        sut = new RestTranscriptsClient(clientBuilder.build(), ENDPOINT);
    }

    @Test
    void shouldFailPutOnNull() {
        assertThrows(IllegalArgumentException.class, () -> sut.put(null, new byte[0]));
    }

    @Test
    void shouldUpload() {
        String id = "id-13";
        byte[] data = "some text data".getBytes();


        MultiValueMap<String, Object> multipart = new LinkedMultiValueMap<>(3);
        multipart.add("file",  new ByteArrayResource(data) {
            @Override
            public String getFilename() {
                return "transcript.txt"; // Filename has to be returned in order to be able to post.
            }
        });

        server.expect(requestTo(UriComponentsBuilder.fromHttpUrl(ENDPOINT)
                        .path("/transcript/{uuid}")
                        .build(id)))
                .andExpect(content().multipartData(multipart))
                .andRespond(withSuccess());

        sut.put(id, data);
    }

}