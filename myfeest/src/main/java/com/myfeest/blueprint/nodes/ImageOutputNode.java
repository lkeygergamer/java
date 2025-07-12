package com.myfeest.blueprint.nodes;

import com.myfeest.blueprint.core.ExecutionContext;
import com.myfeest.blueprint.core.Node;

import java.util.HashMap;
import java.util.Map;

/**
 * Nó de saída de imagem - processa e disponibiliza imagens
 */
public class ImageOutputNode extends Node {
    
    private String format;
    private String destination;
    private int quality;
    private boolean includeMetadata;
    
    public ImageOutputNode(String id, String name) {
        super(id, name);
        this.format = "PNG";
        this.destination = "file";
        this.quality = 90;
        this.includeMetadata = false;
    }
    
    public ImageOutputNode(String id, String name, String destination) {
        super(id, name);
        this.format = "PNG";
        this.destination = destination;
        this.quality = 90;
        this.includeMetadata = false;
    }
    
    @Override
    public Map<String, Object> execute(ExecutionContext context) {
        Map<String, Object> result = new HashMap<>();
        
        // Busca dados de imagem de entrada
        Object imageInput = getInput("image");
        if (imageInput == null) {
            imageInput = getInput("value");
        }
        
        if (imageInput == null) {
            result.put("error", "Nenhum dado de imagem fornecido");
            return result;
        }
        
        // Processa a imagem
        Map<String, Object> imageData = processImage(imageInput);
        
        result.put("image", imageData);
        result.put("format", format);
        result.put("destination", destination);
        result.put("quality", quality);
        result.put("includeMetadata", includeMetadata);
        result.put("status", "ready");
        result.put("timestamp", System.currentTimeMillis());
        
        // Armazena o resultado no contexto
        context.setNodeResult(getId(), result);
        
        return result;
    }
    
    private Map<String, Object> processImage(Object imageInput) {
        Map<String, Object> processed = new HashMap<>();
        
        if (imageInput instanceof Map) {
            Map<String, Object> inputMap = (Map<String, Object>) imageInput;
            
            // Copia dados da imagem
            processed.putAll(inputMap);
            
            // Inclui metadados se solicitado
            if (includeMetadata) {
                Map<String, Object> metadata = new HashMap<>();
                metadata.put("originalFormat", inputMap.get("format"));
                metadata.put("originalDimensions", inputMap.get("dimensions"));
                metadata.put("originalSource", inputMap.get("source"));
                processed.put("metadata", metadata);
            }
        } else {
            processed.put("data", imageInput);
        }
        
        processed.put("processedFormat", format);
        processed.put("processedQuality", quality);
        processed.put("destination", destination);
        processed.put("fileSize", estimateFileSize(processed));
        
        return processed;
    }
    
    private long estimateFileSize(Map<String, Object> imageData) {
        // Estimativa simples baseada no formato e qualidade
        long baseSize = 100000; // 100KB base
        
        if (format.equalsIgnoreCase("JPEG") || format.equalsIgnoreCase("JPG")) {
            baseSize = (long) (baseSize * (quality / 100.0));
        } else if (format.equalsIgnoreCase("PNG")) {
            baseSize = baseSize * 2; // PNG geralmente é maior
        } else if (format.equalsIgnoreCase("GIF")) {
            baseSize = baseSize / 2; // GIF pode ser menor
        }
        
        return baseSize;
    }
    
    @Override
    public boolean validate() {
        return format != null && !format.trim().isEmpty() && 
               destination != null && !destination.trim().isEmpty() && 
               quality >= 1 && quality <= 100;
    }
    
    @Override
    public Map<String, String> getInputTypes() {
        Map<String, String> inputs = new HashMap<>();
        inputs.put("image", "object");
        inputs.put("value", "object");
        return inputs;
    }
    
    @Override
    public Map<String, String> getOutputTypes() {
        Map<String, String> outputs = new HashMap<>();
        outputs.put("image", "object");
        outputs.put("format", "string");
        outputs.put("destination", "string");
        outputs.put("quality", "int");
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
    
    public int getQuality() {
        return quality;
    }
    
    public void setQuality(int quality) {
        this.quality = Math.max(1, Math.min(100, quality));
    }
    
    public boolean isIncludeMetadata() {
        return includeMetadata;
    }
    
    public void setIncludeMetadata(boolean includeMetadata) {
        this.includeMetadata = includeMetadata;
    }
} 