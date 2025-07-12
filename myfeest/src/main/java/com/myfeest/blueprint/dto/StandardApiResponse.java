package com.myfeest.blueprint.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * DTO para respostas padronizadas da API
 */
@Schema(description = "Resposta padronizada da API")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StandardApiResponse<T> {
    
    @Schema(description = "Indica se a operação foi bem-sucedida")
    private boolean success;
    
    @Schema(description = "Mensagem da resposta")
    private String message;
    
    @Schema(description = "Dados da resposta")
    private T data;
    
    @Schema(description = "Código de erro (quando aplicável)")
    private String errorCode;
    
    @Schema(description = "Timestamp da resposta")
    private LocalDateTime timestamp;
    
    // Construtores
    public StandardApiResponse() {
        this.timestamp = LocalDateTime.now();
    }
    
    public StandardApiResponse(boolean success, String message, T data) {
        this();
        this.success = success;
        this.message = message;
        this.data = data;
    }
    
    public StandardApiResponse(boolean success, String message, T data, String errorCode) {
        this(success, message, data);
        this.errorCode = errorCode;
    }
    
    // Métodos estáticos para criar respostas
    public static <T> StandardApiResponse<T> success(String message, T data) {
        return new StandardApiResponse<>(true, message, data);
    }
    
    public static <T> StandardApiResponse<T> success(String message) {
        return new StandardApiResponse<>(true, message, null);
    }
    
    public static <T> StandardApiResponse<T> error(String message, String errorCode) {
        return new StandardApiResponse<>(false, message, null, errorCode);
    }
    
    public static <T> StandardApiResponse<T> error(String message) {
        return new StandardApiResponse<>(false, message, null);
    }
    
    // Getters e Setters
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public T getData() {
        return data;
    }
    
    public void setData(T data) {
        this.data = data;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    @Override
    public String toString() {
        return "StandardApiResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", errorCode='" + errorCode + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
} 