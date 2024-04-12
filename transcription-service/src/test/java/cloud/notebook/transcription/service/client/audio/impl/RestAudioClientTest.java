package cloud.notebook.transcription.service.client.audio.impl;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.web.client.MockServerRestClientCustomizer;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;


import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest
@AutoConfigureMockRestServiceServer(enabled = false)
class RestAudioClientTest {

    private static final String ENDPOINT = "http://endpoint";

    private MockRestServiceServer server;

    @Autowired
    private RestClient.Builder clientBuilder;

    private RestAudioClient sut;

    @BeforeEach
    void init() {
        MockServerRestClientCustomizer mockServerCustomizer = new MockServerRestClientCustomizer();
        mockServerCustomizer.customize(clientBuilder);
        server = mockServerCustomizer.getServer(clientBuilder);

        sut = new RestAudioClient(clientBuilder.build(), ENDPOINT);
    }

    @Test
    void shouldFailOnNull() {
        assertThrows(IllegalArgumentException.class, () -> sut.get(null));
    }

    @Test
    void shouldGetById() {
        String id = "some-id";
        byte[] data = "expected".getBytes();

        server.expect(requestTo(String.format("%s/audio/%s", ENDPOINT, id)))
                .andRespond(withSuccess(data, MediaType.APPLICATION_OCTET_STREAM));

        byte[] result = sut.get(id);

        assertArrayEquals(data, result);
    }
}