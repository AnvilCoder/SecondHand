package ru.ac.secondhand.exception;

import org.springframework.security.access.AccessDeniedException;

public class IncorrectPasswordException extends AccessDeniedException {

    public IncorrectPasswordException(String message) {
        super(message);
    }
}