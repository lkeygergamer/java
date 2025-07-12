package com.myfeest.blueprint.api;

import com.myfeest.blueprint.core.*;
import com.myfeest.blueprint.nodes.*;
import com.myfeest.blueprint.nodes.custom.*;
import com.myfeest.blueprint.nodes.advanced.*;
import com.myfeest.blueprint.engine.BlueprintEngine;
import com.myfeest.blueprint.serializer.SimpleJsonSerializer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * API REST para integração com o editor web
 * Permite criar, executar e gerenciar blueprints via HTTP
 */
public class BlueprintAPI {
    
    private BlueprintEngine engine;
    private SimpleJsonSerializer serializer;
    private Map<String, Blueprint> blueprints;
    private int requestCounter;
    
    public BlueprintAPI() {
        this.engine = new BlueprintEngine();
        this.serializer = new SimpleJsonSerializer();
        this.blueprints = new ConcurrentHashMap<>();
        this.requestCounter = 0;
    }
    
    /**
     * Recebe blueprint do frontend e o executa
     */
    public String executeBlueprintFromFrontend(String jsonBlueprint) {
        try {
            // Deserializa o blueprint do JSON
            Blueprint blueprint = deserializeBlueprint(jsonBlueprint);
            
            // Armazena o blueprint
            String blueprintId = "bp_" + (++requestCounter);
            blueprints.put(blueprintId, blueprint);
            
            // Executa o blueprint
            Map<String, Object> result = engine.execute(blueprint);
            
            // Retorna resultado em JSON
            return createResponse(true, "Blueprint executado com sucesso", result);
            
        } catch (Exception e) {
            return createResponse(false, "Erro ao executar blueprint: " + e.getMessage(), null);
        }
    }
    
    /**
     * Cria blueprint a partir dos dados do frontend
     */
    public String createBlueprintFromFrontend(String jsonData) {
        try {
            // Parse dos dados do frontend
            Map<String, Object> frontendData = parseFrontendData(jsonData);
            
            // Cria blueprint
            Blueprint blueprint = createBlueprintFromFrontendData(frontendData);
            
            // Armazena
            String blueprintId = "bp_" + (++requestCounter);
            blueprints.put(blueprintId, blueprint);
            
            // Serializa para retornar
            String blueprintJson = serializer.serialize(blueprint);
            
            return createResponse(true, "Blueprint criado com sucesso", 
                Map.of("blueprint_id", blueprintId, "blueprint", blueprintJson));
            
        } catch (Exception e) {
            return createResponse(false, "Erro ao criar blueprint: " + e.getMessage(), null);
        }
    }
    
    /**
     * Lista todos os blueprints armazenados
     */
    public String listBlueprints() {
        try {
            Map<String, Object> blueprintList = new HashMap<>();
            
            for (Map.Entry<String, Blueprint> entry : blueprints.entrySet()) {
                Blueprint bp = entry.getValue();
                blueprintList.put(entry.getKey(), Map.of(
                    "name", bp.getName(),
                    "description", bp.getDescription(),
                    "nodes_count", bp.getNodes().size(),
                    "connections_count", bp.getConnections().size()
                ));
            }
            
            return createResponse(true, "Blueprints listados com sucesso", blueprintList);
            
        } catch (Exception e) {
            return createResponse(false, "Erro ao listar blueprints: " + e.getMessage(), null);
        }
    }
    
    /**
     * Obtém um blueprint específico
     */
    public String getBlueprint(String blueprintId) {
        try {
            Blueprint blueprint = blueprints.get(blueprintId);
            if (blueprint == null) {
                return createResponse(false, "Blueprint não encontrado", null);
            }
            
            String blueprintJson = serializer.serialize(blueprint);
            return createResponse(true, "Blueprint obtido com sucesso", blueprintJson);
            
        } catch (Exception e) {
            return createResponse(false, "Erro ao obter blueprint: " + e.getMessage(), null);
        }
    }
    
    /**
     * Deleta um blueprint
     */
    public String deleteBlueprint(String blueprintId) {
        try {
            Blueprint removed = blueprints.remove(blueprintId);
            if (removed == null) {
                return createResponse(false, "Blueprint não encontrado", null);
            }
            
            return createResponse(true, "Blueprint deletado com sucesso", 
                Map.of("deleted_id", blueprintId));
            
        } catch (Exception e) {
            return createResponse(false, "Erro ao deletar blueprint: " + e.getMessage(), null);
        }
    }
    
