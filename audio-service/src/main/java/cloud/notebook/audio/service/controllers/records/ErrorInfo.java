package cloud.notebook.audio.service.controllers.records;


import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ErrorInfo(LocalDateTime timestamp, int status, String error, String path) {
}
