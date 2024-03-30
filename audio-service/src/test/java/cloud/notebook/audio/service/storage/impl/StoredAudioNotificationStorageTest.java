package cloud.notebook.audio.service.storage.impl;

import cloud.notebook.audio.service.aws.sns.StoredAudioSnsService;
import cloud.notebook.audio.service.storage.StorageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StoredAudioNotificationStorageTest {

    @Mock
    private StorageService storageMock;
    @Mock
    private StoredAudioSnsService snsMock;

    @InjectMocks
    private StoredAudioNotificationStorage sut;

    @Test
    void shouldFailConstructionWithNull() {
        assertThrows(IllegalArgumentException.class, () -> new StoredAudioNotificationStorage(storageMock, null));
        assertThrows(IllegalArgumentException.class, () -> new StoredAudioNotificationStorage(null, snsMock));
    }

    @Test
    void shouldAddAndNotify() {
        byte[] data = "data".getBytes();
        String id = "some-id";
        when(storageMock.add(data)).thenReturn(id);

        String result = sut.add(data);

        assertEquals(id, result);
        verify(storageMock, times(1)).add(data);
        verify(snsMock, times(1)).sendId(id);
    }

    @Test
    void shouldFailGetByNullId() {
        assertThrows(IllegalArgumentException.class, () -> sut.get(null));
    }

    @Test
    void shouldGetById() {
        String id = "some-id";
        byte[] data = "expected-data".getBytes();

        when(storageMock.get(id)).thenReturn(Optional.of(data));

        Optional<byte[]> result = sut.get(id);

        assertTrue(result.isPresent());
        assertEquals(data, result.get());
        verify(storageMock, times(1)).get(id);
    }
}