    /**
     * Deserializa blueprint do JSON
     */
    private Blueprint deserializeBlueprint(String json) throws Exception {
        // Parse básico do JSON para extrair nós e conexões
        Map<String, Object> data = parseJson(json);
        
        String name = (String) data.getOrDefault("name", "Blueprint");
        String description = (String) data.getOrDefault("description", "");
        
        Blueprint blueprint = new Blueprint(name);
        blueprint.setDescription(description);
        
        // Processa nós
        Map<String, Object> nodesData = (Map<String, Object>) data.get("nodes");
        if (nodesData != null) {
            for (Map.Entry<String, Object> entry : nodesData.entrySet()) {
                Map<String, Object> nodeData = (Map<String, Object>) entry.getValue();
                Node node = createNodeFromData(nodeData);
                if (node != null) {
                    blueprint.addNode(node);
                }
            }
        }
        
        // Processa conexões
        Map<String, Object> connectionsData = (Map<String, Object>) data.get("connections");
        if (connectionsData != null) {
            for (Map.Entry<String, Object> entry : connectionsData.entrySet()) {
                Map<String, Object> connData = (Map<String, Object>) entry.getValue();
                Connection connection = createConnectionFromData(connData);
                if (connection != null) {
                    blueprint.addConnection(connection);
                }
            }
        }
        
        return blueprint;
    }
    
    /**
     * Cria nó a partir dos dados do frontend
     */
    private Node createNodeFromData(Map<String, Object> nodeData) {
        String id = (String) nodeData.get("id");
        String name = (String) nodeData.get("name");
        String type = (String) nodeData.get("type");
        
        if (id == null || name == null || type == null) {
            return null;
        }
        
        Node node = null;
        
        switch (type.toLowerCase()) {
            case "input":
                node = new InputNode(id, name);
                break;
            case "output":
                node = new OutputNode(id, name);
                break;
            case "transform":
                node = new TransformNode(id, name);
                break;
            case "filter":
                node = new FilterNode(id, name);
                break;
            case "ai":
                node = new AINode(id, name);
                break;
            case "database":
                node = new DatabaseNode(id, name);
                break;
            case "api":
                node = new APINode(id, name);
                break;
            case "advanced_transform":
                node = new AdvancedTransformNode(id, name);
                break;
            case "advanced_filter":
                node = new AdvancedFilterNode(id, name);
                break;
            case "text_input":
                node = new TextInputNode(id, name);
                break;
            case "text_output":
                node = new TextOutputNode(id, name);
                break;
            default:
                System.err.println("Tipo de nó não reconhecido: " + type);
                return null;
        }
        
        // Configura propriedades do nó
        Map<String, Object> properties = (Map<String, Object>) nodeData.get("properties");
        if (properties != null) {
            configureNodeProperties(node, properties);
        }
        
        return node;
    }
    
