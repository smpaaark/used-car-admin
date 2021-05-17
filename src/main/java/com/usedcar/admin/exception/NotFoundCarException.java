package com.usedcar.admin.exception;

public class NotFoundCarException extends RuntimeException {

    public NotFoundCarException() {
    }

    public NotFoundCarException(String message) {
        super(message);
    }

    public NotFoundCarException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundCarException(Throwable cause) {
        super(cause);
    }

}
