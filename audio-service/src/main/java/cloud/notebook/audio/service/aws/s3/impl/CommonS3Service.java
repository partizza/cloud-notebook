package cloud.notebook.audio.service.aws.s3.impl;

import cloud.notebook.audio.service.aws.s3.S3Properties;
import cloud.notebook.audio.service.aws.s3.S3Service;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.Optional;

@Service
@Log4j2
public class CommonS3Service implements S3Service {

    private final S3Client s3Client;
    private final S3Properties s3Props;

    @Autowired
    public CommonS3Service(@NonNull S3Client s3Client,
                           @NonNull S3Properties s3Props) {
        this.s3Client = s3Client;
        this.s3Props = s3Props;
    }

    @Override
    public void put(@NonNull String s3Key, byte[] data) {
        LOG.info("About to upload s3://{}{}", s3Props.bucket(), s3Key);
        PutObjectRequest req = PutObjectRequest.builder()
                .bucket(s3Props.bucket())
                .key(s3Key)
                .build();

        s3Client.putObject(req, RequestBody.fromBytes(data));
        LOG.info("Uploaded into s3://{}{}", s3Props.bucket(), s3Key);
    }

    @Override
    public Optional<byte[]> get(@NonNull String s3Key) {
        GetObjectRequest req = GetObjectRequest.builder()
                .bucket(s3Props.bucket())
                .key(s3Key)
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
}
