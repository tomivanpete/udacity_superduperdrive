package com.udacity.jwdnd.course1.cloudstorage.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR, reason="Duplicate filename")
public class DuplicateFileNameException extends RuntimeException {

    public DuplicateFileNameException(String errorMessage) {
        super(errorMessage);
    }
}
