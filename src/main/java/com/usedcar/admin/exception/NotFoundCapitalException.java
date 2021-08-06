package com.usedcar.admin.exception;

public class NotFoundCapitalException extends RuntimeException {

    public NotFoundCapitalException() {
    }

    public NotFoundCapitalException(String message) {
        super(message);
    }

    public NotFoundCapitalException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundCapitalException(Throwable cause) {
        super(cause);
    }

}
