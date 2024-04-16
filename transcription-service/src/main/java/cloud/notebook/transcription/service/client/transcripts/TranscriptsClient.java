package cloud.notebook.transcription.service.client.transcripts;

public interface TranscriptsClient {

    void put(String id, byte[] data);
}
