package com.myfeest.blueprint.core;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Classe base para todos os nós do sistema de blueprint
 * Representa um bloco funcional que pode ser conectado a outros nós
 */
public abstract class Node {
    
    protected String id;
    protected String name;
    protected String type;
    protected Map<String, Object> properties;
    protected Map<String, Object> inputs;
    protected Map<String, Object> outputs;
    protected Position position;
    
    public Node(String id, String name) {
        this.id = id != null ? id : UUID.randomUUID().toString();
        this.name = name;
        this.type = this.getClass().getSimpleName();
        this.properties = new HashMap<>();
        this.inputs = new HashMap<>();
        this.outputs = new HashMap<>();
        this.position = new Position(0, 0);
    }
    
    /**
     * Executa a lógica do nó
     * @param context Contexto de execução com dados de entrada
     * @return Resultado da execução
     */
    public abstract Map<String, Object> execute(ExecutionContext context);
    
    /**
     * Valida se o nó está configurado corretamente
     * @return true se válido, false caso contrário
     */
    public abstract boolean validate();
    
    /**
     * Retorna os tipos de dados que este nó aceita como entrada
     */
    public abstract Map<String, String> getInputTypes();
    
    /**
     * Retorna os tipos de dados que este nó produz como saída
     */
    public abstract Map<String, String> getOutputTypes();
    
    // Métodos padrão para identificação de entrada/saída (estilo Unreal)
    public boolean isInputNode() {
        return false;
    }
    public boolean isOutputNode() {
        return false;
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
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public Map<String, Object> getProperties() {
        return properties;
    }
    
    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }
    
    public void setProperty(String key, Object value) {
        this.properties.put(key, value);
    }
    
    public Object getProperty(String key) {
        return this.properties.get(key);
    }
    
    public Map<String, Object> getInputs() {
        return inputs;
    }
    
    public void setInputs(Map<String, Object> inputs) {
        this.inputs = inputs;
    }
    
    public void setInput(String key, Object value) {
        this.inputs.put(key, value);
    }
    
    public Object getInput(String key) {
        return this.inputs.get(key);
    }
    
    public Map<String, Object> getOutputs() {
        return outputs;
    }
    
    public void setOutputs(Map<String, Object> outputs) {
        this.outputs = outputs;
    }
    
    public void setOutput(String key, Object value) {
        this.outputs.put(key, value);
    }
    
    public Object getOutput(String key) {
        return this.outputs.get(key);
    }
    
    public Position getPosition() {
        return position;
    }
    
    public void setPosition(Position position) {
        this.position = position;
    }
    
    @Override
    public String toString() {
        return String.format("Node{id='%s', name='%s', type='%s'}", id, name, type);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Node node = (Node) obj;
        return id.equals(node.id);
    }
    
    @Override
    public int hashCode() {
        return id.hashCode();
    }
} 