package cloud.notebook.audio.service.controllers;


import cloud.notebook.audio.service.storage.StorageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AudioResourcesController.class)
class AudioResourcesControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean(name = "storedAudioNotificationStorage")
    private StorageService storageServiceMock;

    @Test
    void shouldUpload() throws Exception {

        String expected_uuid = UUID.randomUUID().toString();

        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.mp3", "audio/mp3", "data".getBytes());
        when(storageServiceMock.add(multipartFile.getBytes())).thenReturn(expected_uuid);

        mockMvc.perform(multipart("/audio")
                        .file(multipartFile))
                .andExpect(status().isOk())
                .andExpect(content().string(expected_uuid));
    }

    @Test
    void shouldThrowFileUploadException() throws Exception {

        MockMultipartFile multipartFileMock = mock(MockMultipartFile.class);
        when(multipartFileMock.getName()).thenReturn("file");
        when(multipartFileMock.getOriginalFilename()).thenReturn("origin_name.mp3");
        when(multipartFileMock.getInputStream()).thenThrow(IOException.class);

        mockMvc.perform(multipart("/audio")
                        .file(multipartFileMock))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.path", is("/audio")))
                .andExpect(jsonPath("$.error", notNullValue()))
                .andExpect(jsonPath("$.timestamp", notNullValue()));
    }

    @Test
    void shouldGetBytesByUUID() throws Exception {
        String uuid = UUID.randomUUID().toString();
        byte[] data = "data".getBytes();

        when(storageServiceMock.get(uuid)).thenReturn(Optional.of(data));

        mockMvc.perform(get("/audio/{uuid}", uuid))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(content().bytes(data));
    }

    @Test
    void shouldGetNotFoundByUuid() throws Exception {
        String uuid = UUID.randomUUID().toString();

        when(storageServiceMock.get(uuid)).thenReturn(Optional.empty());

        mockMvc.perform(get("/audio/{uuid}", uuid))
                .andExpect(status().isNotFound());
    }
}