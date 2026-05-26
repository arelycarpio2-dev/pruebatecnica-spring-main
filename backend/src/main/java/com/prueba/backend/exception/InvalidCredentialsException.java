package com.prueba.backend.exception;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String mensaje) {
        super(mensaje);
    }
}
