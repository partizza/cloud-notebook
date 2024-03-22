package cloud.notebook.audio.service.controllers;

import cloud.notebook.audio.service.exceptions.FileUploadException;
import cloud.notebook.audio.service.storage.StorageService;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;


@Log4j2
@Controller
public class AudioResourcesController {

    private final StorageService storageService;

    @Autowired
    public AudioResourcesController(@NonNull StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping(path = "audio", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> post(@RequestParam MultipartFile file) {
        LOG.info("Requested to upload data: {}", file::getName);

        byte[] data = getBytes(file);
        String uuid = storageService.add(data);

        return ResponseEntity.ok(uuid);
    }

    @GetMapping(path = "audio/{uuid}")
    public ResponseEntity<ByteArrayResource> getAudio(@PathVariable String uuid) {
        LOG.info("Requested to download data by UUID: {}", uuid);

        return storageService.get(uuid)
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
            throw new FileUploadException(multipartFile.getName(), e);
        }
    }
}
