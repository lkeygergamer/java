package com.myfeest.blueprint.nodes;

import com.myfeest.blueprint.core.ExecutionContext;
import com.myfeest.blueprint.core.Node;

import java.util.HashMap;
import java.util.Map;

/**
 * Nó de entrada - fornece dados para o blueprint
 */
public class InputNode extends Node {
    
    private Object value;
    private String dataType;
    
    public InputNode(String id, String name) {
        super(id, name);
        this.value = null;
        this.dataType = "string";
    }
    
    public InputNode(String id, String name, Object value) {
        super(id, name);
        this.value = value;
        this.dataType = value != null ? value.getClass().getSimpleName() : "string";
    }
    
    @Override
    public Map<String, Object> execute(ExecutionContext context) {
        Map<String, Object> result = new HashMap<>();
        result.put("value", value);
        result.put("type", dataType);
        
        // Armazena o resultado no contexto
        context.setNodeResult(getId(), result);
        
        return result;
    }
    
    @Override
    public boolean validate() {
        return value != null;
    }
    
    @Override
    public Map<String, String> getInputTypes() {
        return new HashMap<>(); // Nós de entrada não têm entradas
    }
    
    @Override
    public Map<String, String> getOutputTypes() {
        Map<String, String> outputs = new HashMap<>();
        outputs.put("value", dataType);
        outputs.put("type", "string");
        return outputs;
    }
    
    // Getters e Setters específicos
    public Object getValue() {
        return value;
    }
    
    public void setValue(Object value) {
        this.value = value;
        if (value != null) {
            this.dataType = value.getClass().getSimpleName();
        }
    }
    
    public String getDataType() {
        return dataType;
    }
    
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
} 