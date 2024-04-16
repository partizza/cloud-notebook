package cloud.notebook.transcript.service.exceptions;

public class TranscriptServiceException extends RuntimeException{

    public TranscriptServiceException(String message) {
        super(message);
    }

    public TranscriptServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public TranscriptServiceException(Throwable cause) {
        super(cause);
    }
}
