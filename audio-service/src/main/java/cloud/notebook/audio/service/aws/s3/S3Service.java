package cloud.notebook.audio.service.aws.s3;

import java.util.Optional;

public interface S3Service {

    void put(String s3Key, byte[] data);

    Optional<byte[]> get(String s3Key);
}
