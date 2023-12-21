package ru.ac.secondhand.utils.GEH;

import liquibase.pro.packaged.O;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.ac.secondhand.exception.AdNotFoundException;
import ru.ac.secondhand.exception.CommentNotFoundException;
import ru.ac.secondhand.exception.UserNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<Object> buildResponseEntity(ApiMessageError apiMessageError) {
        return new ResponseEntity<>(apiMessageError, apiMessageError.getStatus());
    }

    @ExceptionHandler(AdNotFoundException.class)
    public ResponseEntity<Object> handleAdNotFoundException(AdNotFoundException e) {
        ApiMessageError apiMessageError = new ApiMessageError(HttpStatus.NOT_FOUND, e.getMessage());
        return buildResponseEntity(apiMessageError);
    }

    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<Object> handleCommentNotFoundException(CommentNotFoundException e) {
        ApiMessageError apiMessageError = new ApiMessageError(HttpStatus.NOT_FOUND, e.getMessage());
        return buildResponseEntity(apiMessageError);
    }

    @ExceptionHandler(UserNotFoundException.class)
    protected ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException e) {
        ApiMessageError apiMessageError = new ApiMessageError(HttpStatus.NOT_FOUND, e.getMessage());
        return buildResponseEntity(apiMessageError);
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<Object> handleAccessDenied(AccessDeniedException ex) {
        ApiMessageError apiMessageError = new ApiMessageError(HttpStatus.FORBIDDEN,
                "Доступ запрещен!");
        return buildResponseEntity(apiMessageError);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleAll(Exception ex) {
        ApiMessageError apiMessageError = new ApiMessageError(HttpStatus.INTERNAL_SERVER_ERROR,
                "Произошла внутренняя ошибка!");
        return buildResponseEntity(apiMessageError);
    }

}
