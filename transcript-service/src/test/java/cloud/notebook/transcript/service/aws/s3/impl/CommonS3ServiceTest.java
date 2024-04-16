package cloud.notebook.transcript.service.aws.s3.impl;


import cloud.notebook.transcript.service.aws.s3.S3Properties;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommonS3ServiceTest {

    private static final S3Properties PROPS = new S3Properties("my-bucket", "/pref");
    @Mock
    private S3Client s3ClientMock;
    @Mock
    private ResponseBytes<GetObjectResponse> resBytesMock;

    private CommonS3Service sut;

    @BeforeEach
    void init() {
        sut = new CommonS3Service(s3ClientMock, PROPS);
    }

    @Test
    void shouldFailGetOnNull() {
        assertThrows(IllegalArgumentException.class, () -> sut.get(null));
    }

    @Test
    void shouldGetById() {
        String id = "u-u-id";
        byte[] data = "some data".getBytes();
        GetObjectRequest req = GetObjectRequest.builder()
                .bucket(PROPS.bucket())
                .key(String.format("%s/%s/origin", PROPS.prefix(), id))
                .build();

        when(s3ClientMock.getObjectAsBytes(req)).thenReturn(resBytesMock);
        when(resBytesMock.asByteArray()).thenReturn(data);

        Optional<byte[]> result = sut.get(id);

        assertTrue(result.isPresent());
        assertArrayEquals(data, result.get());
    }

    @Test
    void shouldGetEmptyById() {
        String id = "u-u-id";
        GetObjectRequest req = GetObjectRequest.builder()
                .bucket(PROPS.bucket())
                .key(String.format("%s/%s/origin", PROPS.prefix(), id))
                .build();

        when(s3ClientMock.getObjectAsBytes(req)).thenThrow(NoSuchKeyException.class);

        Optional<byte[]> result = sut.get(id);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldFailPutOnNull() {
        assertThrows(IllegalArgumentException.class, () -> sut.put(null, new byte[0]));
    }

    @Test
    void shouldPut() {
        String id = "u-u-id";
        byte[] data = "123".getBytes();

        PutObjectRequest req = PutObjectRequest.builder()
                .bucket(PROPS.bucket())
                .key(String.format("%s/%s/origin", PROPS.prefix(), id))
                .build();

        sut.put(id, data);

        verify(s3ClientMock, times(1)).putObject(eq(req), any(RequestBody.class));
    }
}