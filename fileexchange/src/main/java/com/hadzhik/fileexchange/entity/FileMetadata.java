package com.hadzhik.fileexchange.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "file_metadata")
@Getter @Setter @NoArgsConstructor
public class FileMetadata {

    @Id
    @GeneratedValue
    private Integer id;

    private String originalFilename;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "upload_id", nullable = false)
    private UploadIdentifier uploadIdentifier;

    private String fileType;

    private int fileSize;

    public FileMetadata(String originalFilename, UploadIdentifier uploadIdentifier,
                        String fileType, int fileSize) {
        this.originalFilename = originalFilename;
        this.uploadIdentifier = uploadIdentifier;
        this.fileType = fileType;
        this.fileSize = fileSize;
    }
}
