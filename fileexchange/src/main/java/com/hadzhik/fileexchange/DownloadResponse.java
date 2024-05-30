package com.hadzhik.fileexchange;

import com.hadzhik.fileexchange.entity.FileMetadata;
import com.hadzhik.fileexchange.service.StorageService;
import lombok.Getter;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.FileInputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class DownloadResponse {

    private List<FileMetadata> fileMetadata;
    @Getter
    private HttpHeaders headers;
    @Getter
    private StreamingResponseBody body;

    public DownloadResponse(List<FileMetadata> fileMetadata) {
        this.fileMetadata = fileMetadata;
        headers = new HttpHeaders();
    }

    public DownloadResponse build() {
        if (fileMetadata.size() == 1) {
            oneFileDownloadResponse();
        } else if (fileMetadata.size() > 1) {
            multiFileDownloadResponse();
        }

        return this;
    }

    private void oneFileDownloadResponse() {
        headers.setContentDisposition(
                ContentDisposition.attachment()
                        .filename(fileMetadata.getFirst().getOriginalFilename())
                        .build());

        headers.set("Content-Type", fileMetadata.getFirst().getFileType());

        body = out -> {
            FileInputStream fileInputStream = new FileInputStream(
                    StorageService.STORAGE_DIR + "/" + fileMetadata.getFirst().getOriginalFilename());
            IOUtils.copy(fileInputStream, out);
            fileInputStream.close();
        };
    }

    private void multiFileDownloadResponse() {
        headers.setContentDisposition(
                ContentDisposition.attachment()
                        .filename(fileMetadata.getFirst().getUploadIdentifier() + ".zip")
                        .build());
        headers.set("Content-Type", "application/zip");

        body = out -> {
            ZipOutputStream zipOutputStream = new ZipOutputStream(out);

            for (FileMetadata metadata : fileMetadata) {
                zipOutputStream.putNextEntry(new ZipEntry(metadata.getOriginalFilename()));
                FileInputStream fileInputStream = new FileInputStream(
                        StorageService.STORAGE_DIR + "/" + metadata.getOriginalFilename());

                IOUtils.copy(fileInputStream, zipOutputStream);

                fileInputStream.close();
                zipOutputStream.closeEntry();
            }

            zipOutputStream.close();
        };
    }
}
