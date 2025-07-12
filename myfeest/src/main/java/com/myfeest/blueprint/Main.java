package com.myfeest.blueprint;

import com.myfeest.blueprint.core.*;
import com.myfeest.blueprint.nodes.*;
import com.myfeest.blueprint.engine.BlueprintEngine;
import com.myfeest.blueprint.serializer.SimpleJsonSerializer;

import java.util.List;
import java.util.Map;

/**
 * Sistema de Blueprint em Java
 * Demonstra como criar, conectar e executar nós visuais
 */
public class Main {
    
    public static void main(String[] args) {
        System.out.println("SISTEMA DE BLUEPRINT EM JAVA");
        System.out.println("================================\n");
        
        // 1. Criar o engine de blueprint
        BlueprintEngine engine = new BlueprintEngine();
        
        // 2. Criar nós de exemplo
        System.out.println("Criando nós...");
        
        // Nó de entrada (Input)
        InputNode inputNode = new InputNode("input_1", "Texto de entrada");
        inputNode.setValue("Olá, mundo do Blueprint!");
        
        // Nó de processamento (Transform)
        TransformNode transformNode = new TransformNode("transform_1", "Transformar texto");
        transformNode.setOperation("uppercase");
        
        // Nó de filtro
        FilterNode filterNode = new FilterNode("filter_1", "Filtrar caracteres");
        filterNode.setCondition("length > 5");
        
        // Nó de saída (Output)
        OutputNode outputNode = new OutputNode("output_1", "Resultado final");
        
        // 3. Criar conexões entre os nós
        System.out.println("Conectando nós...");
        
        Connection conn1 = new Connection("conn_1", inputNode.getId(), transformNode.getId());
        Connection conn2 = new Connection("conn_2", transformNode.getId(), filterNode.getId());
        Connection conn3 = new Connection("conn_3", filterNode.getId(), outputNode.getId());
        
        // 4. Criar o blueprint
        Blueprint blueprint = new Blueprint("exemplo_blueprint");
        blueprint.addNode(inputNode);
        blueprint.addNode(transformNode);
        blueprint.addNode(filterNode);
        blueprint.addNode(outputNode);
        blueprint.addConnection(conn1);
        blueprint.addConnection(conn2);
        blueprint.addConnection(conn3);
        
        // 5. Executar o blueprint
        System.out.println("Executando blueprint...\n");
        
        try {
            Map<String, Object> result = engine.execute(blueprint);
            
            System.out.println("Resultado da execução:");
            result.forEach((key, value) -> {
                System.out.println("   " + key + ": " + value);
            });
            
        } catch (Exception e) {
            System.err.println("Erro na execução: " + e.getMessage());
            e.printStackTrace();
        }
        
        // 6. Serializar o blueprint para JSON
        System.out.println("\nSerializando blueprint...");
        SimpleJsonSerializer serializer = new SimpleJsonSerializer();
        String json = serializer.serialize(blueprint);
        System.out.println("JSON gerado:");
        System.out.println(json);
        
        // 7. Demonstrar blueprint mais complexo
        System.out.println("\nCRIANDO BLUEPRINT COMPLEXO - MIXER DE IA");
        System.out.println("=============================================");
        
        createComplexBlueprint(engine);
    }
    
    private static void createComplexBlueprint(BlueprintEngine engine) {
        // Criar um blueprint de "mixer de IA" mais complexo
        Blueprint mixerBlueprint = new Blueprint("ai_mixer");
        
        // Nós de entrada
        AudioInputNode audioInput = new AudioInputNode("audio_1", "Microfone");
        TextInputNode textInput = new TextInputNode("text_1", "Prompt de texto");
        ImageInputNode imageInput = new ImageInputNode("image_1", "Imagem de entrada");
        
        // Nós de processamento
        AINode aiProcessor = new AINode("ai_1", "Processador de IA");
        aiProcessor.setModel("gpt-4");
        
        FilterNode audioFilter = new FilterNode("filter_1", "Filtro de áudio");
        audioFilter.setCondition("frequency > 1000");
        
        TransformNode imageTransform = new TransformNode("transform_1", "Transformar imagem");
        imageTransform.setOperation("resize");
        
        // Nós de saída
        AudioOutputNode audioOutput = new AudioOutputNode("audio_out", "Audio processado");
        TextOutputNode textOutput = new TextOutputNode("text_out", "Texto gerado");
        ImageOutputNode imageOutput = new ImageOutputNode("image_out", "Imagem final");
        
        // Adicionar nós ao blueprint
        mixerBlueprint.addNode(audioInput);
        mixerBlueprint.addNode(textInput);
        mixerBlueprint.addNode(imageInput);
        mixerBlueprint.addNode(aiProcessor);
        mixerBlueprint.addNode(audioFilter);
        mixerBlueprint.addNode(imageTransform);
        mixerBlueprint.addNode(audioOutput);
        mixerBlueprint.addNode(textOutput);
        mixerBlueprint.addNode(imageOutput);
        
        // Criar conexões complexas
        mixerBlueprint.addConnection(new Connection("c1", audioInput.getId(), audioFilter.getId()));
        mixerBlueprint.addConnection(new Connection("c2", textInput.getId(), aiProcessor.getId()));
        mixerBlueprint.addConnection(new Connection("c3", imageInput.getId(), imageTransform.getId()));
        mixerBlueprint.addConnection(new Connection("c4", audioFilter.getId(), audioOutput.getId()));
        mixerBlueprint.addConnection(new Connection("c5", aiProcessor.getId(), textOutput.getId()));
        mixerBlueprint.addConnection(new Connection("c6", imageTransform.getId(), imageOutput.getId()));
        
        System.out.println("Blueprint 'AI Mixer' criado com sucesso!");
        System.out.println("   - " + mixerBlueprint.getNodes().size() + " nós");
        System.out.println("   - " + mixerBlueprint.getConnections().size() + " conexões");
        
        // Mostrar estrutura do blueprint
        System.out.println("\nEstrutura do Blueprint:");
        mixerBlueprint.getNodes().forEach(node -> {
            System.out.println("   " + node.getName() + " (" + node.getType() + ")");
        });
        
        System.out.println("\nConexões:");
        mixerBlueprint.getConnections().forEach(conn -> {
            System.out.println("   " + conn.getFromNodeId() + " -> " + conn.getToNodeId());
        });
    }
} 