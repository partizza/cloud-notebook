package cloud.notebook.audio.service.storage.impl;

import cloud.notebook.audio.service.aws.s3.S3Service;
import cloud.notebook.audio.service.storage.S3ReferenceService;
import cloud.notebook.audio.service.storage.StorageService;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Log4j2
@Service
public class CommonAudioStorage implements StorageService {

    private final S3ReferenceService s3Reference;
    private final S3Service s3;

    public CommonAudioStorage(@NonNull S3ReferenceService s3Reference, @NonNull S3Service s3) {
        this.s3Reference = s3Reference;
        this.s3 = s3;
    }

    @Override
    public String add(byte[] data) {
        String uuid = UUID.randomUUID().toString();
        String s3Key = s3Reference.generate(uuid);
        s3.put(s3Key, data);

        LOG.info("New audio stored, uuid: {}", uuid);
        return uuid;
    }

    @Override
    public Optional<byte[]> get(@NonNull String uuid) {
        return s3Reference.get(uuid)
                .flatMap(s3::get);
    }
}
