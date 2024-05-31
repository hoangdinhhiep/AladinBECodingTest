package com.tatoeba.exception;

public class TatoebaException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private String errorCode;

    public TatoebaException() {
        super("General error.");
    }

    public TatoebaException(String message) {
        super(message);
    }

    public TatoebaException(String message, Throwable t) {
        super(message, t);
    }

    public TatoebaException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
