package br.com.stockapi.controller.exception;

public class NotFoundException extends StandardError {

    public NotFoundException(String reason, String message, Throwable throwable) {
        super(reason, message, throwable);
    }

    public NotFoundException(String reason, String message) {
        super(reason, message);
    }

}
