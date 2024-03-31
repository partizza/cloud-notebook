package cloud.notebook.audio.service.storage.impl;

import cloud.notebook.audio.service.aws.s3.S3Properties;
import cloud.notebook.audio.service.db.entries.StorageReference;
import cloud.notebook.audio.service.db.repositories.StorageReferenceRepository;
import cloud.notebook.audio.service.storage.S3ReferenceService;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Optional;

@Log4j2
@Service
public class CommonS3ReferenceService implements S3ReferenceService {

    private S3Properties s3Props;
    private final StorageReferenceRepository repository;

    @Autowired
    public CommonS3ReferenceService(@NonNull S3Properties s3Props,
                                    @NonNull StorageReferenceRepository repository) {
        this.s3Props = s3Props;
        this.repository = repository;
    }

    @Override
    public String generate(@NonNull String uuid) {
        LOG.debug("Generating s3 key, uuid:{}", uuid);

        StorageReference storageReference = toStorageReference(uuid);
        return repository.save(storageReference)
                .getS3Key();
    }

    @Override
    public Optional<String> get(@NonNull String uuid) {
        LOG.debug("Getting s3 key by uuid:{}", uuid);

        return repository.findById(uuid)
                .map(StorageReference::getS3Key);
    }

    private StorageReference toStorageReference(@NonNull String uuid) {
        String s3Key = generateS3Key(uuid);
        return new StorageReference(uuid, s3Key);
    }

    private String generateS3Key(@NonNull String uuid) {
        LocalDate now = LocalDate.now(ZoneOffset.UTC);

        return Path.of(s3Props.prefix())
                .resolve(String.valueOf(now.getYear()))
                .resolve(String.valueOf(now.getMonthValue()))
                .resolve(String.valueOf(now.getDayOfMonth()))
                .resolve(uuid)
                .toString();
    }
}
