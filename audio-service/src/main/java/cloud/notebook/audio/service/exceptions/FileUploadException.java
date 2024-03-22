package cloud.notebook.audio.service.exceptions;

import java.util.function.UnaryOperator;

public class FileUploadException extends AudioServiceException {

    private static final UnaryOperator<String> MESSAGE_FORMAT = f -> String.format("Can't upload file:%s", f);

    private final String fileName;


    public FileUploadException(String fileName, Throwable cause) {
        super(MESSAGE_FORMAT.apply(fileName), cause);
        this.fileName = fileName;
    }
}
