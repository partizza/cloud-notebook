package cloud.notebook.transcript.service.controllers;

import cloud.notebook.transcript.service.aws.s3.S3Service;
import cloud.notebook.transcript.service.controllers.records.ErrorInfo;
import cloud.notebook.transcript.service.exceptions.FileUploadException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;


@Log4j2
@RestController
public class TranscriptController {

    private final S3Service s3Service;

    @Autowired
    public TranscriptController(@NonNull S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @PutMapping(path = "/transcript/{uuid}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> put(@PathVariable String uuid, @RequestParam MultipartFile file) {
        LOG.info("Requested to upload data with '{}' id", uuid);

        s3Service.put(uuid, getBytes(file));

        return ResponseEntity.ok()
                .build();
    }

    @GetMapping(path = "/transcript/{uuid}")
    public ResponseEntity<ByteArrayResource> get(@PathVariable String uuid) {
        LOG.info("Requested to download data by id: {}", uuid);

        return s3Service.get(uuid)
                .map(ByteArrayResource::new)
                .map(data -> ResponseEntity.ok()
                        .contentLength(data.contentLength())
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .body(data))
                .orElse(ResponseEntity.notFound().build());
    }

    private byte[] getBytes(@NonNull MultipartFile multipartFile) {
        try (InputStream in = multipartFile.getInputStream()) {

            return in.readAllBytes();

        } catch (IOException e) {
            throw new FileUploadException(multipartFile.getOriginalFilename(), e);
        }
    }

    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<ErrorInfo> handle(FileUploadException e, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorInfo.builder()
                        .timestamp(LocalDateTime.now())
                        .path(req.getRequestURI())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .error(e.getMessage())
                        .build());
    }
}
