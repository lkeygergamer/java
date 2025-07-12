package com.myfeest.blueprint.nodes;

import com.myfeest.blueprint.core.ExecutionContext;
import com.myfeest.blueprint.core.Node;

import java.util.HashMap;
import java.util.Map;

/**
 * Nó de entrada de texto - fornece prompts de texto para IA
 */
public class TextInputNode extends Node {
    
    private String prompt;
    private String language;
    private int maxLength;
    
    public TextInputNode(String id, String name) {
        super(id, name);
        this.prompt = "Digite seu prompt aqui...";
        this.language = "pt-BR";
        this.maxLength = 1000;
    }
    
    public TextInputNode(String id, String name, String prompt) {
        super(id, name);
        this.prompt = prompt;
        this.language = "pt-BR";
        this.maxLength = 1000;
    }
    
    @Override
    public Map<String, Object> execute(ExecutionContext context) {
        Map<String, Object> result = new HashMap<>();
        
        // Simula dados de texto
        Map<String, Object> textData = new HashMap<>();
        textData.put("prompt", prompt);
        textData.put("language", language);
        textData.put("maxLength", maxLength);
        textData.put("length", prompt.length());
        textData.put("timestamp", System.currentTimeMillis());
        textData.put("tokens", estimateTokens(prompt));
        
        result.put("text", textData);
        result.put("prompt", prompt);
        result.put("language", language);
        
        // Armazena o resultado no contexto
        context.setNodeResult(getId(), result);
        
        return result;
    }
    
    private int estimateTokens(String text) {
        // Estimativa simples: ~4 caracteres por token
        return Math.max(1, text.length() / 4);
    }
    
    @Override
    public boolean validate() {
        return prompt != null && !prompt.trim().isEmpty() && 
               language != null && !language.trim().isEmpty() && 
               maxLength > 0;
    }
    
    @Override
    public Map<String, String> getInputTypes() {
        return new HashMap<>(); // Nós de entrada não têm entradas
    }
    
    @Override
    public Map<String, String> getOutputTypes() {
        Map<String, String> outputs = new HashMap<>();
        outputs.put("text", "object");
        outputs.put("prompt", "string");
        outputs.put("language", "string");
        return outputs;
    }
    
    // Getters e Setters específicos
    public String getPrompt() {
        return prompt;
    }
    
    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }
    
    public String getLanguage() {
        return language;
    }
    
    public void setLanguage(String language) {
        this.language = language;
    }
    
    public int getMaxLength() {
        return maxLength;
    }
    
    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }
} 