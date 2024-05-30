package com.hadzhik.fileexchange.service;

import com.hadzhik.fileexchange.DownloadResponse;
import com.hadzhik.fileexchange.UploadIdGenerator;
import com.hadzhik.fileexchange.entity.FileMetadata;
import com.hadzhik.fileexchange.entity.UploadIdentifier;
import com.hadzhik.fileexchange.exception.StorageException;
import com.hadzhik.fileexchange.repository.FileMetadataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StorageService {

    public static final Path STORAGE_DIR = Path.of("C:\\Users\\smartypants\\Downloads\\uploaded_files");
    private final FileMetadataRepository fileMetadataRepo;
    private final UploadIdGenerator uploadIdGenerator;

    public UploadIdentifier save(MultipartFile... files) {
        List<MultipartFile> uploadedFiles = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                saveFile(file);
                uploadedFiles.add(file);
            } catch (StorageException storageException) {
                System.out.println(storageException.getMessage());
            }
        }

        if (uploadedFiles.isEmpty()) return null;

        UploadIdentifier uploadId = uploadIdGenerator.generateUploadId();
        for (MultipartFile file: files) {
            fileMetadataRepo.save(
                    new FileMetadata(file.getOriginalFilename(),
                            uploadId,
                            file.getContentType(),
                            (int) file.getSize())
            );
        }

        return uploadId;
    }

    public List<FileMetadata> getFileMetadata(UploadIdentifier uploadId) {
        List<FileMetadata> fileMetadata =
                fileMetadataRepo.findAllByUploadIdentifier(uploadId);

        return fileMetadata.isEmpty() ? null : fileMetadata;
    }

    public DownloadResponse prepareDownload(String uploadId) {
        return new DownloadResponse(
                getFileMetadata(new UploadIdentifier(uploadId))
        ).build();
    }

    public void deleteAll() throws IOException {
        FileSystemUtils.deleteRecursively(STORAGE_DIR);
    }

    public void init() {
        try {
            Files.createDirectories(STORAGE_DIR);
        }
        catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }

    private void saveFile(MultipartFile file) {
        if (file.isEmpty()) throw new StorageException("file is empty");

        Path destinationFile = STORAGE_DIR.resolve(Path.of(file.getOriginalFilename()));

        if (!destinationFile.getParent().equals(STORAGE_DIR)) {
            // This is a security check
            throw new StorageException(
                    "cant store file outside current dir");
        }

        try (InputStream in = file.getInputStream()) {
            Files.copy(in, destinationFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new StorageException("failed to store file", e);
        }
    }

}
