package cloud.notebook.transcription.service.aws.sqs.listeners;

import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class AudioTranscriptionQueueListener {

    @SqsListener("${notebook.transcription.sqs.name:audio-transcription-sqs}")
    public void listenAudioToTranslate(@NonNull String audioId) {
        LOG.info("Audio id '{}' received to translate", audioId);
    }
}
