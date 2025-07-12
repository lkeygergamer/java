package com.myfeest.blueprint.nodes.advanced;

import com.myfeest.blueprint.core.ExecutionContext;
import com.myfeest.blueprint.core.Node;

import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Nó de transformação avançada com operações complexas
 */
public class AdvancedTransformNode extends Node {
    
    private String operation;
    private Map<String, Object> parameters;
    private List<String> operations;
    
    public AdvancedTransformNode(String id, String name) {
        super(id, name);
        this.operation = "format";
        this.parameters = new HashMap<>();
        this.operations = Arrays.asList(
            "format", "extract", "replace", "split", "join", 
            "sort", "unique", "group", "aggregate", "map", 
            "reduce", "flatten", "nest", "merge", "diff"
        );
    }
    
    @Override
    public Map<String, Object> execute(ExecutionContext context) {
        Map<String, Object> result = new HashMap<>();
        Object input = getInputData(context);
        
        try {
            Object output = performAdvancedOperation(input);
            result.put("value", output);
            result.put("output", output);
            result.put("operation", operation);
            result.put("success", true);
            result.put("input_type", input != null ? input.getClass().getSimpleName() : "null");
            result.put("output_type", output != null ? output.getClass().getSimpleName() : "null");
        } catch (Exception e) {
            result.put("error", e.getMessage());
            result.put("success", false);
        }
        
        context.setNodeResult(getId(), result);
        return result;
    }
    
    private Object performAdvancedOperation(Object input) {
        switch (operation.toLowerCase()) {
            case "format":
                return formatData(input);
            case "extract":
                return extractData(input);
            case "replace":
                return replaceData(input);
            case "split":
                return splitData(input);
            case "join":
                return joinData(input);
            case "sort":
                return sortData(input);
            case "unique":
                return uniqueData(input);
            case "group":
                return groupData(input);
            case "aggregate":
                return aggregateData(input);
            case "map":
                return mapData(input);
            case "reduce":
                return reduceData(input);
            case "flatten":
                return flattenData(input);
            case "nest":
                return nestData(input);
            case "merge":
                return mergeData(input);
            case "diff":
                return diffData(input);
            default:
                throw new IllegalArgumentException("Operação não suportada: " + operation);
        }
    }
    
    private Object formatData(Object input) {
        if (input == null) return null;
        
        String format = (String) parameters.getOrDefault("format", "default");
        String data = input.toString();
        
        switch (format.toLowerCase()) {
            case "uppercase":
                return data.toUpperCase();
            case "lowercase":
                return data.toLowerCase();
            case "capitalize":
                return capitalizeWords(data);
            case "camelcase":
                return toCamelCase(data);
            case "snakecase":
                return toSnakeCase(data);
            case "kebabcase":
                return toKebabCase(data);
            case "titlecase":
                return toTitleCase(data);
            default:
                return data;
        }
    }
    
    private Object extractData(Object input) {
        if (input == null) return null;
        
        String pattern = (String) parameters.getOrDefault("pattern", "\\d+");
        String data = input.toString();
        List<String> matches = new ArrayList<>();
        
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(data);
        
        while (matcher.find()) {
            matches.add(matcher.group());
        }
        
        return matches;
    }
    
    private Object replaceData(Object input) {
        if (input == null) return null;
        
        String find = (String) parameters.getOrDefault("find", "");
        String replace = (String) parameters.getOrDefault("replace", "");
        String data = input.toString();
        
        return data.replace(find, replace);
    }
    
    private Object splitData(Object input) {
        if (input == null) return new String[0];
        
        String delimiter = (String) parameters.getOrDefault("delimiter", ",");
        String data = input.toString();
        
        return data.split(delimiter);
    }
    
    private Object joinData(Object input) {
        if (input == null) return "";
        
        String delimiter = (String) parameters.getOrDefault("delimiter", ",");
        
        if (input instanceof Collection) {
            Collection<?> collection = (Collection<?>) input;
            return String.join(delimiter, collection.stream()
                .map(Object::toString)
                .toArray(String[]::new));
        } else if (input instanceof Object[]) {
            Object[] array = (Object[]) input;
            return String.join(delimiter, Arrays.stream(array)
                .map(Object::toString)
                .toArray(String[]::new));
        }
        
        return input.toString();
    }
    
    private Object sortData(Object input) {
        if (input == null) return null;
        
        boolean ascending = (Boolean) parameters.getOrDefault("ascending", true);
        
        if (input instanceof List) {
            List<?> list = new ArrayList<>((List<?>) input);
            list.sort((a, b) -> {
                int comparison = a.toString().compareTo(b.toString());
                return ascending ? comparison : -comparison;
            });
            return list;
        } else if (input instanceof Object[]) {
            Object[] array = (Object[]) input;
            Arrays.sort(array, (a, b) -> {
                int comparison = a.toString().compareTo(b.toString());
                return ascending ? comparison : -comparison;
            });
            return array;
        }
        
        return input;
    }
    
