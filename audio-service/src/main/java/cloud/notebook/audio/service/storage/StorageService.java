package cloud.notebook.audio.service.storage;

import java.util.Optional;

public interface StorageService {

    String add(byte[] data);

    Optional<byte[]> get(String uuid);

}
