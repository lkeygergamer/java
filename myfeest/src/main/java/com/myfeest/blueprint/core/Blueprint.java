package com.myfeest.blueprint.core;

import java.util.*;
import java.util.stream.Collectors;
import com.myfeest.blueprint.nodes.*;

/**
 * Representa um blueprint completo - um grafo de nós conectados
 * que pode ser executado como um sistema
 */
public class Blueprint {
    private String id;
    private String name;
    private String description;
    private Map<String, Node> nodes;
    private Map<String, Connection> connections;
    private Map<String, Object> metadata;
    private Date createdAt;
    private Date updatedAt;
    
    public Blueprint(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.description = "";
        this.nodes = new HashMap<>();
        this.connections = new HashMap<>();
        this.metadata = new HashMap<>();
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }
    
    public Blueprint(String id, String name) {
        this(name);
        this.id = id;
    }
    
    // Métodos para gerenciar nós
    public void addNode(Node node) {
        nodes.put(node.getId(), node);
        updatedAt = new Date();
    }
    
    public void removeNode(String nodeId) {
        nodes.remove(nodeId);
        // Remove conexões relacionadas
        connections.entrySet().removeIf(entry -> 
            entry.getValue().getFromNodeId().equals(nodeId) || 
            entry.getValue().getToNodeId().equals(nodeId));
        updatedAt = new Date();
    }
    
    public Node getNode(String nodeId) {
        return nodes.get(nodeId);
    }
    
    public List<Node> getNodes() {
        return new ArrayList<>(nodes.values());
    }
    
    public List<Node> getNodesByType(String type) {
        return nodes.values().stream()
                   .filter(node -> node.getType().equals(type))
                   .collect(Collectors.toList());
    }
    
    // Métodos para gerenciar conexões
    public void addConnection(Connection connection) {
        connections.put(connection.getId(), connection);
        updatedAt = new Date();
    }
    
    public void removeConnection(String connectionId) {
        connections.remove(connectionId);
        updatedAt = new Date();
    }
    
    public Connection getConnection(String connectionId) {
        return connections.get(connectionId);
    }
    
    public List<Connection> getConnections() {
        return new ArrayList<>(connections.values());
    }
    
    public List<Connection> getConnectionsFrom(String nodeId) {
        return connections.values().stream()
                         .filter(conn -> conn.getFromNodeId().equals(nodeId))
                         .collect(Collectors.toList());
    }
    
    public List<Connection> getConnectionsTo(String nodeId) {
        return connections.values().stream()
                         .filter(conn -> conn.getToNodeId().equals(nodeId))
                         .collect(Collectors.toList());
    }
    
    // Métodos de validação
    public boolean validate() {
        // Verifica se todos os nós são válidos
        for (Node node : nodes.values()) {
            if (!node.validate()) {
                return false;
            }
        }
        
        // Verifica se todas as conexões referenciam nós existentes
        for (Connection conn : connections.values()) {
            if (!nodes.containsKey(conn.getFromNodeId()) || 
                !nodes.containsKey(conn.getToNodeId())) {
                return false;
            }
        }
        
        return true;
    }
    
    // Métodos para análise do grafo
    public List<Node> getInputNodes() {
        return nodes.values().stream()
                   .filter(node -> node instanceof InputNode)
                   .collect(Collectors.toList());
    }
    
    /**
     * Retorna todos os nós que não têm saída (nós finais)
     */
    public List<Node> getOutputNodes() {
        List<Node> outputs = new ArrayList<>();
        for (Node node : nodes.values()) {
            if (getConnectionsFrom(node.getId()).isEmpty() && isConnected(node.getId())) {
                outputs.add(node);
            }
        }
        return outputs;
    }

    /**
     * Verifica se um nó está conectado a pelo menos uma conexão
     */
    public boolean isConnected(String nodeId) {
        for (Connection c : connections.values()) {
            if (c.getFromNodeId().equals(nodeId) || c.getToNodeId().equals(nodeId)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean hasCycles() {
        Set<String> visited = new HashSet<>();
        Set<String> recursionStack = new HashSet<>();
        
        for (Node node : nodes.values()) {
            if (hasCyclesDFS(node.getId(), visited, recursionStack)) {
                return true;
            }
        }
        
        return false;
    }
    
    private boolean hasCyclesDFS(String nodeId, Set<String> visited, Set<String> recursionStack) {
        if (recursionStack.contains(nodeId)) {
            return true; // Ciclo encontrado
        }
        
        if (visited.contains(nodeId)) {
            return false; // Já visitado, sem ciclo
        }
        
        visited.add(nodeId);
        recursionStack.add(nodeId);
        
        // Verifica todos os nós conectados
        for (Connection conn : getConnectionsFrom(nodeId)) {
            if (hasCyclesDFS(conn.getToNodeId(), visited, recursionStack)) {
                return true;
            }
        }
        
        recursionStack.remove(nodeId);
        return false;
    }
    
    /**
     * Obtém a ordem de execução dos nós (topológica)
     * Agora considera qualquer nó conectado como válido (estilo Unreal)
     */
    public List<Node> getExecutionOrder() {
        List<Node> order = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        for (Node node : nodes.values()) {
            if (isConnected(node.getId())) {
                visit(node, visited, order);
            }
        }
        Collections.reverse(order);
        return order;
    }

    private void visit(Node node, Set<String> visited, List<Node> order) {
        if (visited.contains(node.getId())) return;
        visited.add(node.getId());
        for (Connection conn : getConnectionsFrom(node.getId())) {
            Node to = nodes.get(conn.getToNodeId());
            if (to != null) visit(to, visited, order);
        }
        order.add(node);
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
    
    public Map<String, Object> getMetadata() {
        return metadata;
    }
    
    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
    
    public void setMetadata(String key, Object value) {
        this.metadata.put(key, value);
    }
    
    public Object getMetadata(String key) {
        return this.metadata.get(key);
    }
    
    public Date getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    
    public Date getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    @Override
    public String toString() {
        return String.format("Blueprint{id='%s', name='%s', nodes=%d, connections=%d}", 
                           id, name, nodes.size(), connections.size());
    }
} 