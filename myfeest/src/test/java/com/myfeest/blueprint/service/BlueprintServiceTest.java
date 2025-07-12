package com.myfeest.blueprint.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myfeest.blueprint.core.Blueprint;
import com.myfeest.blueprint.core.Connection;
import com.myfeest.blueprint.entity.BlueprintEntity;
import com.myfeest.blueprint.engine.BlueprintEngine;
import com.myfeest.blueprint.exception.BlueprintNotFoundException;
import com.myfeest.blueprint.exception.BlueprintValidationException;
import com.myfeest.blueprint.exception.DuplicateBlueprintException;
import com.myfeest.blueprint.nodes.InputNode;
import com.myfeest.blueprint.nodes.OutputNode;
import com.myfeest.blueprint.repository.BlueprintRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para BlueprintService
 */
@ExtendWith(MockitoExtension.class)
class BlueprintServiceTest {
    
    @Mock
    private BlueprintRepository blueprintRepository;
    
    @Mock
    private BlueprintEngine blueprintEngine;
    
    @Mock
    private ObjectMapper objectMapper;
    
    @InjectMocks
    private BlueprintService blueprintService;
    
    private Blueprint validBlueprint;
    private BlueprintEntity blueprintEntity;
    
    @BeforeEach
    void setUp() {
        // Cria um blueprint válido para testes
        validBlueprint = new Blueprint("Test Blueprint");
        validBlueprint.setDescription("Test Description");
        
        InputNode inputNode = new InputNode("input_1", "Input Node");
        inputNode.setValue("test value");
        
        OutputNode outputNode = new OutputNode("output_1", "Output Node");
        
        validBlueprint.addNode(inputNode);
        validBlueprint.addNode(outputNode);
        
        Connection connection = new Connection("conn_1", inputNode.getId(), outputNode.getId());
        validBlueprint.addConnection(connection);
        
        // Cria uma entidade para testes
        blueprintEntity = new BlueprintEntity();
        blueprintEntity.setId("test-id");
        blueprintEntity.setName("Test Blueprint");
        blueprintEntity.setDescription("Test Description");
        blueprintEntity.setIsActive(true);
    }
    
    @Test
    void createBlueprint_WithValidBlueprint_ShouldSucceed() throws Exception {
        // Arrange
        when(blueprintRepository.findByNameIgnoreCaseAndIsActiveTrue(anyString()))
            .thenReturn(Optional.empty());
        when(objectMapper.writeValueAsString(any(Blueprint.class)))
            .thenReturn("{\"name\":\"Test Blueprint\"}");
        when(blueprintRepository.save(any(BlueprintEntity.class)))
            .thenReturn(blueprintEntity);
        
        // Act
        BlueprintEntity result = blueprintService.createBlueprint(validBlueprint, "test-user");
        
        // Assert
        assertNotNull(result);
        assertEquals("test-id", result.getId());
        verify(blueprintRepository).save(any(BlueprintEntity.class));
    }
    
    @Test
    void createBlueprint_WithDuplicateName_ShouldThrowException() {
        // Arrange
        when(blueprintRepository.findByNameIgnoreCaseAndIsActiveTrue(anyString()))
            .thenReturn(Optional.of(blueprintEntity));
        
        // Act & Assert
        assertThrows(DuplicateBlueprintException.class, () -> {
            blueprintService.createBlueprint(validBlueprint, "test-user");
        });
    }
    
    @Test
    void createBlueprint_WithInvalidBlueprint_ShouldThrowException() {
        // Arrange
        Blueprint invalidBlueprint = new Blueprint("Invalid Blueprint");
        // Não adiciona nós, tornando inválido
        
        when(blueprintRepository.findByNameIgnoreCaseAndIsActiveTrue(anyString()))
            .thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(BlueprintValidationException.class, () -> {
            blueprintService.createBlueprint(invalidBlueprint, "test-user");
        });
    }
    
    @Test
    void getBlueprint_WithValidId_ShouldReturnBlueprint() {
        // Arrange
        when(blueprintRepository.findById("test-id"))
            .thenReturn(Optional.of(blueprintEntity));
        
        // Act
        BlueprintEntity result = blueprintService.getBlueprint("test-id");
        
        // Assert
        assertNotNull(result);
        assertEquals("test-id", result.getId());
    }
    
    @Test
    void getBlueprint_WithInvalidId_ShouldThrowException() {
        // Arrange
        when(blueprintRepository.findById("invalid-id"))
            .thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(BlueprintNotFoundException.class, () -> {
            blueprintService.getBlueprint("invalid-id");
        });
    }
    
    @Test
    void executeBlueprint_WithValidId_ShouldSucceed() throws Exception {
        // Arrange
        when(blueprintRepository.findById("test-id"))
            .thenReturn(Optional.of(blueprintEntity));
        when(objectMapper.readValue(anyString(), eq(Blueprint.class)))
            .thenReturn(validBlueprint);
        
        Map<String, Object> expectedResult = new HashMap<>();
        expectedResult.put("output", "test result");
        when(blueprintEngine.execute(any(Blueprint.class)))
            .thenReturn(expectedResult);
        
        // Act
        Map<String, Object> result = blueprintService.executeBlueprint("test-id");
        
        // Assert
        assertNotNull(result);
        assertEquals("test result", result.get("output"));
        verify(blueprintEngine).execute(any(Blueprint.class));
    }
    
    @Test
    void executeBlueprint_WithInvalidId_ShouldThrowException() {
        // Arrange
        when(blueprintRepository.findById("invalid-id"))
            .thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(BlueprintNotFoundException.class, () -> {
            blueprintService.executeBlueprint("invalid-id");
        });
    }
    
    @Test
    void deleteBlueprint_WithValidId_ShouldSucceed() {
        // Arrange
        when(blueprintRepository.findById("test-id"))
            .thenReturn(Optional.of(blueprintEntity));
        when(blueprintRepository.save(any(BlueprintEntity.class)))
            .thenReturn(blueprintEntity);
        
        // Act
        blueprintService.deleteBlueprint("test-id");
        
        // Assert
        verify(blueprintRepository).save(any(BlueprintEntity.class));
        assertFalse(blueprintEntity.getIsActive());
    }
    
    @Test
    void deleteBlueprint_WithInvalidId_ShouldThrowException() {
        // Arrange
        when(blueprintRepository.findById("invalid-id"))
            .thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(BlueprintNotFoundException.class, () -> {
            blueprintService.deleteBlueprint("invalid-id");
        });
    }
} 