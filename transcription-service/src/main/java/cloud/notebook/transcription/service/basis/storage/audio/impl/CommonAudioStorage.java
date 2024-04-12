package cloud.notebook.transcription.service.basis.storage.audio.impl;

import cloud.notebook.transcription.service.basis.storage.audio.AudioStorage;
import cloud.notebook.transcription.service.client.audio.AudioClient;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class CommonAudioStorage implements AudioStorage {

    private final AudioClient audioClient;

    @Autowired
    public CommonAudioStorage(@NonNull AudioClient audioClient) {
        this.audioClient = audioClient;
    }

    @Override
    public byte[] get(@NonNull String id) {
        LOG.debug("Retrieving audio by id:{}", id);
        return audioClient.get(id);
    }
}
