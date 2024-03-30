package cloud.notebook.audio.service.aws.sns.impl;

import cloud.notebook.audio.service.aws.sns.StoredAudioSnsService;
import io.awspring.cloud.sns.core.SnsNotification;
import io.awspring.cloud.sns.core.SnsOperations;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class CommonStoredAudioSnsService implements StoredAudioSnsService {

    private final SnsOperations snsOperations;
    private final String topicName;

    @Autowired
    public CommonStoredAudioSnsService(@NonNull SnsOperations snsOperations,
                                       @NonNull @Value("${notebook.audio.service.storedAudioTopic.name}") String topicName) {
        this.snsOperations = snsOperations;
        this.topicName = topicName;
    }

    @Override
    public void sendId(@NonNull String id) {
        snsOperations.sendNotification(topicName, SnsNotification.of(id));
        LOG.info("Stored audio notification sent. Audio id:{}. Topic: {}", id, topicName);
    }
}
