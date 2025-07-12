package com.myfeest.blueprint.nodes.advanced;

import com.myfeest.blueprint.core.ExecutionContext;
import com.myfeest.blueprint.core.Node;

import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Nó de filtro avançado com condições complexas
 */
public class AdvancedFilterNode extends Node {
    
    private String condition;
    private Map<String, Object> parameters;
    private List<String> operators;
    private List<String> functions;
    
    public AdvancedFilterNode(String id, String name) {
        super(id, name);
        this.condition = "true";
        this.parameters = new HashMap<>();
        this.operators = Arrays.asList(
            "==", "!=", ">", "<", ">=", "<=", 
            "contains", "starts_with", "ends_with", 
            "matches", "in", "not_in", "is_null", "is_not_null"
        );
        this.functions = Arrays.asList(
            "length", "count", "sum", "average", "min", "max",
            "upper", "lower", "trim", "substring", "replace"
        );
    }
    
    @Override
    public Map<String, Object> execute(ExecutionContext context) {
        Map<String, Object> result = new HashMap<>();
        Object input = getInputData(context);
        
        try {
            boolean passes = evaluateCondition(input);
            Object output = passes ? input : null;
            
            result.put("value", output);
            result.put("output", output);
            result.put("condition", condition);
            result.put("passes", passes);
            result.put("success", true);
            result.put("filtered_count", getFilteredCount(input, passes));
        } catch (Exception e) {
            result.put("error", e.getMessage());
            result.put("success", false);
            result.put("passes", false);
        }
        
        context.setNodeResult(getId(), result);
        return result;
    }
    
    private boolean evaluateCondition(Object input) {
        if (input == null) {
            return evaluateNullCondition();
        }
        
        // Avaliar condição baseada no tipo de entrada
        if (input instanceof Collection) {
            return evaluateCollectionCondition((Collection<?>) input);
        } else if (input instanceof Object[]) {
            return evaluateArrayCondition((Object[]) input);
        } else if (input instanceof Map) {
            return evaluateMapCondition((Map<?, ?>) input);
        } else {
            return evaluateSimpleCondition(input);
        }
    }
    
    private boolean evaluateNullCondition() {
        return condition.toLowerCase().contains("is_null") || 
               condition.toLowerCase().contains("null");
    }
    
    private boolean evaluateCollectionCondition(Collection<?> collection) {
        String cond = condition.toLowerCase();
        
        if (cond.contains("length") || cond.contains("count")) {
            int length = collection.size();
            return evaluateNumericCondition(length);
        } else if (cond.contains("empty")) {
            return collection.isEmpty();
        } else if (cond.contains("not_empty")) {
            return !collection.isEmpty();
        } else {
            // Avaliar cada item da coleção
            return collection.stream().anyMatch(this::evaluateSimpleCondition);
        }
    }
    
    private boolean evaluateArrayCondition(Object[] array) {
        String cond = condition.toLowerCase();
        
        if (cond.contains("length") || cond.contains("count")) {
            int length = array.length;
            return evaluateNumericCondition(length);
        } else if (cond.contains("empty")) {
            return array.length == 0;
        } else if (cond.contains("not_empty")) {
            return array.length > 0;
        } else {
            // Avaliar cada item do array
            return Arrays.stream(array).anyMatch(this::evaluateSimpleCondition);
        }
    }
    
    private boolean evaluateMapCondition(Map<?, ?> map) {
        String cond = condition.toLowerCase();
        
        if (cond.contains("length") || cond.contains("count")) {
            int length = map.size();
            return evaluateNumericCondition(length);
        } else if (cond.contains("empty")) {
            return map.isEmpty();
        } else if (cond.contains("not_empty")) {
            return !map.isEmpty();
        } else if (cond.contains("has_key")) {
            String key = extractKeyFromCondition();
            return map.containsKey(key);
        } else if (cond.contains("has_value")) {
            Object value = extractValueFromCondition();
            return map.containsValue(value);
        } else {
            // Avaliar valores do mapa
            return map.values().stream().anyMatch(this::evaluateSimpleCondition);
        }
    }
    
    private boolean evaluateSimpleCondition(Object input) {
        String cond = condition.toLowerCase();
        String inputStr = input.toString();
        
        // Operadores de comparação
        if (cond.contains("==")) {
            String value = extractValueFromCondition();
            return inputStr.equals(value);
        } else if (cond.contains("!=")) {
            String value = extractValueFromCondition();
            return !inputStr.equals(value);
        } else if (cond.contains(">")) {
            return evaluateNumericComparison(input, ">");
        } else if (cond.contains("<")) {
            return evaluateNumericComparison(input, "<");
        } else if (cond.contains(">=")) {
            return evaluateNumericComparison(input, ">=");
        } else if (cond.contains("<=")) {
            return evaluateNumericComparison(input, "<=");
        }
        
        // Operadores de string
        if (cond.contains("contains")) {
            String value = extractValueFromCondition();
            return inputStr.toLowerCase().contains(value.toLowerCase());
        } else if (cond.contains("starts_with")) {
            String value = extractValueFromCondition();
            return inputStr.toLowerCase().startsWith(value.toLowerCase());
        } else if (cond.contains("ends_with")) {
            String value = extractValueFromCondition();
            return inputStr.toLowerCase().endsWith(value.toLowerCase());
        } else if (cond.contains("matches")) {
            String pattern = extractValueFromCondition();
            return Pattern.matches(pattern, inputStr);
        }
        
        // Operadores de inclusão
        if (cond.contains("in")) {
            List<String> values = extractListFromCondition();
            return values.contains(inputStr);
        } else if (cond.contains("not_in")) {
            List<String> values = extractListFromCondition();
            return !values.contains(inputStr);
        }
        
        // Operadores de tipo
        if (cond.contains("is_string")) {
            return input instanceof String;
        } else if (cond.contains("is_number")) {
            return input instanceof Number;
        } else if (cond.contains("is_boolean")) {
            return input instanceof Boolean;
        }
        
        // Condições padrão
        if (cond.equals("true")) {
            return true;
        } else if (cond.equals("false")) {
            return false;
        }
        
        // Se não encontrou nenhum operador, verificar se contém o valor
        return inputStr.toLowerCase().contains(cond);
    }
    
