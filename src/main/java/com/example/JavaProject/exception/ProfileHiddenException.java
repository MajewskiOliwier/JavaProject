package com.example.JavaProject.exception;

public class ProfileHiddenException extends RuntimeException {

    public ProfileHiddenException() {}

    public ProfileHiddenException(String message) {
        super(message);
    }
}