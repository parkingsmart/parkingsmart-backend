package com.oocl.parkingsmart.exception;

import org.springframework.http.HttpStatus;

public class NullParamExpection extends SystemException {
    public NullParamExpection() {
        super(HttpStatus.BAD_REQUEST,"Parameter cannot be empty");
    }
}
