package com.myfeest.blueprint.nodes.custom;

import com.myfeest.blueprint.core.ExecutionContext;
import com.myfeest.blueprint.core.Node;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Nó personalizado para operações de banco de dados
 */
public class DatabaseNode extends Node {
    
    private String operation; // SELECT, INSERT, UPDATE, DELETE
    private String table;
    private Map<String, Object> parameters;
    private Random random;
    
    public DatabaseNode(String id, String name) {
        super(id, name);
        this.operation = "SELECT";
        this.table = "users";
        this.parameters = new HashMap<>();
        this.random = new Random();
    }
    
    public DatabaseNode(String id, String name, String operation, String table) {
        super(id, name);
        this.operation = operation;
        this.table = table;
        this.parameters = new HashMap<>();
        this.random = new Random();
    }
    
    @Override
    public Map<String, Object> execute(ExecutionContext context) {
        Map<String, Object> result = new HashMap<>();
        
        // Simula operação de banco de dados
        Object dbResult = simulateDatabaseOperation(operation, table, parameters);
        
        result.put("value", dbResult);
        result.put("output", dbResult);
        result.put("operation", operation);
        result.put("table", table);
        result.put("rows_affected", random.nextInt(100) + 1);
        result.put("execution_time", random.nextInt(500) + 50);
        result.put("success", true);
        
        // Armazena o resultado no contexto
        context.setNodeResult(getId(), result);
        
        return result;
    }
    
    private Object simulateDatabaseOperation(String operation, String table, Map<String, Object> params) {
        switch (operation.toUpperCase()) {
            case "SELECT":
                return simulateSelect(table, params);
            case "INSERT":
                return simulateInsert(table, params);
            case "UPDATE":
                return simulateUpdate(table, params);
            case "DELETE":
                return simulateDelete(table, params);
            default:
                return "Operação não suportada: " + operation;
        }
    }
    
    private Object simulateSelect(String table, Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        result.put("query", "SELECT * FROM " + table);
        result.put("records", random.nextInt(50) + 1);
        result.put("columns", new String[]{"id", "name", "email", "created_at"});
        result.put("data", generateMockData());
        return result;
    }
    
    private Object simulateInsert(String table, Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        result.put("query", "INSERT INTO " + table + " VALUES (...)");
        result.put("inserted_id", random.nextInt(10000));
        result.put("affected_rows", 1);
        return result;
    }
    
    private Object simulateUpdate(String table, Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        result.put("query", "UPDATE " + table + " SET ... WHERE ...");
        result.put("affected_rows", random.nextInt(10) + 1);
        return result;
    }
    
    private Object simulateDelete(String table, Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        result.put("query", "DELETE FROM " + table + " WHERE ...");
        result.put("affected_rows", random.nextInt(5) + 1);
        return result;
    }
    
    private Object[] generateMockData() {
        Object[] data = new Object[random.nextInt(5) + 1];
        for (int i = 0; i < data.length; i++) {
            Map<String, Object> row = new HashMap<>();
            row.put("id", i + 1);
            row.put("name", "User " + (i + 1));
            row.put("email", "user" + (i + 1) + "@example.com");
            row.put("created_at", "2024-01-" + (i + 1));
            data[i] = row;
        }
        return data;
    }
    
    @Override
    public boolean validate() {
        return operation != null && !operation.trim().isEmpty() && 
               table != null && !table.trim().isEmpty();
    }
    
    @Override
    public Map<String, String> getInputTypes() {
        Map<String, String> inputs = new HashMap<>();
        inputs.put("query_params", "object");
        inputs.put("connection_string", "string");
        return inputs;
    }
    
    @Override
    public Map<String, String> getOutputTypes() {
        Map<String, String> outputs = new HashMap<>();
        outputs.put("data", "object");
        outputs.put("operation", "string");
        outputs.put("table", "string");
        outputs.put("rows_affected", "int");
        outputs.put("execution_time", "int");
        outputs.put("success", "boolean");
        return outputs;
    }
    
    @Override
    public String getType() {
        return "database";
    }

    @Override
    public boolean isInputNode() {
        // Pode ser usado como entrada se for SELECT
        return "SELECT".equalsIgnoreCase(operation);
    }

    @Override
    public boolean isOutputNode() {
        // Pode ser usado como saída se for INSERT/UPDATE/DELETE
        return "INSERT".equalsIgnoreCase(operation) || "UPDATE".equalsIgnoreCase(operation) || "DELETE".equalsIgnoreCase(operation);
    }
    
    // Getters e Setters específicos
    public String getOperation() {
        return operation;
    }
    
    public void setOperation(String operation) {
        this.operation = operation;
    }
    
    public String getTable() {
        return table;
    }
    
    public void setTable(String table) {
        this.table = table;
    }
    
    public Map<String, Object> getParameters() {
        return new HashMap<>(parameters);
    }
    
    public void setParameters(Map<String, Object> parameters) {
        this.parameters = new HashMap<>(parameters);
    }
    
    public void setParameter(String key, Object value) {
        this.parameters.put(key, value);
    }
    
    public Object getParameter(String key) {
        return this.parameters.get(key);
    }
} 