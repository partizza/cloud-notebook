package cloud.notebook.audio.service.storage.impl;

import cloud.notebook.audio.service.aws.sns.StoredAudioSnsService;
import cloud.notebook.audio.service.storage.StorageService;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
public class StoredAudioNotificationStorage implements StorageService {

    private final StorageService storageDelegate;
    private final StoredAudioSnsService snsService;

    @Autowired
    public StoredAudioNotificationStorage(@NonNull @Qualifier("commonAudioStorage") StorageService storageDelegate,
                                          @NonNull StoredAudioSnsService snsService) {
        this.storageDelegate = storageDelegate;
        this.snsService = snsService;
    }

    @Override
    public String add(byte[] data) {
        LOG.debug("About to store audio with notification");
        String id = storageDelegate.add(data);
        snsService.sendId(id);
        return id;
    }

    @Override
    public Optional<byte[]> get(@NonNull String uuid) {
        return storageDelegate.get(uuid);
    }
}