    private Object uniqueData(Object input) {
        if (input == null) return null;
        
        if (input instanceof Collection) {
            return new ArrayList<>(new LinkedHashSet<>((Collection<?>) input));
        } else if (input instanceof Object[]) {
            Object[] array = (Object[]) input;
            return Arrays.stream(array).distinct().toArray();
        }
        
        return input;
    }
    
    private Object groupData(Object input) {
        if (input == null) return null;
        
        String keyField = (String) parameters.getOrDefault("key_field", "id");
        
        if (input instanceof List) {
            List<?> list = (List<?>) input;
            Map<String, List<Object>> groups = new HashMap<>();
            
            for (Object item : list) {
                if (item instanceof Map) {
                    Map<?, ?> map = (Map<?, ?>) item;
                    String key = map.get(keyField) != null ? map.get(keyField).toString() : "unknown";
                    groups.computeIfAbsent(key, k -> new ArrayList<>()).add(item);
                }
            }
            
            return groups;
        }
        
        return input;
    }
    
    private Object aggregateData(Object input) {
        if (input == null) return null;
        
        String operation = (String) parameters.getOrDefault("aggregate_operation", "sum");
        String field = (String) parameters.getOrDefault("field", "value");
        
        if (input instanceof List) {
            List<?> list = (List<?>) input;
            
            switch (operation.toLowerCase()) {
                case "sum":
                    return list.stream()
                        .filter(item -> item instanceof Map)
                        .mapToDouble(item -> {
                            Map<?, ?> map = (Map<?, ?>) item;
                            Object value = map.get(field);
                            return value instanceof Number ? ((Number) value).doubleValue() : 0.0;
                        })
                        .sum();
                case "average":
                    return list.stream()
                        .filter(item -> item instanceof Map)
                        .mapToDouble(item -> {
                            Map<?, ?> map = (Map<?, ?>) item;
                            Object value = map.get(field);
                            return value instanceof Number ? ((Number) value).doubleValue() : 0.0;
                        })
                        .average()
                        .orElse(0.0);
                case "count":
                    return list.size();
                case "min":
                    return list.stream()
                        .filter(item -> item instanceof Map)
                        .mapToDouble(item -> {
                            Map<?, ?> map = (Map<?, ?>) item;
                            Object value = map.get(field);
                            return value instanceof Number ? ((Number) value).doubleValue() : Double.MAX_VALUE;
                        })
                        .min()
                        .orElse(0.0);
                case "max":
                    return list.stream()
                        .filter(item -> item instanceof Map)
                        .mapToDouble(item -> {
                            Map<?, ?> map = (Map<?, ?>) item;
                            Object value = map.get(field);
                            return value instanceof Number ? ((Number) value).doubleValue() : Double.MIN_VALUE;
                        })
                        .max()
                        .orElse(0.0);
                default:
                    return input;
            }
        }
        
        return input;
    }
    
    private Object mapData(Object input) {
        if (input == null) return null;
        
        String transform = (String) parameters.getOrDefault("transform", "uppercase");
        
        if (input instanceof List) {
            List<?> list = (List<?>) input;
            return list.stream()
                .map(item -> transformItem(item, transform))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        } else if (input instanceof Object[]) {
            Object[] array = (Object[]) input;
            return Arrays.stream(array)
                .map(item -> transformItem(item, transform))
                .toArray();
        }
        
        return transformItem(input, transform);
    }
    
    private Object reduceData(Object input) {
        if (input == null) return null;
        
        String operation = (String) parameters.getOrDefault("reduce_operation", "concat");
        String initial = (String) parameters.getOrDefault("initial", "");
        
        if (input instanceof List) {
            List<?> list = (List<?>) input;
            return list.stream()
                .map(Object::toString)
                .reduce(initial, (a, b) -> a + b);
        }
        
        return input;
    }
    
    private Object flattenData(Object input) {
        if (input == null) return null;
        
        List<Object> flattened = new ArrayList<>();
        flattenRecursive(input, flattened);
        return flattened;
    }
    
    private void flattenRecursive(Object input, List<Object> result) {
        if (input instanceof Collection) {
            for (Object item : (Collection<?>) input) {
                flattenRecursive(item, result);
            }
        } else if (input instanceof Object[]) {
            for (Object item : (Object[]) input) {
                flattenRecursive(item, result);
            }
        } else {
            result.add(input);
        }
    }
    
    private Object nestData(Object input) {
        if (input == null) return null;
        
        String keyField = (String) parameters.getOrDefault("key_field", "id");
        String parentField = (String) parameters.getOrDefault("parent_field", "parent_id");
        
        if (input instanceof List) {
            List<?> list = (List<?>) input;
            Map<String, Object> nested = new HashMap<>();
            
            // Primeiro, criar mapa de todos os itens
            Map<String, Object> itemMap = new HashMap<>();
            for (Object item : list) {
                if (item instanceof Map) {
                    Map<?, ?> map = (Map<?, ?>) item;
                    String key = map.get(keyField) != null ? map.get(keyField).toString() : "item_" + itemMap.size();
                    itemMap.put(key, new HashMap<>(map));
                }
            }
            
            // Construir estrutura aninhada
            for (Object item : list) {
                if (item instanceof Map) {
                    Map<?, ?> map = (Map<?, ?>) item;
                    String key = map.get(keyField) != null ? map.get(keyField).toString() : "item_" + itemMap.size();
                    String parentKey = map.get(parentField) != null ? map.get(parentField).toString() : null;
                    
                    if (parentKey == null || !itemMap.containsKey(parentKey)) {
                        nested.put(key, itemMap.get(key));
                    } else {
                        Map<String, Object> parent = (Map<String, Object>) itemMap.get(parentKey);
                        if (!parent.containsKey("children")) {
                            parent.put("children", new ArrayList<>());
                        }
                        ((List<Object>) parent.get("children")).add(itemMap.get(key));
                    }
                }
            }
            
            return nested;
        }
        
        return input;
    }
    
