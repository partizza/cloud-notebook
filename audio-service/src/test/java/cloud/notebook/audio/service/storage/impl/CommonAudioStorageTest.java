package cloud.notebook.audio.service.storage.impl;


import cloud.notebook.audio.service.aws.s3.S3Service;
import cloud.notebook.audio.service.storage.S3ReferenceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommonAudioStorageTest {

    @Mock
    private S3ReferenceService s3ReferenceMock;
    @Mock
    private S3Service s3Mock;

    @InjectMocks
    private CommonAudioStorage sut;

    @Test
    void shouldAdd() {
        byte[] data = "data".getBytes();
        String s3Key = "/key/to/put";

        when(s3ReferenceMock.generate(anyString())).thenReturn(s3Key);

        String result = sut.add(data);

        assertNotNull(result);
        verify(s3Mock, times(1)).put(s3Key, data);
    }

    @Test
    void shouldGet() {
        String uuid = UUID.randomUUID().toString();
        String s3Key = "/data/path/key";
        byte[] data = "466".getBytes();

        when(s3ReferenceMock.get(uuid)).thenReturn(Optional.of(s3Key));
        when(s3Mock.get(s3Key)).thenReturn(Optional.of(data));

        Optional<byte[]> result = sut.get(uuid);

        assertTrue(result.isPresent());
        assertEquals(data, result.get());
    }

    @Test
    void shouldGetEmptyIfNoDataFoundByKey() {
        String uuid = UUID.randomUUID().toString();
        String s3Key = "/data/path/key";

        when(s3ReferenceMock.get(uuid)).thenReturn(Optional.of(s3Key));
        when(s3Mock.get(s3Key)).thenReturn(Optional.empty());

        Optional<byte[]> result = sut.get(uuid);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldGetEmptyIfNoS3KeyFound() {
        String uuid = UUID.randomUUID().toString();

        when(s3ReferenceMock.get(uuid)).thenReturn(Optional.empty());

        Optional<byte[]> result = sut.get(uuid);

        assertTrue(result.isEmpty());
    }
}