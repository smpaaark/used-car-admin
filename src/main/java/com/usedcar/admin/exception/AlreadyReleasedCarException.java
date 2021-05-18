package com.usedcar.admin.exception;

public class AlreadyReleasedCarException extends RuntimeException {

    public AlreadyReleasedCarException() {
    }

    public AlreadyReleasedCarException(String message) {
        super(message);
    }

    public AlreadyReleasedCarException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadyReleasedCarException(Throwable cause) {
        super(cause);
    }

}
