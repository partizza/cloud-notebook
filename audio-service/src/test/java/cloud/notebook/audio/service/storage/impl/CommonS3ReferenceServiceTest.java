package cloud.notebook.audio.service.storage.impl;


import cloud.notebook.audio.service.aws.s3.S3Properties;
import cloud.notebook.audio.service.db.entries.StorageReference;
import cloud.notebook.audio.service.db.repositories.StorageReferenceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommonS3ReferenceServiceTest {

    private static final S3Properties S3_PROPS = new S3Properties("buck", "/test/prefix");

    @Mock
    private StorageReferenceRepository repositoryMock;


    @Test
    void shouldFailGenerateOnNull() {
        CommonS3ReferenceService sut = new CommonS3ReferenceService(S3_PROPS, repositoryMock);
        assertThrows(IllegalArgumentException.class, () -> sut.generate(null));
    }

    @Test
    void shouldGenerate() {
        String uuid = UUID.randomUUID().toString();
        LocalDate now = LocalDate.now();

        String expectedKey = Path.of(S3_PROPS.prefix())
                .resolve(String.valueOf(now.getYear()))
                .resolve(String.valueOf(now.getMonthValue()))
                .resolve(String.valueOf(now.getDayOfMonth()))
                .resolve(uuid)
                .toString();

        StorageReference storageReference = new StorageReference(uuid, expectedKey);
        when(repositoryMock.save(storageReference)).thenReturn(storageReference);


        CommonS3ReferenceService sut = new CommonS3ReferenceService(S3_PROPS, repositoryMock);
        String result = sut.generate(uuid);

        assertEquals(expectedKey, result);
        verify(repositoryMock, times(1)).save(storageReference);
    }

    @Test
    void shouldGet() {
        String uuid = UUID.randomUUID().toString();
        StorageReference storageReference = new StorageReference(uuid, "/s3key/path");
        when(repositoryMock.findById(uuid)).thenReturn(Optional.of(storageReference));

        CommonS3ReferenceService sut = new CommonS3ReferenceService(S3_PROPS, repositoryMock);
        Optional<String> result = sut.get(uuid);

        assertTrue(result.isPresent());
        assertEquals(storageReference.getS3Key(), result.get());
    }

    @Test
    void shouldGetEmptyIfNotFound() {
        String uuid = UUID.randomUUID().toString();
        when(repositoryMock.findById(uuid)).thenReturn(Optional.empty());

        CommonS3ReferenceService sut = new CommonS3ReferenceService(S3_PROPS, repositoryMock);
        Optional<String> result = sut.get(uuid);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldFailGetOnNull() {
        CommonS3ReferenceService sut = new CommonS3ReferenceService(S3_PROPS, repositoryMock);
        assertThrows(IllegalArgumentException.class, () -> sut.get(null));
    }
}