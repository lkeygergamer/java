package com.myfeest.blueprint.nodes;

import com.myfeest.blueprint.core.ExecutionContext;
import com.myfeest.blueprint.core.Node;

import java.util.HashMap;
import java.util.Map;

/**
 * Nó de transformação - processa e modifica dados
 */
public class TransformNode extends Node {
    
    private String operation;
    private Map<String, Object> parameters;
    
    public TransformNode(String id, String name) {
        super(id, name);
        this.operation = "identity";
        this.parameters = new HashMap<>();
    }
    
    public TransformNode(String id, String name, String operation) {
        super(id, name);
        this.operation = operation;
        this.parameters = new HashMap<>();
    }
    
    @Override
    public Map<String, Object> execute(ExecutionContext context) {
        Map<String, Object> result = new HashMap<>();
        
        // Busca dados de entrada
        Object inputValue = getInput("value");
        if (inputValue == null) {
            result.put("error", "Nenhum valor de entrada fornecido");
            return result;
        }
        
        // Aplica a transformação
        Object output = applyTransformation(inputValue, operation, parameters);
        
        result.put("value", output);
        result.put("output", output);
        result.put("operation", operation);
        result.put("original", inputValue);
        
        // Armazena o resultado no contexto
        context.setNodeResult(getId(), result);
        
        return result;
    }
    
    private Object applyTransformation(Object input, String operation, Map<String, Object> params) {
        switch (operation.toLowerCase()) {
            case "uppercase":
                if (input instanceof String) {
                    return ((String) input).toUpperCase();
                }
                break;
                
            case "lowercase":
                if (input instanceof String) {
                    return ((String) input).toLowerCase();
                }
                break;
                
            case "reverse":
                if (input instanceof String) {
                    return new StringBuilder((String) input).reverse().toString();
                }
                break;
                
            case "length":
                if (input instanceof String) {
                    return ((String) input).length();
                }
                break;
                
            case "multiply":
                if (input instanceof Number) {
                    double multiplier = params.containsKey("multiplier") ? 
                        ((Number) params.get("multiplier")).doubleValue() : 2.0;
                    return ((Number) input).doubleValue() * multiplier;
                }
                break;
                
            case "add":
                if (input instanceof Number) {
                    double addend = params.containsKey("addend") ? 
                        ((Number) params.get("addend")).doubleValue() : 1.0;
                    return ((Number) input).doubleValue() + addend;
                }
                break;
                
            case "substring":
                if (input instanceof String) {
                    int start = params.containsKey("start") ? 
                        ((Number) params.get("start")).intValue() : 0;
                    int end = params.containsKey("end") ? 
                        ((Number) params.get("end")).intValue() : ((String) input).length();
                    return ((String) input).substring(start, end);
                }
                break;
                
            case "identity":
            default:
                return input;
        }
        
        return input; // Retorna o valor original se a operação não for aplicável
    }
    
    @Override
    public boolean validate() {
        return operation != null && !operation.trim().isEmpty();
    }
    
    @Override
    public Map<String, String> getInputTypes() {
        Map<String, String> inputs = new HashMap<>();
        inputs.put("value", "object");
        return inputs;
    }
    
    @Override
    public Map<String, String> getOutputTypes() {
        Map<String, String> outputs = new HashMap<>();
        outputs.put("value", "object");
        outputs.put("operation", "string");
        outputs.put("original", "object");
        return outputs;
    }
    
    // Getters e Setters específicos
    public String getOperation() {
        return operation;
    }
    
    public void setOperation(String operation) {
        this.operation = operation;
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