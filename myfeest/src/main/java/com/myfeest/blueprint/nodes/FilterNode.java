package com.myfeest.blueprint.nodes;

import com.myfeest.blueprint.core.ExecutionContext;
import com.myfeest.blueprint.core.Node;

import java.util.HashMap;
import java.util.Map;

/**
 * Nó de filtro - aplica condições aos dados
 */
public class FilterNode extends Node {
    
    private String condition;
    private Map<String, Object> parameters;
    
    public FilterNode(String id, String name) {
        super(id, name);
        this.condition = "true";
        this.parameters = new HashMap<>();
    }
    
    public FilterNode(String id, String name, String condition) {
        super(id, name);
        this.condition = condition;
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
        
        // Aplica o filtro
        boolean passes = evaluateCondition(inputValue, condition, parameters);
        Object output = passes ? inputValue : null;
        
        result.put("value", output);
        result.put("output", output);
        result.put("condition", condition);
        result.put("passes", passes);
        result.put("original", inputValue);
        
        // Armazena o resultado no contexto
        context.setNodeResult(getId(), result);
        
        return result;
    }
    
    private boolean evaluateCondition(Object input, String condition, Map<String, Object> params) {
        if (condition == null || condition.trim().isEmpty()) {
            return true;
        }
        
        String cond = condition.toLowerCase().trim();
        
        // Condições básicas para strings
        if (input instanceof String) {
            String str = (String) input;
            
            switch (cond) {
                case "true":
                case "always":
                    return true;
                    
                case "false":
                case "never":
                    return false;
                    
                case "not_empty":
                case "non_empty":
                    return !str.isEmpty();
                    
                case "empty":
                    return str.isEmpty();
                    
                case "length > 5":
                    return str.length() > 5;
                    
                case "length >= 10":
                    return str.length() >= 10;
                    
                case "contains_vowels":
                    return str.matches(".*[aeiouAEIOU].*");
                    
                case "starts_with_capital":
                    return !str.isEmpty() && Character.isUpperCase(str.charAt(0));
                    
                case "is_numeric":
                    return str.matches("\\d+");
                    
                case "is_alpha":
                    return str.matches("[a-zA-Z]+");
                    
                case "is_alphanumeric":
                    return str.matches("[a-zA-Z0-9]+");
            }
            
            // Condições com parâmetros
            if (cond.startsWith("length > ")) {
                try {
                    int minLength = Integer.parseInt(cond.substring(9));
                    return str.length() > minLength;
                } catch (NumberFormatException e) {
                    return false;
                }
            }
            
            if (cond.startsWith("length >= ")) {
                try {
                    int minLength = Integer.parseInt(cond.substring(10));
                    return str.length() >= minLength;
                } catch (NumberFormatException e) {
                    return false;
                }
            }
            
            if (cond.startsWith("contains ")) {
                String substring = cond.substring(9);
                return str.contains(substring);
            }
            
            if (cond.startsWith("starts_with ")) {
                String prefix = cond.substring(12);
                return str.startsWith(prefix);
            }
            
            if (cond.startsWith("ends_with ")) {
                String suffix = cond.substring(10);
                return str.endsWith(suffix);
            }
        }
        
        // Condições para números
        if (input instanceof Number) {
            double num = ((Number) input).doubleValue();
            
            switch (cond) {
                case "positive":
                    return num > 0;
                    
                case "negative":
                    return num < 0;
                    
                case "zero":
                    return num == 0;
                    
                case "even":
                    return num % 2 == 0;
                    
                case "odd":
                    return num % 2 != 0;
            }
            
            // Condições numéricas com parâmetros
            if (cond.startsWith("> ")) {
                try {
                    double threshold = Double.parseDouble(cond.substring(2));
                    return num > threshold;
                } catch (NumberFormatException e) {
                    return false;
                }
            }
            
            if (cond.startsWith(">= ")) {
                try {
                    double threshold = Double.parseDouble(cond.substring(3));
                    return num >= threshold;
                } catch (NumberFormatException e) {
                    return false;
                }
            }
            
            if (cond.startsWith("< ")) {
                try {
                    double threshold = Double.parseDouble(cond.substring(2));
                    return num < threshold;
                } catch (NumberFormatException e) {
                    return false;
                }
            }
            
            if (cond.startsWith("<= ")) {
                try {
                    double threshold = Double.parseDouble(cond.substring(3));
                    return num <= threshold;
                } catch (NumberFormatException e) {
                    return false;
                }
            }
        }
        
        // Condições para objetos
        if (input != null) {
            switch (cond) {
                case "not_null":
                    return true;
                    
                case "is_null":
                    return false;
            }
        }
        
        return false; // Condição não reconhecida
    }
    
    @Override
    public boolean validate() {
        return condition != null && !condition.trim().isEmpty();
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
        outputs.put("condition", "string");
        outputs.put("passes", "boolean");
        outputs.put("original", "object");
        return outputs;
    }
    
    // Getters e Setters específicos
    public String getCondition() {
        return condition;
    }
    
    public void setCondition(String condition) {
        this.condition = condition;
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