    private boolean evaluateNumericCondition(int value) {
        String cond = condition.toLowerCase();
        
        if (cond.contains(">")) {
            int threshold = extractNumberFromCondition();
            return value > threshold;
        } else if (cond.contains("<")) {
            int threshold = extractNumberFromCondition();
            return value < threshold;
        } else if (cond.contains(">=")) {
            int threshold = extractNumberFromCondition();
            return value >= threshold;
        } else if (cond.contains("<=")) {
            int threshold = extractNumberFromCondition();
            return value <= threshold;
        } else if (cond.contains("==")) {
            int threshold = extractNumberFromCondition();
            return value == threshold;
        } else if (cond.contains("!=")) {
            int threshold = extractNumberFromCondition();
            return value != threshold;
        }
        
        return false;
    }
    
    private boolean evaluateNumericComparison(Object input, String operator) {
        try {
            double inputValue = Double.parseDouble(input.toString());
            double threshold = extractNumberFromCondition();
            
            switch (operator) {
                case ">":
                    return inputValue > threshold;
                case "<":
                    return inputValue < threshold;
                case ">=":
                    return inputValue >= threshold;
                case "<=":
                    return inputValue <= threshold;
                default:
                    return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    private String extractValueFromCondition() {
        // Extrair valor entre aspas ou após operador
        Pattern pattern = Pattern.compile("['\"]([^'\"]*)['\"]");
        Matcher matcher = pattern.matcher(condition);
        if (matcher.find()) {
            return matcher.group(1);
        }
        
        // Extrair valor após operador
        for (String op : operators) {
            if (condition.contains(op)) {
                String[] parts = condition.split(op);
                if (parts.length > 1) {
                    return parts[1].trim();
                }
            }
        }
        
        return "";
    }
    
    private String extractKeyFromCondition() {
        // Extrair chave da condição "has_key(key)"
        Pattern pattern = Pattern.compile("has_key\\(['\"]?([^'\"]*)['\"]?\\)");
        Matcher matcher = pattern.matcher(condition);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }
    
    private int extractNumberFromCondition() {
        // Extrair número da condição
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(condition);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group());
        }
        return 0;
    }
    
    private List<String> extractListFromCondition() {
        // Extrair lista da condição "in [a, b, c]"
        Pattern pattern = Pattern.compile("\\[([^\\]]*)\\]");
        Matcher matcher = pattern.matcher(condition);
        if (matcher.find()) {
            String listStr = matcher.group(1);
            return Arrays.asList(listStr.split(","));
        }
        return new ArrayList<>();
    }
    
    private Object getInputData(ExecutionContext context) {
        // Buscar dados de entrada do contexto
        Object data = context.getGlobalData("data");
        if (data != null) {
            return data;
        }
        
        // Se não encontrar, retornar valor padrão
        return parameters.getOrDefault("default_value", "test");
    }
    
    private int getFilteredCount(Object input, boolean passes) {
        if (!passes) return 0;
        
        if (input instanceof Collection) {
            return ((Collection<?>) input).size();
        } else if (input instanceof Object[]) {
            return ((Object[]) input).length;
        } else if (input instanceof Map) {
            return ((Map<?, ?>) input).size();
        } else {
            return 1;
        }
    }
    
    @Override
    public boolean validate() {
        return condition != null && !condition.trim().isEmpty();
    }
    
    @Override
    public Map<String, String> getInputTypes() {
        Map<String, String> inputs = new HashMap<>();
        inputs.put("data", "object");
        inputs.put("condition", "string");
        return inputs;
    }
    
    @Override
    public Map<String, String> getOutputTypes() {
        Map<String, String> outputs = new HashMap<>();
        outputs.put("output", "object");
        outputs.put("condition", "string");
        outputs.put("passes", "boolean");
        outputs.put("success", "boolean");
        outputs.put("filtered_count", "int");
        return outputs;
    }
    
    @Override
    public String getType() {
        return "advanced_filter";
    }

    @Override
    public boolean isInputNode() {
        return false;
    }

    @Override
    public boolean isOutputNode() {
        return false;
    }
    
    // Getters e Setters
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
    
    public List<String> getAvailableOperators() {
        return new ArrayList<>(operators);
    }
    
    public List<String> getAvailableFunctions() {
        return new ArrayList<>(functions);
    }
    
    // Métodos auxiliares para criar condições
    public void setLengthCondition(int length, String operator) {
        this.condition = "length " + operator + " " + length;
    }
    
    public void setContainsCondition(String value) {
        this.condition = "contains '" + value + "'";
    }
    
    public void setEqualsCondition(String value) {
        this.condition = "== '" + value + "'";
    }
    
    public void setInCondition(List<String> values) {
        this.condition = "in " + values.toString();
    }
    
    public void setNullCondition() {
        this.condition = "is_null";
    }
    
    public void setNotEmptyCondition() {
        this.condition = "not_empty";
    }
} 