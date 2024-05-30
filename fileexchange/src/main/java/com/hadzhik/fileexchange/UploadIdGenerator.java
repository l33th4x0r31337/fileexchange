package com.hadzhik.fileexchange;

import com.hadzhik.fileexchange.entity.UploadIdentifier;
import com.hadzhik.fileexchange.repository.UploadIdentifierRepository;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Component
public class UploadIdGenerator {
    private final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private final int LENGTH = 5;
    private final UploadIdentifierRepository uploadIdRepo;
    private final Set<String> generatedStrings = new HashSet<>();

    public UploadIdGenerator(UploadIdentifierRepository uploadIdRepo) {
        this.uploadIdRepo = uploadIdRepo;
    }

    public UploadIdentifier generateUploadId() {
        Random random = new Random();
        String uploadId;
        do {
            StringBuilder sb = new StringBuilder(LENGTH);
            for (int i = 0; i < LENGTH; i++) {
                int randomIndex = random.nextInt(CHARACTERS.length());
                sb.append(CHARACTERS.charAt(randomIndex));
            }
            uploadId = sb.toString();
        } while (uploadIdRepo.existsById(uploadId)); // Check if the generated string is already in the db

        return new UploadIdentifier(uploadId);
    }

}
