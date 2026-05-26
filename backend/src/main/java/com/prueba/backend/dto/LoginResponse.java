package com.prueba.backend.dto;

public class LoginResponse {

    private String token;
    private String username;
    private String mensaje;

    public LoginResponse() {
    }

    public LoginResponse(String token, String username, String mensaje) {
        this.token = token;
        this.username = username;
        this.mensaje = mensaje;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
