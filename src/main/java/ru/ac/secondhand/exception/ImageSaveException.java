package ru.ac.secondhand.exception;

public class ImageSaveException extends RuntimeException{
    public ImageSaveException() {
    }

    public ImageSaveException(String message, Throwable cause) {
        super(message, cause);
    }
}
