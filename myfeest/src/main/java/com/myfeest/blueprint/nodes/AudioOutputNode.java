package com.myfeest.blueprint.nodes;

import com.myfeest.blueprint.core.ExecutionContext;
import com.myfeest.blueprint.core.Node;

import java.util.HashMap;
import java.util.Map;

/**
 * Nó de saída de áudio - processa e disponibiliza áudio
 */
public class AudioOutputNode extends Node {
    
    private String format;
    private String destination;
    private int sampleRate;
    private int channels;
    
    public AudioOutputNode(String id, String name) {
        super(id, name);
        this.format = "WAV";
        this.destination = "speaker";
        this.sampleRate = 44100;
        this.channels = 2;
    }
    
    public AudioOutputNode(String id, String name, String destination) {
        super(id, name);
        this.format = "WAV";
        this.destination = destination;
        this.sampleRate = 44100;
        this.channels = 2;
    }
    
    @Override
    public Map<String, Object> execute(ExecutionContext context) {
        Map<String, Object> result = new HashMap<>();
        
        // Busca dados de áudio de entrada
        Object audioInput = getInput("audio");
        if (audioInput == null) {
            audioInput = getInput("value");
        }
        
        if (audioInput == null) {
            result.put("error", "Nenhum dado de áudio fornecido");
            return result;
        }
        
        // Processa o áudio
        Map<String, Object> audioData = processAudio(audioInput);
        
        result.put("audio", audioData);
        result.put("format", format);
        result.put("destination", destination);
        result.put("sampleRate", sampleRate);
        result.put("channels", channels);
        result.put("status", "ready");
        result.put("timestamp", System.currentTimeMillis());
        
        // Armazena o resultado no contexto
        context.setNodeResult(getId(), result);
        
        return result;
    }
    
    private Map<String, Object> processAudio(Object audioInput) {
        Map<String, Object> processed = new HashMap<>();
        
        if (audioInput instanceof Map) {
            Map<String, Object> inputMap = (Map<String, Object>) audioInput;
            processed.putAll(inputMap);
        } else {
            processed.put("data", audioInput);
        }
        
        processed.put("processedFormat", format);
        processed.put("processedSampleRate", sampleRate);
        processed.put("processedChannels", channels);
        processed.put("destination", destination);
        
        return processed;
    }
    
    @Override
    public boolean validate() {
        return format != null && !format.trim().isEmpty() && 
               destination != null && !destination.trim().isEmpty() && 
               sampleRate > 0 && channels > 0;
    }
    
    @Override
    public Map<String, String> getInputTypes() {
        Map<String, String> inputs = new HashMap<>();
        inputs.put("audio", "object");
        inputs.put("value", "object");
        return inputs;
    }
    
    @Override
    public Map<String, String> getOutputTypes() {
        Map<String, String> outputs = new HashMap<>();
        outputs.put("audio", "object");
        outputs.put("format", "string");
        outputs.put("destination", "string");
        outputs.put("sampleRate", "int");
        outputs.put("channels", "int");
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
    
    public int getSampleRate() {
        return sampleRate;
    }
    
    public void setSampleRate(int sampleRate) {
        this.sampleRate = sampleRate;
    }
    
    public int getChannels() {
        return channels;
    }
    
    public void setChannels(int channels) {
        this.channels = channels;
    }
} 