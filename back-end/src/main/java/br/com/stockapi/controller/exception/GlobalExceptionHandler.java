package br.com.stockapi.controller.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    private static final String INTERNAL_SERVER_ERROR_MESSAGE = "Ocorreu um erro interno, tente novamente mais tarde ou contate um administrador";
    private static final String REQUEST_JSON_INVALID_ERROR_MESSAGE = "JSON mal formado. Verifique as informações fornecidas e tente novamente";
    private Environment environment;

    @ExceptionHandler({Throwable.class})
    public ResponseEntity<StandardError> handleThrowable(final Throwable throwable) {

        StandardError standardError = new StandardError(HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                INTERNAL_SERVER_ERROR_MESSAGE, throwable);

        standardError.setTimestamp(LocalDateTime.now());

        log.error(standardError.getMessage(), standardError);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(standardError);
    }

    @ExceptionHandler({InvalidRequestException.class})
    public ResponseEntity<StandardError> handleGlobalException(final InvalidRequestException invalidRequestException) {

        StandardError standardError = new StandardError(invalidRequestException.getReason(),
                invalidRequestException.getMessage(), invalidRequestException.getCause());

        standardError.setTimestamp(LocalDateTime.now());

        log.error(invalidRequestException.getMessage(), invalidRequestException);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(standardError);
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<StandardError> handleGlobalNotFoundException(final NotFoundException notFoundException) {

        StandardError standardError = new StandardError(notFoundException.getReason(),
                notFoundException.getMessage(), notFoundException.getCause());

        standardError.setTimestamp(LocalDateTime.now());

        log.error(notFoundException.getMessage(), notFoundException);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(standardError);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<StandardError> handleHttpMessageNotReadable(HttpMessageNotReadableException exception) {

        StandardError standardError = new StandardError(HttpStatus.BAD_REQUEST.toString(), REQUEST_JSON_INVALID_ERROR_MESSAGE, exception.getCause());

        standardError.setTimestamp(LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(standardError);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<StandardError> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception){

        List<String> errors = new ArrayList<>();

        for (FieldError error : exception.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : exception.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }

        StandardError standardError = new StandardError(HttpStatus.BAD_REQUEST.toString(),
                errors.toString().replace("[", "").replace("]", ""), exception.getCause());

        standardError.setTimestamp(LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(standardError);
    }

    @Autowired
    public void setEnvironment(final Environment environment) {
        this.environment = environment;
    }
}
