package com.example.microserviceVoiture.model;

import lombok.Data;

@Data
public class FileResponse {
    private String fileName;
    private String fileUrl;
    private String message;

    public FileResponse(String fileName, String fileUrl, String message) {
        this.fileName = fileName;
        this.fileUrl = fileUrl;
        this.message = message;
    }
}