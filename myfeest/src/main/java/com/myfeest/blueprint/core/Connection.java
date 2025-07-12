package com.myfeest.blueprint.core;

import java.util.UUID;

/**
 * Representa uma conexão entre dois nós no sistema de blueprint
 */
public class Connection {
    private String id;
    private String fromNodeId;
    private String toNodeId;
    private String fromPort;
    private String toPort;
    private ConnectionType type;
    
    public enum ConnectionType {
        DATA,      // Fluxo de dados
        CONTROL,   // Fluxo de controle
        EVENT      // Eventos
    }
    
    public Connection(String id, String fromNodeId, String toNodeId) {
        this.id = id != null ? id : UUID.randomUUID().toString();
        this.fromNodeId = fromNodeId;
        this.toNodeId = toNodeId;
        this.fromPort = "output";
        this.toPort = "input";
        this.type = ConnectionType.DATA;
    }
    
    public Connection(String id, String fromNodeId, String toNodeId, String fromPort, String toPort) {
        this(id, fromNodeId, toNodeId);
        this.fromPort = fromPort;
        this.toPort = toPort;
    }
    
    public Connection(String id, String fromNodeId, String toNodeId, String fromPort, String toPort, ConnectionType type) {
        this(id, fromNodeId, toNodeId, fromPort, toPort);
        this.type = type;
    }
    
    // Getters e Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getFromNodeId() {
        return fromNodeId;
    }
    
    public void setFromNodeId(String fromNodeId) {
        this.fromNodeId = fromNodeId;
    }
    
    public String getToNodeId() {
        return toNodeId;
    }
    
    public void setToNodeId(String toNodeId) {
        this.toNodeId = toNodeId;
    }
    
    public String getFromPort() {
        return fromPort;
    }
    
    public void setFromPort(String fromPort) {
        this.fromPort = fromPort;
    }
    
    public String getToPort() {
        return toPort;
    }
    
    public void setToPort(String toPort) {
        this.toPort = toPort;
    }
    
    public ConnectionType getType() {
        return type;
    }
    
    public void setType(ConnectionType type) {
        this.type = type;
    }
    
    @Override
    public String toString() {
        return String.format("Connection{id='%s', from='%s', to='%s', type='%s'}", 
                           id, fromNodeId, toNodeId, type);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Connection that = (Connection) obj;
        return id.equals(that.id);
    }
    
    @Override
    public int hashCode() {
        return id.hashCode();
    }
} 