package com.example.doctorappointment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT) // Optional: automatically returns 409 in REST APIs
public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
