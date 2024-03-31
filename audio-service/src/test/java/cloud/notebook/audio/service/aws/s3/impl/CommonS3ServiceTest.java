package cloud.notebook.audio.service.aws.s3.impl;


import cloud.notebook.audio.service.aws.s3.S3Properties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommonS3ServiceTest {

    private static final S3Properties S3_PROPS = new S3Properties("testBucket", "/prefix");

    @Mock
    private ResponseBytes<GetObjectResponse> bytesRespMock;

    @Mock
    private S3Client s3ClientMock;

    @Test
    void shouldPutToBucket() {
        String s3Key = "/some/path";

        PutObjectRequest req = PutObjectRequest.builder()
                .bucket(S3_PROPS.bucket())
                .key(s3Key)
                .build();

        when(s3ClientMock.putObject(eq(req), any(RequestBody.class))).thenReturn(null);

        CommonS3Service sut = new CommonS3Service(s3ClientMock, S3_PROPS);
        sut.put(s3Key, "".getBytes());

        verify(s3ClientMock, times(1)).putObject(eq(req), any(RequestBody.class));
    }

    @Test
    void shouldThrowOnPutWithNullInput() {
        CommonS3Service sut = new CommonS3Service(s3ClientMock, S3_PROPS);
        assertThrows(IllegalArgumentException.class, () -> sut.put(null, "".getBytes()));
    }

    @Test
    void shouldGet() {
        String s3Key = "/some/path/key";
        byte[] expected = "result".getBytes();

        GetObjectRequest req = GetObjectRequest.builder()
                .bucket(S3_PROPS.bucket())
                .key(s3Key)
                .build();

        when(s3ClientMock.getObjectAsBytes(req)).thenReturn(bytesRespMock);
        when(bytesRespMock.asByteArray()).thenReturn(expected);

        CommonS3Service sut = new CommonS3Service(s3ClientMock, S3_PROPS);
        Optional<byte[]> result = sut.get(s3Key);

        assertTrue(result.isPresent());
        assertEquals(expected, result.get());
    }

    @Test
    void shouldGetEmptyIfNoData() {
        String s3Key = "/some/path/key";

        GetObjectRequest req = GetObjectRequest.builder()
                .bucket(S3_PROPS.bucket())
                .key(s3Key)
                .build();

        when(s3ClientMock.getObjectAsBytes(req)).thenThrow(NoSuchKeyException.class);

        CommonS3Service sut = new CommonS3Service(s3ClientMock, S3_PROPS);
        Optional<byte[]> result = sut.get(s3Key);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldFailGetOnNullInput() {
        CommonS3Service sut = new CommonS3Service(s3ClientMock, S3_PROPS);
        assertThrows(IllegalArgumentException.class, () -> sut.get(null));
    }
}