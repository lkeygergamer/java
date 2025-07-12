package com.myfeest.blueprint.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myfeest.blueprint.core.Blueprint;
import com.myfeest.blueprint.core.Connection;
import com.myfeest.blueprint.dto.BlueprintCreateRequest;
import com.myfeest.blueprint.nodes.InputNode;
import com.myfeest.blueprint.nodes.OutputNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes de integração para BlueprintController
 */
@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
class BlueprintControllerIntegrationTest {
    
    @Autowired
    private WebApplicationContext webApplicationContext;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private MockMvc mockMvc;
    
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
    
    @Test
    void createBlueprint_WithValidData_ShouldReturn201() throws Exception {
        // Arrange
        BlueprintCreateRequest request = new BlueprintCreateRequest();
        request.setName("Test Blueprint");
        request.setDescription("Test Description");
        request.setBlueprintData(createValidBlueprintJson());
        
        // Act & Assert
        mockMvc.perform(post("/api/v1/blueprints")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("Test Blueprint"))
                .andExpect(jsonPath("$.data.description").value("Test Description"));
    }
    
    @Test
    void createBlueprint_WithInvalidData_ShouldReturn400() throws Exception {
        // Arrange
        BlueprintCreateRequest request = new BlueprintCreateRequest();
        // Nome vazio para causar erro de validação
        request.setName("");
        request.setDescription("Test Description");
        request.setBlueprintData(createValidBlueprintJson());
        
        // Act & Assert
        mockMvc.perform(post("/api/v1/blueprints")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void getBlueprints_ShouldReturn200() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/v1/blueprints"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
    
    @Test
    void getBlueprint_WithValidId_ShouldReturn200() throws Exception {
        // Primeiro criar um blueprint
        BlueprintCreateRequest createRequest = new BlueprintCreateRequest();
        createRequest.setName("Test Blueprint for Get");
        createRequest.setDescription("Test Description");
        createRequest.setBlueprintData(createValidBlueprintJson());
        
        String response = mockMvc.perform(post("/api/v1/blueprints")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        
        // Extrair ID da resposta
        String id = objectMapper.readTree(response).get("data").get("id").asText();
        
        // Buscar o blueprint criado
        mockMvc.perform(get("/api/v1/blueprints/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("Test Blueprint for Get"));
    }
    
    @Test
    void getBlueprint_WithInvalidId_ShouldReturn404() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/v1/blueprints/invalid-id"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void executeBlueprint_WithValidId_ShouldReturn200() throws Exception {
        // Primeiro criar um blueprint
        BlueprintCreateRequest createRequest = new BlueprintCreateRequest();
        createRequest.setName("Test Blueprint for Execute");
        createRequest.setDescription("Test Description");
        createRequest.setBlueprintData(createValidBlueprintJson());
        
        String response = mockMvc.perform(post("/api/v1/blueprints")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        
        // Extrair ID da resposta
        String id = objectMapper.readTree(response).get("data").get("id").asText();
        
        // Executar o blueprint
        mockMvc.perform(post("/api/v1/blueprints/" + id + "/execute"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
    
    @Test
    void executeBlueprintDirectly_WithValidBlueprint_ShouldReturn200() throws Exception {
        // Arrange
        Blueprint blueprint = createValidBlueprint();
        
        // Act & Assert
        mockMvc.perform(post("/api/v1/blueprints/execute")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(blueprint)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
    
    @Test
    void deleteBlueprint_WithValidId_ShouldReturn200() throws Exception {
        // Primeiro criar um blueprint
        BlueprintCreateRequest createRequest = new BlueprintCreateRequest();
        createRequest.setName("Test Blueprint for Delete");
        createRequest.setDescription("Test Description");
        createRequest.setBlueprintData(createValidBlueprintJson());
        
        String response = mockMvc.perform(post("/api/v1/blueprints")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        
        // Extrair ID da resposta
        String id = objectMapper.readTree(response).get("data").get("id").asText();
        
        // Deletar o blueprint
        mockMvc.perform(delete("/api/v1/blueprints/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
        
        // Verificar se foi deletado
        mockMvc.perform(get("/api/v1/blueprints/" + id))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void getStatistics_ShouldReturn200() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/v1/blueprints/statistics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").exists());
    }
    
    @Test
    void healthCheck_ShouldReturn200() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/v1/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("UP"));
    }
    
    // Métodos auxiliares
    private Blueprint createValidBlueprint() {
        Blueprint blueprint = new Blueprint("Test Blueprint");
        blueprint.setDescription("Test Description");
        
        InputNode inputNode = new InputNode("input_1", "Input Node");
        inputNode.setValue("test value");
        
        OutputNode outputNode = new OutputNode("output_1", "Output Node");
        
        blueprint.addNode(inputNode);
        blueprint.addNode(outputNode);
        
        Connection connection = new Connection("conn_1", inputNode.getId(), outputNode.getId());
        blueprint.addConnection(connection);
        
        return blueprint;
    }
    
    private String createValidBlueprintJson() {
        try {
            return objectMapper.writeValueAsString(createValidBlueprint());
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar JSON do blueprint", e);
        }
    }
} 