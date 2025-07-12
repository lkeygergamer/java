package com.myfeest.blueprint.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myfeest.blueprint.entity.BlueprintEntity;
import com.myfeest.blueprint.repository.BlueprintRepository;
import com.myfeest.blueprint.core.Blueprint;
import com.myfeest.blueprint.engine.BlueprintEngine;
import com.myfeest.blueprint.exception.BlueprintNotFoundException;
import com.myfeest.blueprint.exception.BlueprintValidationException;
import com.myfeest.blueprint.exception.DuplicateBlueprintException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Serviço de blueprint com cache e validações empresariais
 */
@Service
@Validated
@Transactional
public class BlueprintService {
    
    private static final Logger logger = LoggerFactory.getLogger(BlueprintService.class);
    
    private final BlueprintRepository blueprintRepository;
    private final BlueprintEngine blueprintEngine;
    private final ObjectMapper objectMapper;
    
    @Autowired
    public BlueprintService(BlueprintRepository blueprintRepository, 
                          BlueprintEngine blueprintEngine,
                          ObjectMapper objectMapper) {
        this.blueprintRepository = blueprintRepository;
        this.blueprintEngine = blueprintEngine;
        this.objectMapper = objectMapper;
    }
    
    /**
     * Cria um novo blueprint
     */
    @CacheEvict(value = "blueprints", allEntries = true)
    public BlueprintEntity createBlueprint(@Valid @NotNull Blueprint blueprint, 
                                         @NotBlank String createdBy) {
        logger.info("Criando novo blueprint: {}", blueprint.getName());
        
        // Verifica se já existe um blueprint com o mesmo nome
        Optional<BlueprintEntity> existing = blueprintRepository
            .findByNameIgnoreCaseAndIsActiveTrue(blueprint.getName());
        
        if (existing.isPresent()) {
            throw new DuplicateBlueprintException("Blueprint com nome '" + blueprint.getName() + "' já existe");
        }
        
        // Valida o blueprint
        if (!blueprint.validate()) {
            throw new BlueprintValidationException("Blueprint inválido");
        }
        
        if (blueprint.hasCycles()) {
            throw new BlueprintValidationException("Blueprint contém ciclos");
        }
        
        // Serializa o blueprint para JSON
        String blueprintData;
        try {
            blueprintData = objectMapper.writeValueAsString(blueprint);
        } catch (JsonProcessingException e) {
            throw new BlueprintValidationException("Erro ao serializar blueprint: " + e.getMessage());
        }
        
        // Cria a entidade
        BlueprintEntity entity = new BlueprintEntity(
            blueprint.getName(),
            blueprint.getDescription(),
            blueprintData
        );
        entity.setCreatedBy(createdBy);
        
        // Salva no banco
        BlueprintEntity saved = blueprintRepository.save(entity);
        
        logger.info("Blueprint criado com sucesso: {}", saved.getId());
        return saved;
    }
    
    /**
     * Atualiza um blueprint existente
     */
    @CacheEvict(value = "blueprints", allEntries = true)
    public BlueprintEntity updateBlueprint(@NotBlank String id, 
                                         @Valid @NotNull Blueprint blueprint,
                                         @NotBlank String updatedBy) {
        logger.info("Atualizando blueprint: {}", id);
        
        BlueprintEntity existing = blueprintRepository.findById(id)
            .orElseThrow(() -> new BlueprintNotFoundException("Blueprint não encontrado: " + id));
        
        // Verifica se o blueprint está ativo
        if (!existing.getIsActive()) {
            throw new BlueprintNotFoundException("Blueprint não está ativo: " + id);
        }
        
        // Valida o blueprint
        if (!blueprint.validate()) {
            throw new BlueprintValidationException("Blueprint inválido");
        }
        
        if (blueprint.hasCycles()) {
            throw new BlueprintValidationException("Blueprint contém ciclos");
        }
        
        // Serializa o blueprint para JSON
        String blueprintData;
        try {
            blueprintData = objectMapper.writeValueAsString(blueprint);
        } catch (JsonProcessingException e) {
            throw new BlueprintValidationException("Erro ao serializar blueprint: " + e.getMessage());
        }
        
        // Atualiza os campos
        existing.setName(blueprint.getName());
        existing.setDescription(blueprint.getDescription());
        existing.setBlueprintData(blueprintData);
        existing.setVersion(existing.getVersion() + 1);
        existing.setCreatedBy(updatedBy);
        
        // Salva no banco
        BlueprintEntity saved = blueprintRepository.save(existing);
        
        logger.info("Blueprint atualizado com sucesso: {}", saved.getId());
        return saved;
    }
    
