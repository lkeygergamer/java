package com.myfeest.blueprint.nodes.custom;

import com.myfeest.blueprint.core.ExecutionContext;
import com.myfeest.blueprint.core.Node;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Nó personalizado para chamadas de API REST
 */
public class APINode extends Node {
    
    private String method; // GET, POST, PUT, DELETE
    private String url;
    private Map<String, Object> headers;
    private Map<String, Object> body;
    private Random random;
    
    public APINode(String id, String name) {
        super(id, name);
        this.method = "GET";
        this.url = "https://api.example.com/data";
        this.headers = new HashMap<>();
        this.body = new HashMap<>();
        this.random = new Random();
    }
    
    public APINode(String id, String name, String method, String url) {
        super(id, name);
        this.method = method;
        this.url = url;
        this.headers = new HashMap<>();
        this.body = new HashMap<>();
        this.random = new Random();
    }
    
    @Override
    public Map<String, Object> execute(ExecutionContext context) {
        Map<String, Object> result = new HashMap<>();
        
        // Simula chamada de API
        Object apiResult = simulateAPICall(method, url, headers, body);
        
        result.put("value", apiResult);
        result.put("output", apiResult);
        result.put("method", method);
        result.put("url", url);
        result.put("status_code", getStatusCode());
        result.put("response_time", random.nextInt(2000) + 100);
        result.put("success", isSuccess());
        
        // Armazena o resultado no contexto
        context.setNodeResult(getId(), result);
        
        return result;
    }
    
    private Object simulateAPICall(String method, String url, Map<String, Object> headers, Map<String, Object> body) {
        switch (method.toUpperCase()) {
            case "GET":
                return simulateGET(url);
            case "POST":
                return simulatePOST(url, body);
            case "PUT":
                return simulatePUT(url, body);
            case "DELETE":
                return simulateDELETE(url);
            default:
                return "Método não suportado: " + method;
        }
    }
    
    private Object simulateGET(String url) {
        Map<String, Object> response = new HashMap<>();
        response.put("data", generateMockData());
        response.put("total", random.nextInt(100) + 1);
        response.put("page", 1);
        response.put("limit", 10);
        return response;
    }
    
    private Object simulatePOST(String url, Map<String, Object> body) {
        Map<String, Object> response = new HashMap<>();
        response.put("id", random.nextInt(10000));
        response.put("created", true);
        response.put("data", body);
        return response;
    }
    
    private Object simulatePUT(String url, Map<String, Object> body) {
        Map<String, Object> response = new HashMap<>();
        response.put("id", random.nextInt(10000));
        response.put("updated", true);
        response.put("data", body);
        return response;
    }
    
    private Object simulateDELETE(String url) {
        Map<String, Object> response = new HashMap<>();
        response.put("deleted", true);
        response.put("id", random.nextInt(10000));
        return response;
    }
    
    private Object[] generateMockData() {
        Object[] data = new Object[random.nextInt(5) + 1];
        for (int i = 0; i < data.length; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", i + 1);
            item.put("name", "Item " + (i + 1));
            item.put("description", "Descrição do item " + (i + 1));
            item.put("price", random.nextDouble() * 100);
            item.put("active", random.nextBoolean());
            data[i] = item;
        }
        return data;
    }
    
    private int getStatusCode() {
        // 90% de chance de sucesso (200-299)
        if (random.nextDouble() < 0.9) {
            return 200 + random.nextInt(100);
        } else {
            // 10% de chance de erro (400-500)
            return 400 + random.nextInt(100);
        }
    }
    
    private boolean isSuccess() {
        int status = getStatusCode();
        return status >= 200 && status < 300;
    }
    
    @Override
    public boolean validate() {
        return method != null && !method.trim().isEmpty() && 
               url != null && !url.trim().isEmpty();
    }
    
    @Override
    public Map<String, String> getInputTypes() {
        Map<String, String> inputs = new HashMap<>();
        inputs.put("headers", "object");
        inputs.put("body", "object");
        inputs.put("params", "object");
        return inputs;
    }
    
    @Override
    public Map<String, String> getOutputTypes() {
        Map<String, String> outputs = new HashMap<>();
        outputs.put("response", "object");
        outputs.put("method", "string");
        outputs.put("url", "string");
        outputs.put("status_code", "int");
        outputs.put("response_time", "int");
        outputs.put("success", "boolean");
        return outputs;
    }
    
    @Override
    public String getType() {
        return "api";
    }

    @Override
    public boolean isInputNode() {
        // Pode ser usado como entrada se for GET
        return "GET".equalsIgnoreCase(method);
    }

    @Override
    public boolean isOutputNode() {
        // Pode ser usado como saída se for POST/PUT/DELETE
        return "POST".equalsIgnoreCase(method) || "PUT".equalsIgnoreCase(method) || "DELETE".equalsIgnoreCase(method);
    }
    
    // Getters e Setters específicos
    public String getMethod() {
        return method;
    }
    
    public void setMethod(String method) {
        this.method = method;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public Map<String, Object> getHeaders() {
        return new HashMap<>(headers);
    }
    
    public void setHeaders(Map<String, Object> headers) {
        this.headers = new HashMap<>(headers);
    }
    
    public void setHeader(String key, Object value) {
        this.headers.put(key, value);
    }
    
    public Object getHeader(String key) {
        return this.headers.get(key);
    }
    
    public Map<String, Object> getBody() {
        return new HashMap<>(body);
    }
    
    public void setBody(Map<String, Object> body) {
        this.body = new HashMap<>(body);
    }
    
    public void setBodyField(String key, Object value) {
        this.body.put(key, value);
    }
    
    public Object getBodyField(String key) {
        return this.body.get(key);
    }
} 