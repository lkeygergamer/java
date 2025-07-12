package com.myfeest.blueprint.exception;

/**
 * Exceção lançada quando um blueprint não é encontrado
 */
public class BlueprintNotFoundException extends RuntimeException {
    
    public BlueprintNotFoundException(String message) {
        super(message);
    }
    
    public BlueprintNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
} 