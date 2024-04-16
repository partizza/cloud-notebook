package cloud.notebook.transcript.service.aws.s3;

import java.util.Optional;

public interface S3Service {

    Optional<byte[]> get(String id);

    void put(String id, byte[] data);
}
