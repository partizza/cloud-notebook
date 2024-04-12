package cloud.notebook.transcription.service.basis.storage.audio;

public interface AudioStorage {

    byte[] get(String id);
}
