package com.myfeest.blueprint.engine;

import com.myfeest.blueprint.core.*;
import com.myfeest.blueprint.nodes.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Engine de execução de blueprints
 * Coordena a execução dos nós na ordem correta
 */
public class BlueprintEngine {
    
    private static final Logger logger = Logger.getLogger(BlueprintEngine.class.getName());
    private Map<String, Object> globalVariables;
    private boolean enableLogging;
    private long timeout;
    
    public BlueprintEngine() {
        this.globalVariables = new ConcurrentHashMap<>();
        this.enableLogging = true;
        this.timeout = 30000; // 30 segundos
    }
    
    public BlueprintEngine(long timeout) {
        this();
        this.timeout = timeout;
    }
    
    /**
     * Executa um blueprint completo
     * @param blueprint O blueprint a ser executado
     * @return Resultados da execução
     */
    public Map<String, Object> execute(Blueprint blueprint) throws Exception {
        if (blueprint == null) {
            throw new IllegalArgumentException("Blueprint não pode ser nulo");
        }
        
        if (!blueprint.validate()) {
            throw new IllegalStateException("Blueprint inválido");
        }
        
        if (blueprint.hasCycles()) {
            throw new IllegalStateException("Blueprint contém ciclos");
        }
        
        logger.info("Iniciando execução do blueprint: " + blueprint.getName());
        
        // Cria contexto de execução
        ExecutionContext context = new ExecutionContext(timeout);
        context.setStatus(ExecutionContext.ExecutionStatus.RUNNING);
        
        // Obtém ordem de execução
        List<Node> executionOrder = blueprint.getExecutionOrder();
        
        if (executionOrder.isEmpty()) {
            throw new IllegalStateException("Nenhum nó encontrado para execução");
        }
        
        logger.info("Ordem de execução: " + executionOrder.size() + " nós");
        
        // Executa cada nó na ordem correta
        Map<String, Object> results = new HashMap<>();
        
        for (Node node : executionOrder) {
            if (context.hasTimeout()) {
                context.setStatus(ExecutionContext.ExecutionStatus.TIMEOUT);
                throw new RuntimeException("Timeout na execução do blueprint");
            }
            
            try {
                logger.info("Executando nó: " + node.getName() + " (" + node.getType() + ")");
                
                // Prepara dados de entrada para o nó
                prepareNodeInputs(node, blueprint, context);
                
                // Executa o nó
                Map<String, Object> nodeResult = node.execute(context);
                
                // Armazena resultado
                results.put(node.getId(), nodeResult);
                
                // Atualiza contexto global
                updateGlobalContext(node, nodeResult, context);
                
                logger.info("Nó executado com sucesso: " + node.getName());
                
            } catch (Exception e) {
                logger.severe("Erro ao executar nó " + node.getName() + ": " + e.getMessage());
                context.setStatus(ExecutionContext.ExecutionStatus.FAILED);
                throw new RuntimeException("Erro na execução do nó " + node.getName(), e);
            }
        }
        
        // Coleta resultados finais dos nós de saída
        Map<String, Object> finalResults = collectOutputResults(blueprint, results);
        
        context.setStatus(ExecutionContext.ExecutionStatus.COMPLETED);
        
        logger.info("Blueprint executado com sucesso em " + context.getExecutionTime() + "ms");
        
        return finalResults;
    }
    
    /**
     * Prepara os dados de entrada para um nó baseado nas conexões
     */
    private void prepareNodeInputs(Node node, Blueprint blueprint, ExecutionContext context) {
        // Busca conexões que chegam neste nó
        List<Connection> incomingConnections = blueprint.getConnectionsTo(node.getId());
        
        for (Connection conn : incomingConnections) {
            String fromNodeId = conn.getFromNodeId();
            String toPort = conn.getToPort();
            
            // Busca resultado do nó de origem
            Object sourceResult = context.getNodeResult(fromNodeId);
            
            if (sourceResult != null) {
                if (sourceResult instanceof Map) {
                    Map<String, Object> resultMap = (Map<String, Object>) sourceResult;
                    
                    // Tenta encontrar o valor correto baseado na porta
                    Object value = resultMap.get(toPort);
                    if (value == null) {
                        // Fallback para valores comuns
                        value = resultMap.get("value");
                        if (value == null) {
                            value = resultMap.get("result");
                        }
                        if (value == null) {
                            value = sourceResult; // Usa o resultado completo
                        }
                    }
                    
                    node.setInput(toPort, value);
                } else {
                    node.setInput(toPort, sourceResult);
                }
            }
        }
    }
    
