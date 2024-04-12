package cloud.notebook.transcription.service.basis.storage.transcript.impl;

import cloud.notebook.transcription.service.basis.storage.transcript.TranscriptStorage;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class CommonTranscriptStorage implements TranscriptStorage {

    @Override
    public void save(@NonNull String id, String text) {
        LOG.warn("Transcript persisting is not implemented yet. Id:{}. Text:{}", id, text);
    }
}
