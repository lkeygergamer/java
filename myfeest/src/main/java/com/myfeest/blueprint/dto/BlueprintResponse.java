package com.myfeest.blueprint.dto;

import com.myfeest.blueprint.entity.BlueprintEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO para resposta de blueprint
 */
@Schema(description = "Resposta de blueprint")
public class BlueprintResponse {
    
    @Schema(description = "ID único do blueprint")
    private String id;
    
    @Schema(description = "Nome do blueprint")
    private String name;
    
    @Schema(description = "Descrição do blueprint")
    private String description;
    
    @Schema(description = "Versão do blueprint")
    private Integer version;
    
    @Schema(description = "Indica se o blueprint está ativo")
    private Boolean isActive;
    
    @Schema(description = "Usuário que criou o blueprint")
    private String createdBy;
    
    @Schema(description = "Data de criação")
    private LocalDateTime createdAt;
    
    @Schema(description = "Data da última atualização")
    private LocalDateTime updatedAt;
    
    @Schema(description = "Metadados do blueprint")
    private Map<String, String> metadata;
    
    // Construtor
    public BlueprintResponse() {}
    
    public BlueprintResponse(String id, String name, String description, Integer version,
                           Boolean isActive, String createdBy, LocalDateTime createdAt,
                           LocalDateTime updatedAt, Map<String, String> metadata) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.version = version;
        this.isActive = isActive;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.metadata = metadata;
    }
    
    // Método estático para criar a partir de uma entidade
    public static BlueprintResponse fromEntity(BlueprintEntity entity) {
        return new BlueprintResponse(
            entity.getId(),
            entity.getName(),
            entity.getDescription(),
            entity.getVersion(),
            entity.getIsActive(),
            entity.getCreatedBy(),
            entity.getCreatedAt(),
            entity.getUpdatedAt(),
            entity.getMetadata()
        );
    }
    
    // Getters e Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Integer getVersion() {
        return version;
    }
    
    public void setVersion(Integer version) {
        this.version = version;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    public String getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public Map<String, String> getMetadata() {
        return metadata;
    }
    
    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }
    
    @Override
    public String toString() {
        return "BlueprintResponse{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", version=" + version +
                ", isActive=" + isActive +
                ", createdBy='" + createdBy + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", metadata=" + metadata +
                '}';
    }
} 