package br.com.stockapi.controller.exception;

public class BusinessException extends StandardException{
    public BusinessException(String reason, String error) {
        super(reason, error);
    }
}
