package cloud.notebook.audio.service.controllers;

import cloud.notebook.audio.service.dto.AudioRecord;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;


@Log4j2
@Controller
public class AudioResourcesController {


    @PostMapping(path = "audio", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AudioRecord> post(@RequestParam MultipartFile file) {
        LOG.info("Requested to upload data: {}", file::getName);

        return ResponseEntity.ok(new AudioRecord(UUID.randomUUID().toString()));
    }

    @GetMapping(path = "audio/{uuid}")
    public ResponseEntity<ByteArrayResource> getAudio(@PathVariable String uuid) {
        LOG.info("Requested to download data by UUID: {}", uuid);

        byte[] data = "audio data".getBytes();
        return ResponseEntity.ok()
                .contentLength(data.length)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new ByteArrayResource(data));
    }
}
