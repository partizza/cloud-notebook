package cloud.notebook.transcript.service.controllers;


import cloud.notebook.transcript.service.aws.s3.S3Service;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(TranscriptController.class)
@ExtendWith(MockitoExtension.class)
class TranscriptControllerTest {

    @MockBean
    private S3Service s3ServiceMock;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldPut() throws Exception {
        String uuid = "uu-id";
        byte[] data = "data".getBytes();
        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt", "text/plain", data);

        mockMvc.perform(multipart(HttpMethod.PUT, String.format("/transcript/%s", uuid))
                        .file(multipartFile))
                .andExpect(status().isOk());

        Mockito.verify(s3ServiceMock, times(1)).put(eq(uuid), argThat(it -> Arrays.equals(data, it)));
    }

    @Test
    void shouldThrowFileUploadException() throws Exception {
        String uuid = "uu-id";
        String uri = String.format("/transcript/%s", uuid);

        MockMultipartFile multipartFileMock = mock(MockMultipartFile.class);
        when(multipartFileMock.getName()).thenReturn("file");
        when(multipartFileMock.getOriginalFilename()).thenReturn("origin_name.txt");
        when(multipartFileMock.getInputStream()).thenThrow(IOException.class);

        mockMvc.perform(multipart(HttpMethod.PUT, uri)
                        .file(multipartFileMock))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.path", is(uri)))
                .andExpect(jsonPath("$.error", notNullValue()))
                .andExpect(jsonPath("$.timestamp", notNullValue()));
    }

    @Test
    void shouldGetBytesByUUID() throws Exception {
        String uuid = UUID.randomUUID().toString();
        String uri = String.format("/transcript/%s", uuid);
        byte[] data = "data".getBytes();

        when(s3ServiceMock.get(uuid)).thenReturn(Optional.of(data));

        mockMvc.perform(get(uri))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(content().bytes(data));
    }

    @Test
    void shouldGetNotFoundByUuid() throws Exception {
        String uuid = UUID.randomUUID().toString();

        when(s3ServiceMock.get(uuid)).thenReturn(Optional.empty());

        mockMvc.perform(get(String.format("/transcript/%s", uuid)))
                .andExpect(status().isNotFound());
    }
}