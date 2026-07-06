package ru.rekklez.userservice.web.exception;

public class NotValidRefreshTokenException extends RuntimeException {
    public NotValidRefreshTokenException(String message) {
        super(message);
    }
}
