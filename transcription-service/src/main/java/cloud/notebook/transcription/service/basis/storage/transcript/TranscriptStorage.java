package cloud.notebook.transcription.service.basis.storage.transcript;

public interface TranscriptStorage {

    void save(String id, String text);
}
