package com.OpenLeaf.exception;


public class GenreException extends Exception {

    public GenreException(String message) {
        super(message);
    }

    public GenreException(String message, Throwable cause) {
        super(message, cause);
    }
}
