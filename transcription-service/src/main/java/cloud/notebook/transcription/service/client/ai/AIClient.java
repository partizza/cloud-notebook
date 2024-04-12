package cloud.notebook.transcription.service.client.ai;

public interface AIClient {

    String transcribe(byte[] audio);
}
