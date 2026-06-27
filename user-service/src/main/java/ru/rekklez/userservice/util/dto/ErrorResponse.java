package ru.rekklez.userservice.util.dto;

import java.time.Instant;
import java.util.Map;

public record ErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path,
        Map<String, String> validations
) {

    public ErrorResponse(int status, String error, String message, String path, Map<String, String> validations) {
        this(Instant.now(), status, error, message, path, validations);
    }
}
