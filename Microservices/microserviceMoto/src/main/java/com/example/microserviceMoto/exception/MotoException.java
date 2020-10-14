package com.example.microserviceMoto.exception;

public class MotoException extends RuntimeException {

    public MotoException(Long id) {
        super("aucune voiture avec cette id: " + id);
    }
}