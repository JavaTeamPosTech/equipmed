package com.postechfiap.msequipamentos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class SecurityInconsistencyException extends RuntimeException {
    public SecurityInconsistencyException(String message) {
        super(message);
    }
}