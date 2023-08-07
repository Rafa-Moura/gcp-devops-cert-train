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
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    private static final String INTERNAL_SERVER_ERROR_MESSAGE = "Ocorreu um erro interno, tente novamente mais tarde ou contate um administrador";
    private static final String REQUEST_JSON_INVALID_ERROR_MESSAGE = "JSON mal formado. Verifique as informações fornecidas e tente novamente";
    private Environment environment;

    @ExceptionHandler({Throwable.class})
    public ResponseEntity<StandardException> handleThrowable(final Throwable throwable) {

        StandardException standardException = new StandardException(HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                INTERNAL_SERVER_ERROR_MESSAGE, throwable);

        standardException.setTimestamp(LocalDateTime.now());

        log.error(standardException.getMessage(), standardException);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(standardException);
    }

    @ExceptionHandler({InvalidRequestException.class})
    public ResponseEntity<StandardException> handleGlobalException(final InvalidRequestException invalidRequestException) {

        StandardException standardException = new StandardException(invalidRequestException.getReason(),
                invalidRequestException.getMessage(), invalidRequestException.getCause());

        standardException.setTimestamp(LocalDateTime.now());

        log.error(invalidRequestException.getMessage(), invalidRequestException);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(standardException);
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<StandardException> handleGlobalNotFoundException(final NotFoundException notFoundException) {

        StandardException standardException = new StandardException(notFoundException.getReason(),
                notFoundException.getMessage(), notFoundException.getCause());

        standardException.setTimestamp(LocalDateTime.now());

        log.error(notFoundException.getMessage(), notFoundException);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(standardException);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<StandardException> handleHttpMessageNotReadable(HttpMessageNotReadableException exception) {

        StandardException standardException = new StandardException(HttpStatus.BAD_REQUEST.toString(), REQUEST_JSON_INVALID_ERROR_MESSAGE, exception.getCause());

        standardException.setTimestamp(LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(standardException);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<StandardException> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception){

        List<String> errors = new ArrayList<>();

        for (FieldError error : exception.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : exception.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }

        StandardException standardException = new StandardException(HttpStatus.BAD_REQUEST.toString(),
                errors.toString().replace("[", "").replace("]", ""), exception.getCause());

        standardException.setTimestamp(LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(standardException);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<StandardException> handleEnumValidationException(MethodArgumentTypeMismatchException ex) {
        StandardException standardException = new StandardException(HttpStatus.BAD_REQUEST.toString(), ex.getMessage(), ex.getCause());
        standardException.setTimestamp(LocalDateTime.now());

        if (Objects.requireNonNull(ex.getRequiredType()).isEnum()) {
            String errorMessage = "Valor inválido para o status. Favor utilizar um dos seguintes valores: " + Arrays.toString(ex.getRequiredType().getEnumConstants());
            standardException = new StandardException(HttpStatus.BAD_REQUEST.toString(), errorMessage, ex.getCause());
            standardException.setTimestamp(LocalDateTime.now());
            return ResponseEntity.badRequest().body(standardException);
        }

         return ResponseEntity.badRequest().body(standardException);
    }

    @Autowired
    public void setEnvironment(final Environment environment) {
        this.environment = environment;
    }
}
