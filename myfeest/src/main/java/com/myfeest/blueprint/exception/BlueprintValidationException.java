package com.myfeest.blueprint.exception;

/**
 * Exceção lançada quando um blueprint é inválido
 */
public class BlueprintValidationException extends RuntimeException {
    
    public BlueprintValidationException(String message) {
        super(message);
    }
    
    public BlueprintValidationException(String message, Throwable cause) {
        super(message, cause);
    }
} 