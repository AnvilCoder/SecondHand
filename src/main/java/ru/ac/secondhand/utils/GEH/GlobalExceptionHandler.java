package ru.ac.secondhand.utils.GEH;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.ac.secondhand.exception.AdNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<Object> buildResponseEntity(ApiMessageError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(AdNotFoundException.class)
    public ResponseEntity<Object> AdNotFoundException(AdNotFoundException e) {
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
