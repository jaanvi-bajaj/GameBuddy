package com.gamebuddy.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;

@Service
public class StorageService {

    private final Path rootLocation;

    public StorageService(org.springframework.core.env.Environment env) {
        String uploadDir = env.getProperty("file.upload-dir", "uploads");
        this.rootLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload dir", e);
        }
    }

    public String store(MultipartFile file, String prefix) {
        if (file == null || file.isEmpty()) return null;
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        String storedName = prefix + "_" + System.currentTimeMillis() + "_" + filename;
        try {
            Path destination = this.rootLocation.resolve(storedName);
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
            return storedName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    public Path getRootLocation(){
        return rootLocation;
    }
}