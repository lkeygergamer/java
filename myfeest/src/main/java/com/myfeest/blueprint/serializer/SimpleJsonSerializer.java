package com.myfeest.blueprint.serializer;

import com.myfeest.blueprint.core.*;
import com.myfeest.blueprint.nodes.*;
import java.util.*;

/**
 * Serializador JSON simples sem dependências externas
 */
public class SimpleJsonSerializer {
    
    /**
     * Serializa um blueprint para JSON
     */
    public String serialize(Blueprint blueprint) {
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        
        // Dados básicos
        json.append("  \"id\": \"").append(blueprint.getId()).append("\",\n");
        json.append("  \"name\": \"").append(blueprint.getName()).append("\",\n");
        json.append("  \"description\": \"").append(blueprint.getDescription()).append("\",\n");
        
        // Nós
        json.append("  \"nodes\": {\n");
        List<Node> nodes = blueprint.getNodes();
        for (int i = 0; i < nodes.size(); i++) {
            Node node = nodes.get(i);
            json.append("    \"").append(node.getId()).append("\": {\n");
            json.append("      \"id\": \"").append(node.getId()).append("\",\n");
            json.append("      \"name\": \"").append(node.getName()).append("\",\n");
            json.append("      \"type\": \"").append(node.getType()).append("\"");
            
            // Propriedades específicas
            if (node instanceof InputNode) {
                InputNode inputNode = (InputNode) node;
                json.append(",\n      \"value\": \"").append(inputNode.getValue()).append("\"");
            } else if (node instanceof TransformNode) {
                TransformNode transformNode = (TransformNode) node;
                json.append(",\n      \"operation\": \"").append(transformNode.getOperation()).append("\"");
            } else if (node instanceof FilterNode) {
                FilterNode filterNode = (FilterNode) node;
                json.append(",\n      \"condition\": \"").append(filterNode.getCondition()).append("\"");
            }
            
            json.append("\n    }");
            if (i < nodes.size() - 1) json.append(",");
            json.append("\n");
        }
        json.append("  },\n");
        
        // Conexões
        json.append("  \"connections\": {\n");
        List<Connection> connections = blueprint.getConnections();
        for (int i = 0; i < connections.size(); i++) {
            Connection conn = connections.get(i);
            json.append("    \"").append(conn.getId()).append("\": {\n");
            json.append("      \"id\": \"").append(conn.getId()).append("\",\n");
            json.append("      \"fromNodeId\": \"").append(conn.getFromNodeId()).append("\",\n");
            json.append("      \"toNodeId\": \"").append(conn.getToNodeId()).append("\",\n");
            json.append("      \"type\": \"").append(conn.getType().name()).append("\"\n");
            json.append("    }");
            if (i < connections.size() - 1) json.append(",");
            json.append("\n");
        }
        json.append("  }\n");
        
        json.append("}");
        return json.toString();
    }
} 