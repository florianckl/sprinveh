package com.example.microserviceVoiture.exception;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileStorageException extends RuntimeException {
    private String message;

    public FileStorageException(String s) {
        this.message=s;
    }
}
