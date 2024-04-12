package cloud.notebook.transcription.service.aws.sqs.listeners;

import cloud.notebook.transcription.service.basis.Transcription;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class AudioTranscriptionQueueListener {

    private final Transcription audioTranscription;

    @Autowired
    public AudioTranscriptionQueueListener(@NonNull Transcription audioTranscription) {
        this.audioTranscription = audioTranscription;
    }

    @SqsListener("${notebook.transcription.sqs.name:audio-transcription-sqs}")
    public void listenAudioToTranslate(@NonNull String audioId) {
        LOG.info("Audio id '{}' received to transcript", audioId);

        audioTranscription.apply(audioId);

        LOG.info("Audio id '{}' has been transcripted", audioId);
    }
}
