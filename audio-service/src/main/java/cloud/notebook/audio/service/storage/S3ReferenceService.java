package cloud.notebook.audio.service.storage;


import java.util.Optional;

public interface S3ReferenceService {

    String generate(String uuid);

    Optional<String> get(String uuid);
}
