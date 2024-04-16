package cloud.notebook.transcription.service.basis.storage.transcript.impl;

import cloud.notebook.transcription.service.basis.storage.transcript.TranscriptStorage;
import cloud.notebook.transcription.service.client.transcripts.TranscriptsClient;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class CommonTranscriptStorage implements TranscriptStorage {

    private final TranscriptsClient transcriptsClient;

    @Autowired
    public CommonTranscriptStorage(@NonNull TranscriptsClient transcriptsClient) {
        this.transcriptsClient = transcriptsClient;
    }

    @Override
    public void save(@NonNull String id, String text) {
        LOG.info("Storing transcript of '{}': {}", id, text);
        transcriptsClient.put(id, text.getBytes());
    }
}
