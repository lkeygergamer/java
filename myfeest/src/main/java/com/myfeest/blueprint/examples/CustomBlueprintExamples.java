package com.myfeest.blueprint.examples;

import com.myfeest.blueprint.core.*;
import com.myfeest.blueprint.nodes.*;
import com.myfeest.blueprint.engine.BlueprintEngine;
import com.myfeest.blueprint.serializer.SimpleJsonSerializer;

import java.util.Map;

/**
 * Exemplos práticos de como criar blueprints personalizados usando a API
 */
public class CustomBlueprintExamples {
    
    public static void main(String[] args) {
        System.out.println("=== EXEMPLOS DE BLUEPRINTS PERSONALIZADOS ===\n");
        
        // Exemplo 1: Processador de texto
        createTextProcessor();
        
        // Exemplo 2: Calculadora de estatísticas
        createStatisticsCalculator();
        
        // Exemplo 3: Pipeline de IA para análise de sentimentos
        createSentimentAnalysisPipeline();
        
        // Exemplo 4: Sistema de notificações
        createNotificationSystem();
    }
    
    /**
     * Exemplo 1: Processador de texto com múltiplas transformações
     */
    private static void createTextProcessor() {
        System.out.println("1. PROCESSADOR DE TEXTO");
        System.out.println("========================");
        
        BlueprintEngine engine = new BlueprintEngine();
        
        // Criar nós
        InputNode textInput = new InputNode("input_1", "Texto original");
        textInput.setValue("Olá, este é um texto de exemplo para processamento!");
        
        TransformNode toUpper = new TransformNode("upper_1", "Converter para maiúsculas");
        toUpper.setOperation("uppercase");
        
        TransformNode reverse = new TransformNode("reverse_1", "Inverter texto");
        reverse.setOperation("reverse");
        
        FilterNode lengthFilter = new FilterNode("filter_1", "Filtrar por comprimento");
        lengthFilter.setCondition("length > 10");
        
        OutputNode finalOutput = new OutputNode("output_1", "Texto processado");
        
        // Criar blueprint
        Blueprint textProcessor = new Blueprint("text_processor");
        textProcessor.addNode(textInput);
        textProcessor.addNode(toUpper);
        textProcessor.addNode(reverse);
        textProcessor.addNode(lengthFilter);
        textProcessor.addNode(finalOutput);
        
        // Conexões: input -> uppercase -> reverse -> filter -> output
        textProcessor.addConnection(new Connection("c1", textInput.getId(), toUpper.getId()));
        textProcessor.addConnection(new Connection("c2", toUpper.getId(), reverse.getId()));
        textProcessor.addConnection(new Connection("c3", reverse.getId(), lengthFilter.getId()));
        textProcessor.addConnection(new Connection("c4", lengthFilter.getId(), finalOutput.getId()));
        
        System.out.println("Blueprint criado: " + textProcessor.getNodes().size() + " nós, " + 
                          textProcessor.getConnections().size() + " conexões");
        
        // Executar
        try {
            Map<String, Object> result = engine.execute(textProcessor);
            System.out.println("Resultado: " + result);
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
        System.out.println();
    }
    
    /**
     * Exemplo 2: Calculadora de estatísticas de números
     */
    private static void createStatisticsCalculator() {
        System.out.println("2. CALCULADORA DE ESTATÍSTICAS");
        System.out.println("===============================");
        
        BlueprintEngine engine = new BlueprintEngine();
        
        // Criar nós de entrada com diferentes números
        InputNode num1 = new InputNode("num_1", "Número 1");
        num1.setValue(10);
        
        InputNode num2 = new InputNode("num_2", "Número 2");
        num2.setValue(20);
        
        InputNode num3 = new InputNode("num_3", "Número 3");
        num3.setValue(30);
        
        // Nós de processamento
        TransformNode sum = new TransformNode("sum_1", "Soma");
        sum.setOperation("add");
        sum.setParameter("addend", 0); // Será calculado dinamicamente
        
        TransformNode multiply = new TransformNode("mult_1", "Multiplicação");
        multiply.setOperation("multiply");
        multiply.setParameter("multiplier", 2);
        
        FilterNode positiveFilter = new FilterNode("pos_filter", "Apenas positivos");
        positiveFilter.setCondition("positive");
        
        OutputNode statsOutput = new OutputNode("stats_out", "Estatísticas");
        
        // Criar blueprint
        Blueprint statsCalculator = new Blueprint("statistics_calculator");
        statsCalculator.addNode(num1);
        statsCalculator.addNode(num2);
        statsCalculator.addNode(num3);
        statsCalculator.addNode(sum);
        statsCalculator.addNode(multiply);
        statsCalculator.addNode(positiveFilter);
        statsCalculator.addNode(statsOutput);
        
        // Conexões
        statsCalculator.addConnection(new Connection("c1", num1.getId(), sum.getId()));
        statsCalculator.addConnection(new Connection("c2", sum.getId(), multiply.getId()));
        statsCalculator.addConnection(new Connection("c3", multiply.getId(), positiveFilter.getId()));
        statsCalculator.addConnection(new Connection("c4", positiveFilter.getId(), statsOutput.getId()));
        
        System.out.println("Blueprint criado: " + statsCalculator.getNodes().size() + " nós, " + 
                          statsCalculator.getConnections().size() + " conexões");
        System.out.println();
    }
    
    /**
     * Exemplo 3: Pipeline de IA para análise de sentimentos
     */
    private static void createSentimentAnalysisPipeline() {
        System.out.println("3. PIPELINE DE ANÁLISE DE SENTIMENTOS");
        System.out.println("=====================================");
        
        BlueprintEngine engine = new BlueprintEngine();
        
        // Nós de entrada
        TextInputNode textInput = new TextInputNode("text_input", "Comentário do usuário");
        textInput.setPrompt("Este produto é incrível! Recomendo para todos.");
        
        // Nós de processamento
        AINode sentimentAI = new AINode("sentiment_ai", "Análise de Sentimento");
        sentimentAI.setModel("gpt-4");
        sentimentAI.setTask("sentiment-analysis");
        
        FilterNode positiveFilter = new FilterNode("positive_filter", "Filtrar positivos");
        positiveFilter.setCondition("contains positive");
        
        TransformNode responseGen = new TransformNode("response_gen", "Gerar resposta");
        responseGen.setOperation("uppercase");
        
        // Nós de saída
        TextOutputNode sentimentOutput = new TextOutputNode("sentiment_out", "Resultado da análise");
        TextOutputNode responseOutput = new TextOutputNode("response_out", "Resposta gerada");
        
        // Criar blueprint
        Blueprint sentimentPipeline = new Blueprint("sentiment_analysis_pipeline");
        sentimentPipeline.addNode(textInput);
        sentimentPipeline.addNode(sentimentAI);
        sentimentPipeline.addNode(positiveFilter);
        sentimentPipeline.addNode(responseGen);
        sentimentPipeline.addNode(sentimentOutput);
        sentimentPipeline.addNode(responseOutput);
        
        // Conexões
        sentimentPipeline.addConnection(new Connection("c1", textInput.getId(), sentimentAI.getId()));
        sentimentPipeline.addConnection(new Connection("c2", sentimentAI.getId(), sentimentOutput.getId()));
        sentimentPipeline.addConnection(new Connection("c3", sentimentAI.getId(), positiveFilter.getId()));
        sentimentPipeline.addConnection(new Connection("c4", positiveFilter.getId(), responseGen.getId()));
        sentimentPipeline.addConnection(new Connection("c5", responseGen.getId(), responseOutput.getId()));
        
        System.out.println("Blueprint criado: " + sentimentPipeline.getNodes().size() + " nós, " + 
                          sentimentPipeline.getConnections().size() + " conexões");
        System.out.println();
    }
    
    /**
     * Exemplo 4: Sistema de notificações
     */
    private static void createNotificationSystem() {
        System.out.println("4. SISTEMA DE NOTIFICAÇÕES");
        System.out.println("==========================");
        
        BlueprintEngine engine = new BlueprintEngine();
        
        // Nós de entrada
        InputNode eventInput = new InputNode("event_input", "Evento do sistema");
        eventInput.setValue("ALERTA: Sistema sobrecarregado");
        
        // Nós de processamento
        FilterNode alertFilter = new FilterNode("alert_filter", "Filtrar alertas");
        alertFilter.setCondition("contains ALERTA");
        
        TransformNode formatMessage = new TransformNode("format_msg", "Formatar mensagem");
        formatMessage.setOperation("uppercase");
        
        AINode priorityAI = new AINode("priority_ai", "Determinar prioridade");
        priorityAI.setModel("gpt-4");
        priorityAI.setTask("text-generation");
        
        // Nós de saída
        TextOutputNode emailOutput = new TextOutputNode("email_out", "Notificação por email");
        TextOutputNode smsOutput = new TextOutputNode("sms_out", "Notificação por SMS");
        
        // Criar blueprint
        Blueprint notificationSystem = new Blueprint("notification_system");
        notificationSystem.addNode(eventInput);
        notificationSystem.addNode(alertFilter);
        notificationSystem.addNode(formatMessage);
        notificationSystem.addNode(priorityAI);
        notificationSystem.addNode(emailOutput);
        notificationSystem.addNode(smsOutput);
        
        // Conexões
        notificationSystem.addConnection(new Connection("c1", eventInput.getId(), alertFilter.getId()));
        notificationSystem.addConnection(new Connection("c2", alertFilter.getId(), formatMessage.getId()));
        notificationSystem.addConnection(new Connection("c3", formatMessage.getId(), priorityAI.getId()));
        notificationSystem.addConnection(new Connection("c4", priorityAI.getId(), emailOutput.getId()));
        notificationSystem.addConnection(new Connection("c5", priorityAI.getId(), smsOutput.getId()));
        
        System.out.println("Blueprint criado: " + notificationSystem.getNodes().size() + " nós, " + 
                          notificationSystem.getConnections().size() + " conexões");
        System.out.println();
    }
} 