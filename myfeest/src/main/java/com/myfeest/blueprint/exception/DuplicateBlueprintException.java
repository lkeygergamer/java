package com.myfeest.blueprint.exception;

/**
 * Exceção lançada quando um blueprint duplicado é encontrado
 */
public class DuplicateBlueprintException extends RuntimeException {
    
    public DuplicateBlueprintException(String message) {
        super(message);
    }
    
    public DuplicateBlueprintException(String message, Throwable cause) {
        super(message, cause);
    }
} 