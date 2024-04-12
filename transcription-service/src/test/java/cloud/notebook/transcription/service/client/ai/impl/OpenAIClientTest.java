package cloud.notebook.transcription.service.client.ai.impl;


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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest
@AutoConfigureMockRestServiceServer(enabled = false)
class OpenAIClientTest {

    private static final OpenAIProperties PROPS = new OpenAIProperties("https://openai:88", "some-key", "model-ok");
    @Autowired
    private RestClient.Builder clientBuilder;
    private MockRestServiceServer server;
    private OpenAIClient sut;

    @BeforeEach
    void init() {
        MockServerRestClientCustomizer mockServerCustomizer = new MockServerRestClientCustomizer();
        mockServerCustomizer.customize(clientBuilder);
        server = mockServerCustomizer.getServer(clientBuilder);

        sut = new OpenAIClient(clientBuilder.build(), PROPS);
    }

    @Test
    void shouldTranscribe() {
        byte[] data = "some audio data".getBytes();
        String expectedResult = "transcription for audio";

        MultiValueMap<String, Object> multipart = new LinkedMultiValueMap<>(3);
        multipart.add("file",  new ByteArrayResource(data) {
            @Override
            public String getFilename() {
                return "data.mp3"; // Filename has to be returned in order to be able to post.
            }
        });
        multipart.add("model", PROPS.model());
        multipart.add("response_format", "text");

        server.expect(requestTo(PROPS.uri()))
                .andExpect(header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", PROPS.key())))
                .andExpect(content().multipartData(multipart))
                .andRespond(withSuccess(expectedResult,MediaType.TEXT_PLAIN));


        String result = sut.transcribe(data);

        assertEquals(expectedResult, result);
    }
}