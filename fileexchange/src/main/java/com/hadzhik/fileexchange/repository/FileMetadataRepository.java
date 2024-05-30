package com.hadzhik.fileexchange.repository;

import com.hadzhik.fileexchange.entity.FileMetadata;
import com.hadzhik.fileexchange.entity.UploadIdentifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileMetadataRepository extends JpaRepository<FileMetadata, Integer> {

    List<FileMetadata> findAllByUploadIdentifier(UploadIdentifier uploadId);

}
