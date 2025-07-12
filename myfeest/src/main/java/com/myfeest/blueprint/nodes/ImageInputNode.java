package com.myfeest.blueprint.nodes;

import com.myfeest.blueprint.core.ExecutionContext;
import com.myfeest.blueprint.core.Node;

import java.util.HashMap;
import java.util.Map;

/**
 * Nó de entrada de imagem - simula carregamento de imagens
 */
public class ImageInputNode extends Node {
    
    private String source;
    private int width;
    private int height;
    private String format;
    private int channels;
    
    public ImageInputNode(String id, String name) {
        super(id, name);
        this.source = "file";
        this.width = 512;
        this.height = 512;
        this.format = "PNG";
        this.channels = 3; // RGB
    }
    
    public ImageInputNode(String id, String name, String source) {
        super(id, name);
        this.source = source;
        this.width = 512;
        this.height = 512;
        this.format = "PNG";
        this.channels = 3;
    }
    
    @Override
    public Map<String, Object> execute(ExecutionContext context) {
        Map<String, Object> result = new HashMap<>();
        
        // Simula dados de imagem
        Map<String, Object> imageData = new HashMap<>();
        imageData.put("source", source);
        imageData.put("width", width);
        imageData.put("height", height);
        imageData.put("format", format);
        imageData.put("channels", channels);
        imageData.put("size", width * height * channels);
        imageData.put("data", generateMockImageData());
        imageData.put("timestamp", System.currentTimeMillis());
        
        result.put("image", imageData);
        result.put("source", source);
        result.put("format", format);
        result.put("dimensions", width + "x" + height);
        
        // Armazena o resultado no contexto
        context.setNodeResult(getId(), result);
        
        return result;
    }
    
    private int[][][] generateMockImageData() {
        int[][][] data = new int[height][width][channels];
        
        // Gera um gradiente simples
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                data[y][x][0] = (int) ((double) x / width * 255); // R
                data[y][x][1] = (int) ((double) y / height * 255); // G
                data[y][x][2] = 128; // B fixo
            }
        }
        
        return data;
    }
    
    @Override
    public boolean validate() {
        return source != null && !source.trim().isEmpty() && 
               width > 0 && height > 0 && 
               format != null && !format.trim().isEmpty() && 
               channels > 0;
    }
    
    @Override
    public Map<String, String> getInputTypes() {
        return new HashMap<>(); // Nós de entrada não têm entradas
    }
    
    @Override
    public Map<String, String> getOutputTypes() {
        Map<String, String> outputs = new HashMap<>();
        outputs.put("image", "object");
        outputs.put("source", "string");
        outputs.put("format", "string");
        outputs.put("dimensions", "string");
        return outputs;
    }
    
    // Getters e Setters específicos
    public String getSource() {
        return source;
    }
    
    public void setSource(String source) {
        this.source = source;
    }
    
    public int getWidth() {
        return width;
    }
    
    public void setWidth(int width) {
        this.width = width;
    }
    
    public int getHeight() {
        return height;
    }
    
    public void setHeight(int height) {
        this.height = height;
    }
    
    public String getFormat() {
        return format;
    }
    
    public void setFormat(String format) {
        this.format = format;
    }
    
    public int getChannels() {
        return channels;
    }
    
    public void setChannels(int channels) {
        this.channels = channels;
    }
} 