    /**
     * Busca um blueprint por ID
     */
    @Cacheable(value = "blueprints", key = "#id")
    public BlueprintEntity getBlueprint(@NotBlank String id) {
        logger.debug("Buscando blueprint: {}", id);
        
        return blueprintRepository.findById(id)
            .filter(BlueprintEntity::getIsActive)
            .orElseThrow(() -> new BlueprintNotFoundException("Blueprint não encontrado: " + id));
    }
    
    /**
     * Busca blueprints paginados
     */
    public Page<BlueprintEntity> getBlueprints(Pageable pageable) {
        logger.debug("Buscando blueprints paginados");
        return blueprintRepository.findByIsActiveTrue(pageable);
    }
    
    /**
     * Busca blueprints por nome
     */
    public List<BlueprintEntity> searchBlueprintsByName(@NotBlank String name) {
        logger.debug("Buscando blueprints por nome: {}", name);
        return blueprintRepository.findByNameContainingIgnoreCaseAndIsActiveTrue(name);
    }
    
    /**
     * Busca blueprints por usuário criador
     */
    public List<BlueprintEntity> getBlueprintsByUser(@NotBlank String createdBy) {
        logger.debug("Buscando blueprints do usuário: {}", createdBy);
        return blueprintRepository.findByCreatedByAndIsActiveTrue(createdBy);
    }
    
    /**
     * Executa um blueprint
     */
    public Map<String, Object> executeBlueprint(@NotBlank String id) {
        logger.info("Executando blueprint: {}", id);
        
        BlueprintEntity entity = getBlueprint(id);
        
        // Deserializa o blueprint
        Blueprint blueprint;
        try {
            blueprint = objectMapper.readValue(entity.getBlueprintData(), Blueprint.class);
        } catch (JsonProcessingException e) {
            throw new BlueprintValidationException("Erro ao deserializar blueprint: " + e.getMessage());
        }
        
        // Executa o blueprint
        try {
            Map<String, Object> result = blueprintEngine.execute(blueprint);
            logger.info("Blueprint executado com sucesso: {}", id);
            return result;
        } catch (Exception e) {
            logger.error("Erro ao executar blueprint {}: {}", id, e.getMessage());
            throw new RuntimeException("Erro ao executar blueprint: " + e.getMessage(), e);
        }
    }
    
    /**
     * Executa um blueprint diretamente (sem salvar)
     */
    public Map<String, Object> executeBlueprintDirectly(@Valid @NotNull Blueprint blueprint) {
        logger.info("Executando blueprint diretamente: {}", blueprint.getName());
        
        // Valida o blueprint
        if (!blueprint.validate()) {
            throw new BlueprintValidationException("Blueprint inválido");
        }
        
        if (blueprint.hasCycles()) {
            throw new BlueprintValidationException("Blueprint contém ciclos");
        }
        
        // Executa o blueprint
        try {
            Map<String, Object> result = blueprintEngine.execute(blueprint);
            logger.info("Blueprint executado com sucesso: {}", blueprint.getName());
            return result;
        } catch (Exception e) {
            logger.error("Erro ao executar blueprint {}: {}", blueprint.getName(), e.getMessage());
            throw new RuntimeException("Erro ao executar blueprint: " + e.getMessage(), e);
        }
    }
    
    /**
     * Deleta um blueprint (soft delete)
     */
    @CacheEvict(value = "blueprints", allEntries = true)
    public void deleteBlueprint(@NotBlank String id) {
        logger.info("Deletando blueprint: {}", id);
        
        BlueprintEntity entity = blueprintRepository.findById(id)
            .orElseThrow(() -> new BlueprintNotFoundException("Blueprint não encontrado: " + id));
        
        entity.setIsActive(false);
        blueprintRepository.save(entity);
        
        logger.info("Blueprint deletado com sucesso: {}", id);
    }
    
    /**
     * Busca estatísticas dos blueprints
     */
    public Map<String, Object> getStatistics() {
        logger.debug("Buscando estatísticas dos blueprints");
        
        LocalDateTime startDate = LocalDateTime.now().minusDays(30);
        Object[] stats = blueprintRepository.getStatistics(startDate);
        
        return Map.of(
            "totalBlueprints", stats[0],
            "recentBlueprints", stats[1],
            "uniqueUsers", stats[2],
            "period", "30 days"
        );
    }
    
    /**
     * Busca versões de um blueprint
     */
    public List<BlueprintEntity> getBlueprintVersions(@NotBlank String name) {
        logger.debug("Buscando versões do blueprint: {}", name);
        return blueprintRepository.findByNameIgnoreCaseAndIsActiveTrue(name)
            .stream()
            .toList();
    }
    
    /**
     * Busca a versão mais recente de um blueprint
     */
    public Optional<BlueprintEntity> getLatestVersion(@NotBlank String name) {
        logger.debug("Buscando versão mais recente do blueprint: {}", name);
        return blueprintRepository.findByNameIgnoreCaseAndIsActiveTrue(name);
    }
} 