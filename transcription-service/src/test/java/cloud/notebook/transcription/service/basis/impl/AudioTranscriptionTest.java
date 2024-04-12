package cloud.notebook.transcription.service.basis.impl;


import cloud.notebook.transcription.service.basis.storage.audio.AudioStorage;
import cloud.notebook.transcription.service.basis.storage.transcript.TranscriptStorage;
import cloud.notebook.transcription.service.client.ai.AIClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AudioTranscriptionTest {

    @Mock
    private AudioStorage audioStorageMock;
    @Mock
    private AIClient aiClientMock;
    @Mock
    private TranscriptStorage transcriptStorageMock;

    @InjectMocks
    private AudioTranscription sut;

    @Test
    void shouldFailOnNullInput() {
        assertThrows(IllegalArgumentException.class, () -> sut.apply(null));
    }

    @Test
    void shouldGetAudioAndTranscriptAndSave() {
        String id = "some-id";
        byte[] data = "data".getBytes();
        String text = "transcript text";

        when(audioStorageMock.get(id)).thenReturn(data);
        when(aiClientMock.transcribe(data)).thenReturn(text);

        sut.apply(id);

        verify(audioStorageMock, times(1)).get(id);
        verify(aiClientMock, times(1)).transcribe(data);
        verify(transcriptStorageMock, times(1)).save(id, text);
    }

    @Test
    void shouldSkipTranscribeAndSaveIfDataNull() {
        String id = "some-id";

        when(audioStorageMock.get(id)).thenReturn(null);

        sut.apply(id);

        verify(audioStorageMock, times(1)).get(id);
        verify(aiClientMock, times(0)).transcribe(any(byte[].class));
        verify(transcriptStorageMock, times(0)).save(eq(id), anyString());
    }

    @Test
    void shouldSkipTranscribeAndSaveIfDataEmpty() {
        String id = "some-id";
        byte[] data = new byte[0];

        when(audioStorageMock.get(id)).thenReturn(data);

        sut.apply(id);

        verify(audioStorageMock, times(1)).get(id);
        verify(aiClientMock, times(0)).transcribe(any(byte[].class));
        verify(transcriptStorageMock, times(0)).save(eq(id), anyString());
    }

    @Test
    void shouldSkipSaveIfTranscriptNull() {
        String id = "some-id";
        byte[] data = "data".getBytes();

        when(audioStorageMock.get(id)).thenReturn(data);
        when(aiClientMock.transcribe(data)).thenReturn(null);

        sut.apply(id);

        verify(audioStorageMock, times(1)).get(id);
        verify(aiClientMock, times(1)).transcribe(data);
        verify(transcriptStorageMock, times(0)).save(id, null);
    }

    @Test
    void shouldSkipSaveIfTranscriptBlank() {
        String id = "some-id";
        byte[] data = "data".getBytes();
        String text = " ";

        when(audioStorageMock.get(id)).thenReturn(data);
        when(aiClientMock.transcribe(data)).thenReturn(null);

        sut.apply(id);

        verify(audioStorageMock, times(1)).get(id);
        verify(aiClientMock, times(1)).transcribe(data);
        verify(transcriptStorageMock, times(0)).save(eq(id), anyString());
    }
}