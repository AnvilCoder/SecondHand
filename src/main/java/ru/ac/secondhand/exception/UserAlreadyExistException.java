package ru.ac.secondhand.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.persistence.EntityNotFoundException;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserAlreadyExistException extends EntityNotFoundException {

    public UserAlreadyExistException(String message) {
        super(message);
    }
}