package com.abarrotes.pos.exception;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {
    private final HttpStatus status;
    private final String detalle;

    public ApiException(HttpStatus status, String mensaje, String detalle) {
        super(mensaje);
        this.status = status;
        this.detalle = detalle;
    }

    public HttpStatus getStatus() { return status; }
    public String getDetalle() { return detalle; }
}
