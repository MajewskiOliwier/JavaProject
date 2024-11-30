package com.example.JavaProject.exception;

public class RecipeNotFoundException extends RuntimeException {
    private static final String MESSAGE = "Recipe not found with %s: %s";

    public RecipeNotFoundException(long id) {
        super(String.format(MESSAGE, "id", id));
    }

    public RecipeNotFoundException(String filed, Object value) {
        super(String.format(MESSAGE, filed, value));
    }
}
