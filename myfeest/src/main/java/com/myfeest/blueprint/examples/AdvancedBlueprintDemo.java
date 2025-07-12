package com.myfeest.blueprint.examples;

import com.myfeest.blueprint.core.*;
import com.myfeest.blueprint.nodes.*;
import com.myfeest.blueprint.nodes.custom.*;
import com.myfeest.blueprint.nodes.advanced.*;
import com.myfeest.blueprint.engine.BlueprintEngine;
import com.myfeest.blueprint.serializer.SimpleJsonSerializer;

import java.util.*;

/**
 * Demonstração completa dos novos recursos do sistema de blueprint
 */
public class AdvancedBlueprintDemo {
    
    public static void main(String[] args) {
        System.out.println("=== DEMONSTRAÇÃO AVANÇADA DO SISTEMA DE BLUEPRINT ===\n");
        
        // Demo 1: Pipeline de dados com nós personalizados
        demoCustomNodesPipeline();
        
        // Demo 2: Transformações avançadas
        demoAdvancedTransformations();
        
        // Demo 3: Filtros complexos
        demoAdvancedFilters();
        
        // Demo 4: Sistema completo de processamento
        demoCompleteSystem();
    }
    
    /**
     * Demo 1: Pipeline usando nós personalizados (Database + API)
     */
    private static void demoCustomNodesPipeline() {
        System.out.println("1. PIPELINE COM NÓS PERSONALIZADOS");
        System.out.println("===================================");
        
        BlueprintEngine engine = new BlueprintEngine();
        
        // Criar nós personalizados
        DatabaseNode dbNode = new DatabaseNode("db_1", "Consulta de Usuários");
        dbNode.setOperation("SELECT");
        dbNode.setTable("users");
        dbNode.setParameter("limit", 10);
        
        APINode apiNode = new APINode("api_1", "Enriquecer Dados");
        apiNode.setMethod("POST");
        apiNode.setUrl("https://api.example.com/enrich");
        apiNode.setHeader("Content-Type", "application/json");
        apiNode.setBodyField("source", "database");
        
        TransformNode formatNode = new TransformNode("format_1", "Formatar Resultado");
        formatNode.setOperation("uppercase");
        
        OutputNode outputNode = new OutputNode("output_1", "Dados Processados");
        
        // Criar blueprint
        Blueprint pipeline = new Blueprint("custom_nodes_pipeline");
        pipeline.addNode(dbNode);
        pipeline.addNode(apiNode);
        pipeline.addNode(formatNode);
        pipeline.addNode(outputNode);
        
        // Conexões
        pipeline.addConnection(new Connection("c1", dbNode.getId(), apiNode.getId()));
        pipeline.addConnection(new Connection("c2", apiNode.getId(), formatNode.getId()));
        pipeline.addConnection(new Connection("c3", formatNode.getId(), outputNode.getId()));
        
        System.out.println("Pipeline criado: " + pipeline.getNodes().size() + " nós, " + 
                          pipeline.getConnections().size() + " conexões");
        
        // Executar
        try {
            Map<String, Object> result = engine.execute(pipeline);
            System.out.println("Resultado: " + result);
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
        System.out.println();
    }
    
    /**
     * Demo 2: Transformações avançadas
     */
    private static void demoAdvancedTransformations() {
        System.out.println("2. TRANSFORMAÇÕES AVANÇADAS");
        System.out.println("============================");
        
        BlueprintEngine engine = new BlueprintEngine();
        
        // Dados de exemplo
        List<Map<String, Object>> sampleData = Arrays.asList(
            createUser("João Silva", "joao@email.com", 25, "admin"),
            createUser("Maria Santos", "maria@email.com", 30, "user"),
            createUser("Pedro Costa", "pedro@email.com", 28, "user"),
            createUser("Ana Oliveira", "ana@email.com", 35, "admin")
        );
        
        // Nós de transformação avançada
        AdvancedTransformNode groupNode = new AdvancedTransformNode("group_1", "Agrupar por Role");
        groupNode.setOperation("group");
        groupNode.setParameter("key_field", "role");
        
        AdvancedTransformNode aggregateNode = new AdvancedTransformNode("agg_1", "Calcular Média de Idade");
        aggregateNode.setOperation("aggregate");
        aggregateNode.setParameter("aggregate_operation", "average");
        aggregateNode.setParameter("field", "age");
        
        AdvancedTransformNode formatNode = new AdvancedTransformNode("format_1", "Formatar Nomes");
        formatNode.setOperation("map");
        formatNode.setParameter("transform", "uppercase");
        
        OutputNode outputNode = new OutputNode("output_1", "Dados Transformados");
        
        // Criar blueprint
        Blueprint transformPipeline = new Blueprint("advanced_transformations");
        transformPipeline.addNode(groupNode);
        transformPipeline.addNode(aggregateNode);
        transformPipeline.addNode(formatNode);
        transformPipeline.addNode(outputNode);
        
        // Conexões
        transformPipeline.addConnection(new Connection("c1", groupNode.getId(), aggregateNode.getId()));
        transformPipeline.addConnection(new Connection("c2", aggregateNode.getId(), formatNode.getId()));
        transformPipeline.addConnection(new Connection("c3", formatNode.getId(), outputNode.getId()));
        
        System.out.println("Pipeline de transformação criado: " + transformPipeline.getNodes().size() + " nós");
        
        // Configurar dados de entrada
        ExecutionContext context = new ExecutionContext();
        context.setGlobalData("data", sampleData);
        
        // Configurar dados de entrada no engine
        engine.setGlobalVariable("data", sampleData);
        
        // Executar
        try {
            Map<String, Object> result = engine.execute(transformPipeline);
            System.out.println("Resultado da transformação: " + result);
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
        System.out.println();
    }
    
    /**
     * Demo 3: Filtros avançados
     */
    private static void demoAdvancedFilters() {
        System.out.println("3. FILTROS AVANÇADOS");
        System.out.println("=====================");
        
        BlueprintEngine engine = new BlueprintEngine();
        
        // Dados de exemplo
        List<Map<String, Object>> sampleData = Arrays.asList(
            createUser("João Silva", "joao@email.com", 25, "admin"),
            createUser("Maria Santos", "maria@email.com", 30, "user"),
            createUser("Pedro Costa", "pedro@email.com", 28, "user"),
            createUser("Ana Oliveira", "ana@email.com", 35, "admin"),
            createUser("Carlos Lima", "carlos@email.com", 22, "user")
        );
        
        // Filtros avançados
        AdvancedFilterNode ageFilter = new AdvancedFilterNode("age_filter", "Filtrar por Idade");
        ageFilter.setCondition("age > 25");
        
        AdvancedFilterNode roleFilter = new AdvancedFilterNode("role_filter", "Filtrar Admins");
        roleFilter.setCondition("role == 'admin'");
        
        AdvancedFilterNode emailFilter = new AdvancedFilterNode("email_filter", "Filtrar por Email");
        emailFilter.setCondition("contains '@email.com'");
        
        OutputNode outputNode = new OutputNode("output_1", "Dados Filtrados");
        
        // Criar blueprint
        Blueprint filterPipeline = new Blueprint("advanced_filters");
        filterPipeline.addNode(ageFilter);
        filterPipeline.addNode(roleFilter);
        filterPipeline.addNode(emailFilter);
        filterPipeline.addNode(outputNode);
        
        // Conexões
        filterPipeline.addConnection(new Connection("c1", ageFilter.getId(), roleFilter.getId()));
        filterPipeline.addConnection(new Connection("c2", roleFilter.getId(), emailFilter.getId()));
        filterPipeline.addConnection(new Connection("c3", emailFilter.getId(), outputNode.getId()));
        
        System.out.println("Pipeline de filtros criado: " + filterPipeline.getNodes().size() + " nós");
        
        // Configurar dados de entrada
        ExecutionContext context = new ExecutionContext();
        context.setGlobalData("data", sampleData);
        
        // Configurar dados de entrada no engine
        engine.setGlobalVariable("data", sampleData);
        
        // Executar
        try {
            Map<String, Object> result = engine.execute(filterPipeline);
            System.out.println("Resultado dos filtros: " + result);
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
        System.out.println();
    }
    
    /**
     * Demo 4: Sistema completo de processamento
     */
    private static void demoCompleteSystem() {
        System.out.println("4. SISTEMA COMPLETO DE PROCESSAMENTO");
        System.out.println("=====================================");
        
        BlueprintEngine engine = new BlueprintEngine();
        
        // Dados de exemplo
        List<Map<String, Object>> sampleData = Arrays.asList(
            createUser("João Silva", "joao@email.com", 25, "admin"),
            createUser("Maria Santos", "maria@email.com", 30, "user"),
            createUser("Pedro Costa", "pedro@email.com", 28, "user"),
            createUser("Ana Oliveira", "ana@email.com", 35, "admin"),
            createUser("Carlos Lima", "carlos@email.com", 22, "user")
        );
        
        // Pipeline completo: Filtro -> Transformação -> API -> Database -> Output
        AdvancedFilterNode filterNode = new AdvancedFilterNode("filter_1", "Filtrar Usuários Ativos");
        filterNode.setCondition("age >= 25");
        
        AdvancedTransformNode transformNode = new AdvancedTransformNode("transform_1", "Processar Dados");
        transformNode.setOperation("map");
        transformNode.setParameter("transform", "uppercase");
        
        APINode apiNode = new APINode("api_1", "Validar Dados");
        apiNode.setMethod("POST");
        apiNode.setUrl("https://api.example.com/validate");
        
        DatabaseNode dbNode = new DatabaseNode("db_1", "Salvar Resultados");
        dbNode.setOperation("INSERT");
        dbNode.setTable("processed_users");
        
        OutputNode outputNode = new OutputNode("output_1", "Resultado Final");
        
        // Criar blueprint
        Blueprint completeSystem = new Blueprint("complete_processing_system");
        completeSystem.addNode(filterNode);
        completeSystem.addNode(transformNode);
        completeSystem.addNode(apiNode);
        completeSystem.addNode(dbNode);
        completeSystem.addNode(outputNode);
        
        // Conexões
        completeSystem.addConnection(new Connection("c1", filterNode.getId(), transformNode.getId()));
        completeSystem.addConnection(new Connection("c2", transformNode.getId(), apiNode.getId()));
        completeSystem.addConnection(new Connection("c3", apiNode.getId(), dbNode.getId()));
        completeSystem.addConnection(new Connection("c4", dbNode.getId(), outputNode.getId()));
        
        System.out.println("Sistema completo criado: " + completeSystem.getNodes().size() + " nós, " + 
                          completeSystem.getConnections().size() + " conexões");
        
        // Configurar dados de entrada
        ExecutionContext context = new ExecutionContext();
        context.setGlobalData("data", sampleData);
        
        // Configurar dados de entrada no engine
        engine.setGlobalVariable("data", sampleData);
        
        // Executar
        try {
            Map<String, Object> result = engine.execute(completeSystem);
            System.out.println("Resultado do sistema completo: " + result);
            
            // Mostrar JSON do blueprint
            SimpleJsonSerializer serializer = new SimpleJsonSerializer();
            String json = serializer.serialize(completeSystem);
            System.out.println("\nJSON do Blueprint:");
            System.out.println(json);
            
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
        System.out.println();
    }
    
    /**
     * Método auxiliar para criar dados de usuário de exemplo
     */
    private static Map<String, Object> createUser(String name, String email, int age, String role) {
        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("email", email);
        user.put("age", age);
        user.put("role", role);
        return user;
    }
} 