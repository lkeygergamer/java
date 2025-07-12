package com.myfeest.blueprint.nodes;

import com.myfeest.blueprint.core.ExecutionContext;
import com.myfeest.blueprint.core.Node;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Nó de IA - processa dados usando modelos de IA simulados
 */
public class AINode extends Node {
    
    private String model;
    private String task;
    private Map<String, Object> parameters;
    private Random random;
    
    public AINode(String id, String name) {
        super(id, name);
        this.model = "gpt-3.5-turbo";
        this.task = "text-generation";
        this.parameters = new HashMap<>();
        this.random = new Random();
    }
    
    public AINode(String id, String name, String model) {
        super(id, name);
        this.model = model;
        this.task = "text-generation";
        this.parameters = new HashMap<>();
        this.random = new Random();
    }
    
    @Override
    public Map<String, Object> execute(ExecutionContext context) {
        Map<String, Object> result = new HashMap<>();
        
        // Busca dados de entrada
        Object inputValue = getInput("text");
        if (inputValue == null) {
            inputValue = getInput("prompt");
        }
        if (inputValue == null) {
            inputValue = getInput("value");
        }
        
        if (inputValue == null) {
            result.put("error", "Nenhum dado de entrada fornecido para processamento de IA");
            return result;
        }
        
        // Processa com IA simulada
        Object aiResult = processWithAI(inputValue, model, task, parameters);
        
        result.put("value", aiResult);
        result.put("output", aiResult);
        result.put("model", model);
        result.put("task", task);
        result.put("confidence", random.nextDouble() * 0.3 + 0.7); // 70-100%
        result.put("processing_time", random.nextInt(1000) + 500); // 500-1500ms
        result.put("tokens_used", estimateTokens(inputValue.toString()));
        
        // Armazena o resultado no contexto
        context.setNodeResult(getId(), result);
        
        return result;
    }
    
    private Object processWithAI(Object input, String model, String task, Map<String, Object> params) {
        String inputStr = input.toString();
        
        switch (task.toLowerCase()) {
            case "text-generation":
                return generateText(inputStr, model);
                
            case "text-completion":
                return completeText(inputStr, model);
                
            case "translation":
                return translateText(inputStr, model);
                
            case "summarization":
                return summarizeText(inputStr, model);
                
            case "sentiment-analysis":
                return analyzeSentiment(inputStr, model);
                
            case "image-generation":
                return generateImagePrompt(inputStr, model);
                
            case "code-generation":
                return generateCode(inputStr, model);
                
            default:
                return "Processado por " + model + ": " + inputStr;
        }
    }
    
    private String generateText(String prompt, String model) {
        String[] responses = {
            "Baseado no seu prompt, aqui está uma resposta criativa e informativa...",
            "Analisando sua solicitação, posso oferecer a seguinte perspectiva...",
            "Considerando os parâmetros fornecidos, sugiro a seguinte abordagem...",
            "Com base na minha análise, aqui está minha recomendação...",
            "Explorando as possibilidades, encontrei uma solução interessante..."
        };
        
        return responses[random.nextInt(responses.length)] + 
               " (Processado por " + model + ")";
    }
    
    private String completeText(String text, String model) {
        String[] completions = {
            " e isso completa a ideia de forma satisfatória.",
            " levando a uma conclusão lógica e bem estruturada.",
            " resultando em uma solução eficaz e elegante.",
            " demonstrando a aplicabilidade prática do conceito.",
            " estabelecendo uma base sólida para futuras implementações."
        };
        
        return text + completions[random.nextInt(completions.length)];
    }
    
    private String translateText(String text, String model) {
        // Simula tradução
        return "[Traduzido por " + model + "]: " + text.toUpperCase();
    }
    
    private String summarizeText(String text, String model) {
        int maxLength = Math.min(100, text.length());
        return "[Resumo por " + model + "]: " + text.substring(0, maxLength) + "...";
    }
    
    private String analyzeSentiment(String text, String model) {
        String[] sentiments = {"Positivo", "Neutro", "Negativo"};
        return "[Análise de sentimento por " + model + "]: " + 
               sentiments[random.nextInt(sentiments.length)];
    }
    
    private String generateImagePrompt(String text, String model) {
        return "[Prompt de imagem por " + model + "]: Uma representação visual de: " + text;
    }
    
    private String generateCode(String text, String model) {
        return "// Código gerado por " + model + "\n" +
               "public class GeneratedCode {\n" +
               "    public static void main(String[] args) {\n" +
               "        System.out.println(\"" + text + "\");\n" +
               "    }\n" +
               "}";
    }
    
    private int estimateTokens(String text) {
        return Math.max(1, text.length() / 4);
    }
    
    @Override
    public boolean validate() {
        return model != null && !model.trim().isEmpty() && 
               task != null && !task.trim().isEmpty();
    }
    
    @Override
    public Map<String, String> getInputTypes() {
        Map<String, String> inputs = new HashMap<>();
        inputs.put("text", "string");
        inputs.put("prompt", "string");
        inputs.put("value", "object");
        return inputs;
    }
    
    @Override
    public Map<String, String> getOutputTypes() {
        Map<String, String> outputs = new HashMap<>();
        outputs.put("result", "object");
        outputs.put("model", "string");
        outputs.put("task", "string");
        outputs.put("confidence", "double");
        outputs.put("processing_time", "int");
        outputs.put("tokens_used", "int");
        return outputs;
    }
    
    // Getters e Setters específicos
    public String getModel() {
        return model;
    }
    
    public void setModel(String model) {
        this.model = model;
    }
    
    public String getTask() {
        return task;
    }
    
    public void setTask(String task) {
        this.task = task;
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