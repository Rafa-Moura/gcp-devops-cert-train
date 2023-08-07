package br.com.stockapi.controller.exception;

public class SystemException extends StandardException {
    public SystemException(String reason, String message, Throwable throwable) {
        super(reason, message, throwable);
    }
}
