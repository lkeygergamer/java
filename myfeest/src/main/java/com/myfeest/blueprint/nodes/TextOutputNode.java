package com.myfeest.blueprint.nodes;

import com.myfeest.blueprint.core.ExecutionContext;
import com.myfeest.blueprint.core.Node;

import java.util.HashMap;
import java.util.Map;

/**
 * Nó de saída de texto - processa e disponibiliza texto
 */
public class TextOutputNode extends Node {
    
    private String format;
    private String destination;
    private boolean includeMetadata;
    
    public TextOutputNode(String id, String name) {
        super(id, name);
        this.format = "plain";
        this.destination = "console";
        this.includeMetadata = false;
    }
    
    public TextOutputNode(String id, String name, String destination) {
        super(id, name);
        this.format = "plain";
        this.destination = destination;
        this.includeMetadata = false;
    }
    
    @Override
    public Map<String, Object> execute(ExecutionContext context) {
        Map<String, Object> result = new HashMap<>();
        
        // Busca dados de texto de entrada
        Object textInput = getInput("text");
        if (textInput == null) {
            textInput = getInput("result");
        }
        if (textInput == null) {
            textInput = getInput("value");
        }
        
        if (textInput == null) {
            result.put("error", "Nenhum dado de texto fornecido");
            return result;
        }
        
        // Processa o texto
        Map<String, Object> textData = processText(textInput);
        
        result.put("text", textData);
        result.put("format", format);
        result.put("destination", destination);
        result.put("includeMetadata", includeMetadata);
        result.put("status", "ready");
        result.put("timestamp", System.currentTimeMillis());
        
        // Armazena o resultado no contexto
        context.setNodeResult(getId(), result);
        
        return result;
    }
    
    private Map<String, Object> processText(Object textInput) {
        Map<String, Object> processed = new HashMap<>();
        
        String textContent;
        if (textInput instanceof Map) {
            Map<String, Object> inputMap = (Map<String, Object>) textInput;
            
            // Extrai o conteúdo de texto do mapa
            if (inputMap.containsKey("text")) {
                textContent = inputMap.get("text").toString();
            } else if (inputMap.containsKey("result")) {
                textContent = inputMap.get("result").toString();
            } else if (inputMap.containsKey("value")) {
                textContent = inputMap.get("value").toString();
            } else {
                textContent = inputMap.toString();
            }
            
            // Inclui metadados se solicitado
            if (includeMetadata) {
                processed.put("metadata", inputMap);
            }
        } else {
            textContent = textInput.toString();
        }
        
        processed.put("content", textContent);
        processed.put("length", textContent.length());
        processed.put("wordCount", textContent.split("\\s+").length);
        processed.put("processedFormat", format);
        processed.put("destination", destination);
        
        return processed;
    }
    
    @Override
    public boolean validate() {
        return format != null && !format.trim().isEmpty() && 
               destination != null && !destination.trim().isEmpty();
    }
    
    @Override
    public Map<String, String> getInputTypes() {
        Map<String, String> inputs = new HashMap<>();
        inputs.put("text", "object");
        inputs.put("result", "object");
        inputs.put("value", "object");
        return inputs;
    }
    
    @Override
    public Map<String, String> getOutputTypes() {
        Map<String, String> outputs = new HashMap<>();
        outputs.put("text", "object");
        outputs.put("format", "string");
        outputs.put("destination", "string");
        outputs.put("includeMetadata", "boolean");
        outputs.put("status", "string");
        return outputs;
    }
    
    // Getters e Setters específicos
    public String getFormat() {
        return format;
    }
    
    public void setFormat(String format) {
        this.format = format;
    }
    
    public String getDestination() {
        return destination;
    }
    
    public void setDestination(String destination) {
        this.destination = destination;
    }
    
    public boolean isIncludeMetadata() {
        return includeMetadata;
    }
    
    public void setIncludeMetadata(boolean includeMetadata) {
        this.includeMetadata = includeMetadata;
    }
} 