/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.service.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author tasu
 */
@Service
public class StorageService {

    Logger logger = LoggerFactory.getLogger(StorageService.class);
    String tomcatHome = System.getProperty("user.home");
    private final Path path = Paths.get(tomcatHome, "/upload-dir");
    //private final Path path = Paths.get("upload-dir");

    public void store(MultipartFile file) {
        try {
            Files.deleteIfExists(Paths.get(path.toFile().getPath(), file.getOriginalFilename()));
            Files.copy(file.getInputStream(), path.resolve(file.getOriginalFilename()));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void storeFile(MultipartFile file, Path filePath, String fileName) {
        try {
            Files.deleteIfExists(Paths.get(filePath.toFile().getPath(), fileName));
            Files.copy(file.getInputStream(), filePath.resolve(fileName));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public Resource loadFile(String fileName) {
        try {
            Path file = path.resolve(fileName);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("FAIL!");
            }
        } catch (RuntimeException | MalformedURLException ex) {
            ex.printStackTrace();
            throw new RuntimeException("FAIL!");
        }
    }

    public void deleteAll() {
        FileSystemUtils.deleteRecursively(path.toFile());
    }

    public void init() {
        try {
            if (Files.notExists(path)) {
                Files.createDirectory(path);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not initialize storage!");
        }
    }

    public Path createNewDirectory(String name) {
        Path dirPath = Paths.get(path.toFile().getPath(), name);
        try {
            if (Files.notExists(dirPath)) {
                //   Files.createDirectory(dirPath);
                Files.createDirectories(dirPath);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage!");
        }
        return dirPath;
    }

}