    /**
     * Configura propriedades de um nó
     */
    private void configureNodeProperties(Node node, Map<String, Object> properties) {
        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            
            switch (node.getType()) {
                case "TransformNode":
                    TransformNode transformNode = (TransformNode) node;
                    if ("operation".equals(key)) {
                        transformNode.setOperation((String) value);
                    }
                    break;
                case "FilterNode":
                    FilterNode filterNode = (FilterNode) node;
                    if ("condition".equals(key)) {
                        filterNode.setCondition((String) value);
                    }
                    break;
                case "AINode":
                    AINode aiNode = (AINode) node;
                    if ("model".equals(key)) {
                        aiNode.setModel((String) value);
                    } else if ("task".equals(key)) {
                        aiNode.setTask((String) value);
                    }
                    break;
                case "DatabaseNode":
                    DatabaseNode dbNode = (DatabaseNode) node;
                    if ("operation".equals(key)) {
                        dbNode.setOperation((String) value);
                    } else if ("table".equals(key)) {
                        dbNode.setTable((String) value);
                    }
                    break;
                case "APINode":
                    APINode apiNode = (APINode) node;
                    if ("method".equals(key)) {
                        apiNode.setMethod((String) value);
                    } else if ("url".equals(key)) {
                        apiNode.setUrl((String) value);
                    }
                    break;
                case "AdvancedTransformNode":
                    AdvancedTransformNode advTransformNode = (AdvancedTransformNode) node;
                    if ("operation".equals(key)) {
                        advTransformNode.setOperation((String) value);
                    }
                    break;
                case "AdvancedFilterNode":
                    AdvancedFilterNode advFilterNode = (AdvancedFilterNode) node;
                    if ("condition".equals(key)) {
                        advFilterNode.setCondition((String) value);
                    }
                    break;
                case "InputNode":
                    InputNode inputNode = (InputNode) node;
                    if ("value".equals(key)) {
                        inputNode.setValue(value);
                    }
                    break;
            }
        }
    }
    
    /**
     * Cria conexão a partir dos dados do frontend
     */
    private Connection createConnectionFromData(Map<String, Object> connData) {
        String id = (String) connData.get("id");
        String fromNodeId = (String) connData.get("fromNodeId");
        String toNodeId = (String) connData.get("toNodeId");
        
        if (id == null || fromNodeId == null || toNodeId == null) {
            return null;
        }
        
        return new Connection(id, fromNodeId, toNodeId);
    }
    
    /**
     * Cria blueprint a partir dos dados do frontend
     */
    private Blueprint createBlueprintFromFrontendData(Map<String, Object> frontendData) {
        String name = (String) frontendData.getOrDefault("name", "Blueprint");
        String description = (String) frontendData.getOrDefault("description", "");
        
        Blueprint blueprint = new Blueprint(name);
        blueprint.setDescription(description);
        
        // Processa nós
        List<Map<String, Object>> nodesList = (List<Map<String, Object>>) frontendData.get("nodes");
        if (nodesList != null) {
            for (Map<String, Object> nodeData : nodesList) {
                Node node = createNodeFromData(nodeData);
                if (node != null) {
                    blueprint.addNode(node);
                }
            }
        }
        
        // Processa conexões
        List<Map<String, Object>> connectionsList = (List<Map<String, Object>>) frontendData.get("connections");
        if (connectionsList != null) {
            for (Map<String, Object> connData : connectionsList) {
                Connection connection = createConnectionFromData(connData);
                if (connection != null) {
                    blueprint.addConnection(connection);
                }
            }
        }
        
        return blueprint;
    }
    
    /**
     * Parse básico de JSON (simplificado)
     */
    private Map<String, Object> parseJson(String json) {
        // Implementação simplificada - em produção usar Jackson ou Gson
        Map<String, Object> result = new HashMap<>();
        
        // Remove espaços e quebras
        json = json.replaceAll("\\s+", "");
        
        // Parse básico de objetos
        if (json.startsWith("{") && json.endsWith("}")) {
            json = json.substring(1, json.length() - 1);
            
            String[] pairs = json.split(",");
            for (String pair : pairs) {
                if (pair.contains(":")) {
                    String[] keyValue = pair.split(":", 2);
                    String key = keyValue[0].replaceAll("\"", "");
                    String value = keyValue[1].replaceAll("\"", "");
                    result.put(key, value);
                }
            }
        }
        
        return result;
    }
    
    /**
     * Parse dos dados do frontend
     */
    private Map<String, Object> parseFrontendData(String jsonData) {
        // Parse simplificado - em produção usar biblioteca JSON
        Map<String, Object> result = new HashMap<>();
        
        // Remove espaços e quebras
        jsonData = jsonData.replaceAll("\\s+", "");
        
        // Parse básico
        if (jsonData.startsWith("{") && jsonData.endsWith("}")) {
            jsonData = jsonData.substring(1, jsonData.length() - 1);
            
            String[] pairs = jsonData.split(",");
            for (String pair : pairs) {
                if (pair.contains(":")) {
                    String[] keyValue = pair.split(":", 2);
                    String key = keyValue[0].replaceAll("\"", "");
                    String value = keyValue[1].replaceAll("\"", "");
                    result.put(key, value);
                }
            }
        }
        
        return result;
    }
    
    /**
     * Cria resposta JSON padronizada
     */
    private String createResponse(boolean success, String message, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        response.put("message", message);
        response.put("data", data);
        response.put("timestamp", new Date().toString());
        
        // Serialização simples para JSON
        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"success\":").append(success).append(",");
        json.append("\"message\":\"").append(message).append("\",");
        json.append("\"timestamp\":\"").append(new Date().toString()).append("\"");
        
        if (data != null) {
            json.append(",\"data\":");
            if (data instanceof String) {
                json.append("\"").append(data.toString().replace("\"", "\\\"")).append("\"");
            } else {
                json.append(data.toString());
            }
        }
        
        json.append("}");
        return json.toString();
    }
    
    /**
     * Obtém estatísticas da API
     */
    public String getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("total_blueprints", blueprints.size());
        stats.put("total_requests", requestCounter);
        stats.put("engine_status", "running");
        stats.put("uptime", System.currentTimeMillis());
        
        return createResponse(true, "Estatísticas obtidas com sucesso", stats);
    }
} 