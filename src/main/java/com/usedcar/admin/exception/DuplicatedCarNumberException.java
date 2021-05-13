package com.usedcar.admin.exception;

public class DuplicatedCarNumberException extends RuntimeException {

    public DuplicatedCarNumberException() {
    }

    public DuplicatedCarNumberException(String message) {
        super(message);
    }

    public DuplicatedCarNumberException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicatedCarNumberException(Throwable cause) {
        super(cause);
    }

}
