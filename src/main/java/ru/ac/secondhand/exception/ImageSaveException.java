package ru.ac.secondhand.exception;

public class ImageSaveException extends RuntimeException{
    public ImageSaveException(String message) {
        super(message);
    }

    public ImageSaveException(String message, Throwable cause) {
        super(message, cause);
    }
}
