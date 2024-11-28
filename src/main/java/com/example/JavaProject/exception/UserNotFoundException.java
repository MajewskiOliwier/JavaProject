package com.example.JavaProject.exception;

public class UserNotFoundException extends RuntimeException{

    private static final String MESSAGE = "User not found with %s: %s";

    public UserNotFoundException(Long value){
        super(MESSAGE.formatted("id", value));
    }

    public UserNotFoundException(String field, Object value){
        super(MESSAGE.formatted(field, value));
    }
}