    /**
     * Atualiza o contexto global com os resultados do nó
     */
    private void updateGlobalContext(Node node, Map<String, Object> nodeResult, ExecutionContext context) {
        // Armazena resultado no contexto
        context.setNodeResult(node.getId(), nodeResult);
        
        // Atualiza variáveis globais se necessário
        if (node instanceof OutputNode) {
            OutputNode outputNode = (OutputNode) node;
            String outputName = outputNode.getOutputName();
            
            if (nodeResult.containsKey(outputName)) {
                globalVariables.put(outputName, nodeResult.get(outputName));
                context.setGlobalData(outputName, nodeResult.get(outputName));
            }
        }
    }
    
    /**
     * Coleta resultados finais dos nós de saída
     */
    private Map<String, Object> collectOutputResults(Blueprint blueprint, Map<String, Object> allResults) {
        Map<String, Object> outputResults = new HashMap<>();
        
        List<Node> outputNodes = blueprint.getOutputNodes();
        
        for (Node outputNode : outputNodes) {
            Object nodeResult = allResults.get(outputNode.getId());
            if (nodeResult != null) {
                if (outputNode instanceof OutputNode) {
                    OutputNode out = (OutputNode) outputNode;
                    String outputName = out.getOutputName();
                    
                    if (nodeResult instanceof Map) {
                        Map<String, Object> resultMap = (Map<String, Object>) nodeResult;
                        if (resultMap.containsKey(outputName)) {
                            outputResults.put(outputName, resultMap.get(outputName));
                        } else {
                            outputResults.put(outputName, nodeResult);
                        }
                    } else {
                        outputResults.put(outputName, nodeResult);
                    }
                } else {
                    outputResults.put(outputNode.getName(), nodeResult);
                }
            }
        }
        
        return outputResults;
    }
    
    /**
     * Executa um nó específico
     */
    public Map<String, Object> executeNode(Node node, ExecutionContext context) throws Exception {
        if (node == null) {
            throw new IllegalArgumentException("Nó não pode ser nulo");
        }
        
        if (!node.validate()) {
            throw new IllegalStateException("Nó inválido: " + node.getName());
        }
        
        return node.execute(context);
    }
    
    /**
     * Valida um blueprint sem executá-lo
     */
    public boolean validateBlueprint(Blueprint blueprint) {
        if (blueprint == null) {
            return false;
        }
        
        try {
            // Valida o blueprint
            if (!blueprint.validate()) {
                return false;
            }
            
            // Verifica ciclos
            if (blueprint.hasCycles()) {
                return false;
            }
            
            // Valida cada nó
            for (Node node : blueprint.getNodes()) {
                if (!node.validate()) {
                    return false;
                }
            }
            
            return true;
            
        } catch (Exception e) {
            logger.warning("Erro na validação do blueprint: " + e.getMessage());
            return false;
        }
    }
    
    // Getters e Setters
    public Map<String, Object> getGlobalVariables() {
        return new HashMap<>(globalVariables);
    }
    
    public void setGlobalVariable(String key, Object value) {
        globalVariables.put(key, value);
    }
    
    public Object getGlobalVariable(String key) {
        return globalVariables.get(key);
    }
    
    public boolean isEnableLogging() {
        return enableLogging;
    }
    
    public void setEnableLogging(boolean enableLogging) {
        this.enableLogging = enableLogging;
    }
    
    public long getTimeout() {
        return timeout;
    }
    
    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }
    
    public void clearGlobalVariables() {
        globalVariables.clear();
    }
} 