    private Object mergeData(Object input) {
        if (input == null) return null;
        
        if (input instanceof List) {
            List<?> list = (List<?>) input;
            Map<String, Object> merged = new HashMap<>();
            
            for (Object item : list) {
                if (item instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> map = (Map<String, Object>) item;
                    merged.putAll(map);
                }
            }
            
            return merged;
        }
        
        return input;
    }
    
    private Object diffData(Object input) {
        if (input == null) return null;
        
        Object compareWith = parameters.get("compare_with");
        if (compareWith == null) return input;
        
        if (input instanceof List && compareWith instanceof List) {
            List<?> list1 = (List<?>) input;
            List<?> list2 = (List<?>) compareWith;
            
            List<Object> diff = new ArrayList<>(list1);
            diff.removeAll(list2);
            
            return diff;
        }
        
        return input;
    }
    
    // Métodos auxiliares para formatação
    private String capitalizeWords(String text) {
        if (text == null || text.isEmpty()) return text;
        
        String[] words = text.split("\\s+");
        StringBuilder result = new StringBuilder();
        
        for (int i = 0; i < words.length; i++) {
            if (i > 0) result.append(" ");
            if (!words[i].isEmpty()) {
                result.append(words[i].substring(0, 1).toUpperCase())
                      .append(words[i].substring(1).toLowerCase());
            }
        }
        
        return result.toString();
    }
    
    private String toCamelCase(String text) {
        if (text == null || text.isEmpty()) return text;
        
        String[] words = text.split("[\\s_-]+");
        StringBuilder result = new StringBuilder(words[0].toLowerCase());
        
        for (int i = 1; i < words.length; i++) {
            if (!words[i].isEmpty()) {
                result.append(words[i].substring(0, 1).toUpperCase())
                      .append(words[i].substring(1).toLowerCase());
            }
        }
        
        return result.toString();
    }
    
    private String toSnakeCase(String text) {
        if (text == null || text.isEmpty()) return text;
        
        return text.replaceAll("([a-z])([A-Z])", "$1_$2")
                   .replaceAll("[\\s-]+", "_")
                   .toLowerCase();
    }
    
    private String toKebabCase(String text) {
        if (text == null || text.isEmpty()) return text;
        
        return text.replaceAll("([a-z])([A-Z])", "$1-$2")
                   .replaceAll("[\\s_]+", "-")
                   .toLowerCase();
    }
    
    private String toTitleCase(String text) {
        if (text == null || text.isEmpty()) return text;
        
        String[] words = text.split("\\s+");
        StringBuilder result = new StringBuilder();
        
        for (int i = 0; i < words.length; i++) {
            if (i > 0) result.append(" ");
            if (!words[i].isEmpty()) {
                result.append(words[i].substring(0, 1).toUpperCase())
                      .append(words[i].substring(1).toLowerCase());
            }
        }
        
        return result.toString();
    }
    
    private Object transformItem(Object item, String transform) {
        if (item == null) return null;
        
        String str = item.toString();
        switch (transform.toLowerCase()) {
            case "uppercase":
                return str.toUpperCase();
            case "lowercase":
                return str.toLowerCase();
            case "capitalize":
                return capitalizeWords(str);
            case "reverse":
                return new StringBuilder(str).reverse().toString();
            case "length":
                return str.length();
            default:
                return item;
        }
    }
    
    private Object getInputData(ExecutionContext context) {
        // Buscar dados de entrada do contexto
        Object data = context.getGlobalData("data");
        if (data != null) {
            return data;
        }
        
        // Se não encontrar, retornar valor padrão
        return parameters.getOrDefault("default_value", "Hello World");
    }
    
    @Override
    public boolean validate() {
        return operation != null && !operation.trim().isEmpty() && 
               operations.contains(operation.toLowerCase());
    }
    
    @Override
    public Map<String, String> getInputTypes() {
        Map<String, String> inputs = new HashMap<>();
        inputs.put("data", "object");
        inputs.put("parameters", "object");
        return inputs;
    }
    
    @Override
    public Map<String, String> getOutputTypes() {
        Map<String, String> outputs = new HashMap<>();
        outputs.put("output", "object");
        outputs.put("operation", "string");
        outputs.put("success", "boolean");
        outputs.put("input_type", "string");
        outputs.put("output_type", "string");
        return outputs;
    }
    
    @Override
    public String getType() {
        return "advanced_transform";
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
    
    public List<String> getAvailableOperations() {
        return new ArrayList<>(operations);
    }
} 