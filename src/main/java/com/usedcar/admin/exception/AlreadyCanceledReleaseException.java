package com.usedcar.admin.exception;

public class AlreadyCanceledReleaseException extends RuntimeException {

    public AlreadyCanceledReleaseException() {
    }

    public AlreadyCanceledReleaseException(String message) {
        super(message);
    }

    public AlreadyCanceledReleaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadyCanceledReleaseException(Throwable cause) {
        super(cause);
    }

}
