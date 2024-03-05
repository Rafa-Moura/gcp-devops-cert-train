package br.com.stockapi.controller.exception;

public class InvalidRequestException extends StandardException {

    public InvalidRequestException(String reason, String message, Throwable throwable){
        super(reason, message, throwable);
    };

}
