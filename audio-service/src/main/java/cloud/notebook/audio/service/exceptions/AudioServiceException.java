package cloud.notebook.audio.service.exceptions;

public class AudioServiceException extends RuntimeException {

    public AudioServiceException(String message) {
        super(message);
    }

    public AudioServiceException(Throwable cause) {
        super(cause);
    }

    public AudioServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
