package cloud.notebook.audio.service.aws.sns.impl;


import io.awspring.cloud.sns.core.SnsNotification;
import io.awspring.cloud.sns.core.SnsOperations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CommonStoredAudioSnsServiceTest {

    private static final String TOPIC_NAME = "some-topic";

    @Mock
    private SnsOperations snsOperationsMock;

    private CommonStoredAudioSnsService sut;

    @BeforeEach
    void init() {
        sut = new CommonStoredAudioSnsService(snsOperationsMock, TOPIC_NAME);
    }

    @Test
    void shouldFailConstructionWithNull() {
        assertThrows(IllegalArgumentException.class, () -> new CommonStoredAudioSnsService(null, TOPIC_NAME));
        assertThrows(IllegalArgumentException.class, () -> new CommonStoredAudioSnsService(snsOperationsMock, null));
    }

    @Test
    void shouldFailSendNullId() {
        assertThrows(IllegalArgumentException.class, () -> sut.sendId(null));
    }

    @Test
    void shouldSendId() {
        String id = "some-id";

        sut.sendId(id);

        verify(snsOperationsMock, times(1)).sendNotification(eq(TOPIC_NAME), argThat(it -> it.getPayload().equals(id)));
    }

}