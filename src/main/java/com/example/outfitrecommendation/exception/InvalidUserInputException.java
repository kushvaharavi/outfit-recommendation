package com.example.outfitrecommendation.exception;

/**
 * Exception thrown when user input is invalid.
 */
public class InvalidUserInputException extends RuntimeException {
    public InvalidUserInputException(final String message) {
        super(message);
    }
}
