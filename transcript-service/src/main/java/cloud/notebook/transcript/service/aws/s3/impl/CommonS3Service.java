package cloud.notebook.transcript.service.aws.s3.impl;

import cloud.notebook.transcript.service.aws.s3.S3Properties;
import cloud.notebook.transcript.service.aws.s3.S3Service;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.nio.file.Paths;
import java.util.Optional;

@Service
@Log4j2
public class CommonS3Service implements S3Service {

    private static final String ORIGIN_TRANSCRIPT_NAME = "origin";

    private final S3Client s3Client;
    private final S3Properties props;

    @Autowired
    public CommonS3Service(@NonNull S3Client s3Client, @NonNull S3Properties props) {
        this.s3Client = s3Client;
        this.props = props;
    }

    @Override
    public Optional<byte[]> get(@NonNull String id) {
        GetObjectRequest req = GetObjectRequest.builder()
                .bucket(props.bucket())
                .key(toOriginKey(id))
                .build();

        return Optional.ofNullable(getOrNull(req));
    }

    private byte[] getOrNull(@NonNull GetObjectRequest req) {
        try {
            return s3Client.getObjectAsBytes(req)
                    .asByteArray();

        } catch (NoSuchKeyException e) {

            LOG.warn("No data exists, s3://{}{}", req::bucket, req::key);
            return null;
        }
    }

    @Override
    public void put(@NonNull String id, byte[] data) {
        String s3Key = toOriginKey(id);
        LOG.info("About to upload s3://{}{}", props.bucket(), s3Key);
        PutObjectRequest req = PutObjectRequest.builder()
                .bucket(props.bucket())
                .key(s3Key)
                .build();

        s3Client.putObject(req, RequestBody.fromBytes(data));
        LOG.info("Uploaded into s3://{}{}", props.bucket(), s3Key);
    }

    private String toOriginKey(@NonNull String id) {
        return Paths.get(props.prefix())
                .resolve(id)
                .resolve(ORIGIN_TRANSCRIPT_NAME)
                .toString();
    }
}
