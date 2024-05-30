package com.hadzhik.fileexchange.repository;

import com.hadzhik.fileexchange.entity.UploadIdentifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UploadIdentifierRepository extends JpaRepository<UploadIdentifier, String> {

}
