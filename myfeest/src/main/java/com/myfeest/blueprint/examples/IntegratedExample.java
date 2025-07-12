package com.myfeest.blueprint.examples;

import com.myfeest.blueprint.api.BlueprintAPI;
import com.myfeest.blueprint.core.*;
import com.myfeest.blueprint.nodes.*;

/**
 * Exemplo de integração Frontend-Backend
 * Demonstra como o editor web se comunica com a API Java
 */
public class IntegratedExample {
    
    public static void main(String[] args) {
        System.out.println("🎯 Exemplo de Integração Frontend-Backend");
        System.out.println("==========================================");
        
        BlueprintAPI api = new BlueprintAPI();
        
        // Simula dados enviados pelo frontend
        String frontendBlueprintData = createFrontendBlueprintData();
        
        System.out.println("\n📤 Dados recebidos do Frontend:");
        System.out.println(frontendBlueprintData);
        
        // Processa na API
        System.out.println("\n🔄 Processando na API Java...");
        String result = api.executeBlueprintFromFrontend(frontendBlueprintData);
        
        System.out.println("\n📥 Resposta da API:");
        System.out.println(result);
        
        // Testa criação de blueprint
        System.out.println("\n💾 Testando criação de blueprint...");
        String createResult = api.createBlueprintFromFrontend(frontendBlueprintData);
        System.out.println(createResult);
        
        // Lista blueprints
        System.out.println("\n📋 Listando blueprints...");
        String listResult = api.listBlueprints();
        System.out.println(listResult);
        
        // Estatísticas
        System.out.println("\n📊 Estatísticas da API:");
        String statsResult = api.getStats();
        System.out.println(statsResult);
        
        System.out.println("\n✅ Exemplo concluído!");
        System.out.println("\n🌐 Para testar a integração completa:");
        System.out.println("1. Execute: start-java-server.bat");
        System.out.println("2. Acesse: http://localhost:8081/editor");
        System.out.println("3. Crie blueprints visualmente");
        System.out.println("4. Execute no backend Java!");
    }
    
    /**
     * Cria dados simulados do frontend
     */
    private static String createFrontendBlueprintData() {
        return """
        {
            "name": "Blueprint de Teste",
            "description": "Blueprint criado no editor visual",
            "nodes": {
                "node_1": {
                    "id": "node_1",
                    "name": "Input 1",
                    "type": "input",
                    "properties": {
                        "value": "Hello World from Frontend!"
                    }
                },
                "node_2": {
                    "id": "node_2",
                    "name": "Transform 1",
                    "type": "transform",
                    "properties": {
                        "operation": "uppercase"
                    }
                },
                "node_3": {
                    "id": "node_3",
                    "name": "AI 1",
                    "type": "ai",
                    "properties": {
                        "model": "gpt-4",
                        "task": "text-analysis"
                    }
                },
                "node_4": {
                    "id": "node_4",
                    "name": "Output 1",
                    "type": "output",
                    "properties": {}
                }
            },
            "connections": {
                "conn_1": {
                    "id": "conn_1",
                    "fromNodeId": "node_1",
                    "toNodeId": "node_2"
                },
                "conn_2": {
                    "id": "conn_2",
                    "fromNodeId": "node_2",
                    "toNodeId": "node_3"
                },
                "conn_3": {
                    "id": "conn_3",
                    "fromNodeId": "node_3",
                    "toNodeId": "node_4"
                }
            }
        }
        """;
    }
    
    /**
     * Testa comunicação com diferentes tipos de nós
     */
    public static void testNodeTypes() {
        System.out.println("\n🧪 Testando diferentes tipos de nós...");
        
        BlueprintAPI api = new BlueprintAPI();
        
        // Teste com nós de texto
        String textBlueprint = """
        {
            "name": "Blueprint de Texto",
            "nodes": {
                "text_input": {
                    "id": "text_input",
                    "name": "Text Input",
                    "type": "text_input",
                    "properties": {
                        "text": "Texto de entrada"
                    }
                },
                "text_output": {
                    "id": "text_output",
                    "name": "Text Output",
                    "type": "text_output",
                    "properties": {}
                }
            },
            "connections": {
                "conn": {
                    "id": "conn",
                    "fromNodeId": "text_input",
                    "toNodeId": "text_output"
                }
            }
        }
        """;
        
        String result = api.executeBlueprintFromFrontend(textBlueprint);
        System.out.println("Resultado do blueprint de texto:");
        System.out.println(result);
        
        // Teste com nós avançados
        String advancedBlueprint = """
        {
            "name": "Blueprint Avançado",
            "nodes": {
                "input": {
                    "id": "input",
                    "name": "Input",
                    "type": "input",
                    "properties": {
                        "value": [1, 2, 3, 4, 5]
                    }
                },
                "advanced_transform": {
                    "id": "advanced_transform",
                    "name": "Advanced Transform",
                    "type": "advanced_transform",
                    "properties": {
                        "operation": "map"
                    }
                },
                "advanced_filter": {
                    "id": "advanced_filter",
                    "name": "Advanced Filter",
                    "type": "advanced_filter",
                    "properties": {
                        "condition": "even"
                    }
                },
                "output": {
                    "id": "output",
                    "name": "Output",
                    "type": "output",
                    "properties": {}
                }
            },
            "connections": {
                "conn1": {
                    "id": "conn1",
                    "fromNodeId": "input",
                    "toNodeId": "advanced_transform"
                },
                "conn2": {
                    "id": "conn2",
                    "fromNodeId": "advanced_transform",
                    "toNodeId": "advanced_filter"
                },
                "conn3": {
                    "id": "conn3",
                    "fromNodeId": "advanced_filter",
                    "toNodeId": "output"
                }
            }
        }
        """;
        
        result = api.executeBlueprintFromFrontend(advancedBlueprint);
        System.out.println("\nResultado do blueprint avançado:");
        System.out.println(result);
    }
} 