package cloud.notebook.transcription.service.basis.storage.audio.impl;


import cloud.notebook.transcription.service.client.audio.AudioClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommonAudioStorageTest {

    @Mock
    private AudioClient audioClientMock;

    @InjectMocks
    private CommonAudioStorage sut;

    @Test
    void shouldFailGetOnNullInput() {
        assertThrows(IllegalArgumentException.class, () -> sut.get(null));
    }

    @Test
    void shouldGetById() {
        String id = "id-1-2";
        byte[] data = "some data".getBytes();

        when(audioClientMock.get(id)).thenReturn(data);

        byte[] result = sut.get(id);

        assertArrayEquals(data, result);
    }
}