package br.com.stockapi.controller.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;

@JsonIgnoreProperties({"stackTrace", "cause", "suppressed", "localizedMessage"})
public class StandardError extends Exception {

    private LocalDateTime timestamp;
    private String reason;


    public StandardError(String reason, String error) {
        super(error);
        this.reason = reason;
    }
    public StandardError(String reason, String error, Throwable throwable) {
        super(error, throwable);
        this.reason = reason;
    }

    public StandardError(LocalDateTime timestamp, String reason, String error) {
        super(error);
        this.timestamp = timestamp;
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
