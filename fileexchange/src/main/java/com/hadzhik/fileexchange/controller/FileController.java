package com.hadzhik.fileexchange.controller;

import com.hadzhik.fileexchange.DownloadResponse;
import com.hadzhik.fileexchange.entity.UploadIdentifier;
import com.hadzhik.fileexchange.service.StorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@RestController
public class FileController {

    private final StorageService storageService;

    public FileController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/api/files/upload")
    public UploadIdentifier handleFileUpload(@RequestParam("files") MultipartFile[] files) {
        return storageService.save(files);
    }

    @GetMapping("/api/files/{uploadId}")
    public ResponseEntity<StreamingResponseBody> downloadFiles(@PathVariable String uploadId) {
        DownloadResponse downloadResponse = storageService.prepareDownload(uploadId);

        return ResponseEntity.ok()
                .headers(downloadResponse.getHeaders())
                .body(downloadResponse.getBody());
    }

}
