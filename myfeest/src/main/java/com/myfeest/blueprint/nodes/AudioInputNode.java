package com.myfeest.blueprint.nodes;

import com.myfeest.blueprint.core.ExecutionContext;
import com.myfeest.blueprint.core.Node;

import java.util.HashMap;
import java.util.Map;

/**
 * Nó de entrada de áudio - simula captura de áudio
 */
public class AudioInputNode extends Node {
    
    private String source;
    private int sampleRate;
    private int channels;
    private double duration;
    
    public AudioInputNode(String id, String name) {
        super(id, name);
        this.source = "microphone";
        this.sampleRate = 44100;
        this.channels = 2;
        this.duration = 5.0; // 5 segundos
    }
    
    public AudioInputNode(String id, String name, String source) {
        super(id, name);
        this.source = source;
        this.sampleRate = 44100;
        this.channels = 2;
        this.duration = 5.0;
    }
    
    @Override
    public Map<String, Object> execute(ExecutionContext context) {
        Map<String, Object> result = new HashMap<>();
        
        // Simula dados de áudio
        Map<String, Object> audioData = new HashMap<>();
        audioData.put("source", source);
        audioData.put("sampleRate", sampleRate);
        audioData.put("channels", channels);
        audioData.put("duration", duration);
        audioData.put("data", generateMockAudioData());
        audioData.put("timestamp", System.currentTimeMillis());
        
        result.put("audio", audioData);
        result.put("source", source);
        result.put("format", "PCM");
        
        // Armazena o resultado no contexto
        context.setNodeResult(getId(), result);
        
        return result;
    }
    
    private double[] generateMockAudioData() {
        int samples = (int) (sampleRate * duration);
        double[] data = new double[samples];
        
        // Gera uma onda senoidal simples
        double frequency = 440.0; // Lá 440Hz
        for (int i = 0; i < samples; i++) {
            data[i] = Math.sin(2 * Math.PI * frequency * i / sampleRate);
        }
        
        return data;
    }
    
    @Override
    public boolean validate() {
        return source != null && !source.trim().isEmpty() && 
               sampleRate > 0 && channels > 0 && duration > 0;
    }
    
    @Override
    public Map<String, String> getInputTypes() {
        return new HashMap<>(); // Nós de entrada não têm entradas
    }
    
    @Override
    public Map<String, String> getOutputTypes() {
        Map<String, String> outputs = new HashMap<>();
        outputs.put("audio", "object");
        outputs.put("source", "string");
        outputs.put("format", "string");
        return outputs;
    }
    
    // Getters e Setters específicos
    public String getSource() {
        return source;
    }
    
    public void setSource(String source) {
        this.source = source;
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
    
    public double getDuration() {
        return duration;
    }
    
    public void setDuration(double duration) {
        this.duration = duration;
    }
} 