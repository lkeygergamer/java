package com.myfeest.blueprint.core;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Contexto de execução para um blueprint
 * Mantém o estado e dados durante a execução
 */
public class ExecutionContext {
    private Map<String, Object> globalData;
    private Map<String, Object> nodeResults;
    private Map<String, Object> variables;
    private ExecutionStatus status;
    private long startTime;
    private long timeout;
    
    public enum ExecutionStatus {
        PENDING,    // Aguardando execução
        RUNNING,    // Em execução
        COMPLETED,  // Concluído com sucesso
        FAILED,     // Falhou
        TIMEOUT     // Timeout
    }
    
    public ExecutionContext() {
        this.globalData = new ConcurrentHashMap<>();
        this.nodeResults = new ConcurrentHashMap<>();
        this.variables = new ConcurrentHashMap<>();
        this.status = ExecutionStatus.PENDING;
        this.startTime = System.currentTimeMillis();
        this.timeout = 30000; // 30 segundos por padrão
    }
    
    public ExecutionContext(long timeout) {
        this();
        this.timeout = timeout;
    }
    
    // Métodos para gerenciar dados globais
    public void setGlobalData(String key, Object value) {
        globalData.put(key, value);
    }
    
    public Object getGlobalData(String key) {
        return globalData.get(key);
    }
    
    public Map<String, Object> getGlobalData() {
        return new HashMap<>(globalData);
    }
    
    // Métodos para gerenciar resultados de nós
    public void setNodeResult(String nodeId, Object result) {
        nodeResults.put(nodeId, result);
    }
    
    public Object getNodeResult(String nodeId) {
        return nodeResults.get(nodeId);
    }
    
    public Map<String, Object> getNodeResults() {
        return new HashMap<>(nodeResults);
    }
    
    // Métodos para gerenciar variáveis
    public void setVariable(String name, Object value) {
        variables.put(name, value);
    }
    
    public Object getVariable(String name) {
        return variables.get(name);
    }
    
    public Map<String, Object> getVariables() {
        return new HashMap<>(variables);
    }
    
    // Métodos para controle de execução
    public void setStatus(ExecutionStatus status) {
        this.status = status;
    }
    
    public ExecutionStatus getStatus() {
        return status;
    }
    
    public boolean isRunning() {
        return status == ExecutionStatus.RUNNING;
    }
    
    public boolean isCompleted() {
        return status == ExecutionStatus.COMPLETED;
    }
    
    public boolean isFailed() {
        return status == ExecutionStatus.FAILED;
    }
    
    public boolean hasTimeout() {
        return System.currentTimeMillis() - startTime > timeout;
    }
    
    public long getExecutionTime() {
        return System.currentTimeMillis() - startTime;
    }
    
    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }
    
    public long getTimeout() {
        return timeout;
    }
    
    // Métodos utilitários
    public void clear() {
        globalData.clear();
        nodeResults.clear();
        variables.clear();
        status = ExecutionStatus.PENDING;
        startTime = System.currentTimeMillis();
    }
    
    public ExecutionContext copy() {
        ExecutionContext copy = new ExecutionContext(timeout);
        copy.globalData.putAll(this.globalData);
        copy.nodeResults.putAll(this.nodeResults);
        copy.variables.putAll(this.variables);
        copy.status = this.status;
        copy.startTime = this.startTime;
        return copy;
    }
    
    @Override
    public String toString() {
        return String.format("ExecutionContext{status='%s', nodes=%d, variables=%d, time=%dms}", 
                           status, nodeResults.size(), variables.size(), getExecutionTime());
    }
} 