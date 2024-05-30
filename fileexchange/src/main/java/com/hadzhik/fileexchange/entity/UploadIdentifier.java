package com.hadzhik.fileexchange.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "upload_ids")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UploadIdentifier {
    @Id @Column(length = 5)
    private String id;

    @Override
    public String toString() {
        return id;
    }
}
