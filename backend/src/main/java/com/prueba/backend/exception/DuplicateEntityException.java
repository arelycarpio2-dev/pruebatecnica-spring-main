package com.prueba.backend.exception;

public class DuplicateEntityException extends RuntimeException {
    public DuplicateEntityException(String mensaje) {
        super(mensaje);
    }
}
