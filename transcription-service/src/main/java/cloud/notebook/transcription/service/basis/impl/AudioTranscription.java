package cloud.notebook.transcription.service.basis.impl;

import cloud.notebook.transcription.service.basis.Transcription;
import cloud.notebook.transcription.service.basis.storage.audio.AudioStorage;
import cloud.notebook.transcription.service.basis.storage.transcript.TranscriptStorage;
import cloud.notebook.transcription.service.client.ai.AIClient;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log4j2
public class AudioTranscription implements Transcription {

    private final AudioStorage audioStorage;
    private final AIClient aiClient;
    private final TranscriptStorage transcriptStorage;

    @Autowired
    public AudioTranscription(@NonNull AudioStorage audioStorage,
                              @NonNull AIClient aiClient,
                              @NonNull TranscriptStorage transcriptStorage) {
        this.audioStorage = audioStorage;
        this.aiClient = aiClient;
        this.transcriptStorage = transcriptStorage;
    }

    @Override
    public void apply(@NonNull String id) {
        Optional.ofNullable(audioStorage.get(id))
                .filter(bytes -> bytes.length > 0)
                .map(aiClient::transcribe)
                .filter(text -> !text.isBlank())
                .ifPresent(text -> transcriptStorage.save(id, text));
    }
}
