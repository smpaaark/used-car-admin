package com.usedcar.admin.exception;

public class NotFoundReleaseException extends RuntimeException {

    public NotFoundReleaseException() {
    }

    public NotFoundReleaseException(String message) {
        super(message);
    }

    public NotFoundReleaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundReleaseException(Throwable cause) {
        super(cause);
    }

}