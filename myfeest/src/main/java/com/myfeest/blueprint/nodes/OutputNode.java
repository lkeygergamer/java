package com.myfeest.blueprint.nodes;

import com.myfeest.blueprint.core.ExecutionContext;
import com.myfeest.blueprint.core.Node;

import java.util.HashMap;
import java.util.Map;

/**
 * Nó de saída - recebe dados e os disponibiliza como resultado final
 */
public class OutputNode extends Node {
    
    private String outputName;
    private String dataType;
    
    public OutputNode(String id, String name) {
        super(id, name);
        this.outputName = "result";
        this.dataType = "object";
    }
    
    public OutputNode(String id, String name, String outputName) {
        super(id, name);
        this.outputName = outputName;
        this.dataType = "object";
    }
    
    @Override
    public Map<String, Object> execute(ExecutionContext context) {
        Map<String, Object> result = new HashMap<>();
        
        // Busca dados de entrada (estilo Unreal Engine 5)
        // Tenta buscar na ordem: output -> value -> result -> qualquer entrada disponível
        Object inputValue = getInput("output");
        if (inputValue == null) {
            inputValue = getInput("value");
        }
        if (inputValue == null) {
            inputValue = getInput("result");
        }
        if (inputValue == null) {
            // Se não encontrar campos padrão, busca qualquer entrada disponível
            Map<String, Object> inputs = getInputs();
            if (!inputs.isEmpty()) {
                inputValue = inputs.values().iterator().next();
            }
        }
        if (inputValue == null) {
            // Tenta buscar do contexto global como último recurso
            inputValue = context.getGlobalData("data");
        }
        
        result.put(outputName, inputValue);
        result.put("value", inputValue);
        result.put("output", inputValue);
        result.put("type", dataType);
        
        // Armazena o resultado no contexto
        context.setNodeResult(getId(), result);
        
        return result;
    }
    
    @Override
    public boolean validate() {
        return outputName != null && !outputName.trim().isEmpty();
    }
    
    @Override
    public boolean isOutputNode() {
        return true;
    }
    
    @Override
    public Map<String, String> getInputTypes() {
        Map<String, String> inputs = new HashMap<>();
        inputs.put("output", "object");
        inputs.put("value", "object");
        inputs.put("result", "object");
        inputs.put("data", "object");
        inputs.put("*", "object"); // Aceita qualquer entrada
        return inputs;
    }
    
    @Override
    public Map<String, String> getOutputTypes() {
        Map<String, String> outputs = new HashMap<>();
        outputs.put(outputName, dataType);
        outputs.put("type", "string");
        return outputs;
    }
    
    // Getters e Setters específicos
    public String getOutputName() {
        return outputName;
    }
    
    public void setOutputName(String outputName) {
        this.outputName = outputName;
    }
    
    public String getDataType() {
        return dataType;
    }
    
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
} 