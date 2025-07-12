package com.myfeest.blueprint.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * DTO para resposta de health check
 */
@Schema(description = "Resposta de health check")
public class HealthResponse {
    
    @Schema(description = "Status da aplicação")
    private String status;
    
    @Schema(description = "Timestamp da verificação")
    private LocalDateTime timestamp;
    
    @Schema(description = "Versão da aplicação")
    private String version;
    
    // Construtor privado para usar o builder
    private HealthResponse(String status, LocalDateTime timestamp, String version) {
        this.status = status;
        this.timestamp = timestamp;
        this.version = version;
    }
    
    // Builder
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private String status;
        private LocalDateTime timestamp;
        private String version;
        
        public Builder status(String status) {
            this.status = status;
            return this;
        }
        
        public Builder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }
        
        public Builder version(String version) {
            this.version = version;
            return this;
        }
        
        public HealthResponse build() {
            return new HealthResponse(status, timestamp, version);
        }
    }
    
    // Getters
    public String getStatus() {
        return status;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public String getVersion() {
        return version;
    }
    
    @Override
    public String toString() {
        return "HealthResponse{" +
                "status='" + status + '\'' +
                ", timestamp=" + timestamp +
                ", version='" + version + '\'' +
                '}';
    }
} 