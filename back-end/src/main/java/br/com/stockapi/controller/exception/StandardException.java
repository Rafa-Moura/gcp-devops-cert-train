package br.com.stockapi.controller.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@JsonIgnoreProperties({"stackTrace", "cause", "suppressed", "localizedMessage"})
public class StandardException extends Exception {

    @Schema(description = "Data e hora da exceção", example = "2023-08-07T01:47:07.213Z")
    private LocalDateTime timestamp;
    @Schema(description = "Motivo da exceção ter sido lançada", example = "400 BAD_REQUEST")
    private String reason;

    public StandardException(String reason, String error) {
        super(error);
        this.reason = reason;
    }
    public StandardException(String reason, String error, Throwable throwable) {
        super(error, throwable);
        this.reason = reason;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

}
