/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author tasu
 */
public class CustomMultiPartFile implements MultipartFile {

    private final byte[] fileContent;

    private String fileName;

    private String contentType;

    private File file;

    private String destPath = System.getProperty("java.io.tmpdir");

    private FileOutputStream fileOutputStream;

    public CustomMultiPartFile(byte[] fileData, String name) {
        this.fileContent = fileData;
        this.fileName = name;
        file = new File(destPath + fileName);

    }

    @Override
    public String getOriginalFilename() {
        // TODO - implementation depends on your requirements
        return null;
    }

    @Override
    public String getName() {
        // TODO - implementation depends on your requirements 
        return null;
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        fileOutputStream = new FileOutputStream(dest);
        fileOutputStream.write(fileContent);
    }

    public void clearOutStreams() throws IOException {
        if (null != fileOutputStream) {
            fileOutputStream.flush();
            fileOutputStream.close();
            file.deleteOnExit();
        }
    }

    @Override
    public byte[] getBytes() throws IOException {
        return fileContent;
    }

    @Override
    public long getSize() {
        return fileContent.length;
    }

    @Override
    public boolean isEmpty() {
        return fileContent == null || fileContent.length == 0;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(fileContent);
    }

    @Override
    public String getContentType() {
        // TODO - implementation depends on your requirements
        return null;
    }
}
