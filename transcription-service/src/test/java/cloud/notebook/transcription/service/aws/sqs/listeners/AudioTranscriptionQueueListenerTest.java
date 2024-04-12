package cloud.notebook.transcription.service.aws.sqs.listeners;


import cloud.notebook.transcription.service.basis.Transcription;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AudioTranscriptionQueueListenerTest {

    @Mock
    private Transcription transcriptionMock;

    @InjectMocks
    private AudioTranscriptionQueueListener sut;

    @Test
    void shouldFailOnNullInput() {
        assertThrows(IllegalArgumentException.class, () -> sut.listenAudioToTranslate(null));
    }

    @Test
    void shouldProcess() {
        String id = "i-d";

        sut.listenAudioToTranslate(id);

        verify(transcriptionMock, times(1)).apply(id);
    }
}