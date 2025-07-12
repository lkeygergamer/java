package com.myfeest.blueprint.dto;

import com.myfeest.blueprint.core.Blueprint;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Map;

/**
 * DTO para requisição de atualização de blueprint
 */
@Schema(description = "Requisição para atualizar blueprint")
public class BlueprintUpdateRequest {
    
    @Schema(description = "Nome do blueprint", example = "Meu Blueprint Atualizado")
    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 255, message = "Nome deve ter no máximo 255 caracteres")
    private String name;
    
    @Schema(description = "Descrição do blueprint", example = "Blueprint atualizado para processamento de dados")
    @Size(max = 1000, message = "Descrição deve ter no máximo 1000 caracteres")
    private String description;
    
    @Schema(description = "Dados do blueprint em formato JSON")
    @NotBlank(message = "Dados do blueprint são obrigatórios")
    private String blueprintData;
    
    @Schema(description = "Metadados do blueprint")
    private Map<String, String> metadata;
    
    // Construtores
    public BlueprintUpdateRequest() {}
    
    public BlueprintUpdateRequest(String name, String description, String blueprintData) {
        this.name = name;
        this.description = description;
        this.blueprintData = blueprintData;
    }
    
    public BlueprintUpdateRequest(String name, String description, String blueprintData, Map<String, String> metadata) {
        this(name, description, blueprintData);
        this.metadata = metadata;
    }
    
    // Método para converter para Blueprint
    public Blueprint toBlueprint() {
        Blueprint blueprint = new Blueprint(name);
        blueprint.setDescription(description != null ? description : "");
        
        if (metadata != null) {
            metadata.forEach(blueprint::setMetadata);
        }
        
        return blueprint;
    }
    
    // Getters e Setters
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
    
    public String getBlueprintData() {
        return blueprintData;
    }
    
    public void setBlueprintData(String blueprintData) {
        this.blueprintData = blueprintData;
    }
    
    public Map<String, String> getMetadata() {
        return metadata;
    }
    
    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }
    
    @Override
    public String toString() {
        return "BlueprintUpdateRequest{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", blueprintData='" + blueprintData + '\'' +
                ", metadata=" + metadata +
                '}';
    }
} 