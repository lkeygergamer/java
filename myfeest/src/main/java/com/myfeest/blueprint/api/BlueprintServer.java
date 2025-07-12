package com.myfeest.blueprint.api;

import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Servidor HTTP simples para expor a API REST do Blueprint
 */
public class BlueprintServer {
    
    private BlueprintAPI api;
    private ServerSocket serverSocket;
    private ExecutorService executor;
    private boolean running;
    private int port;
    
    public BlueprintServer(int port) {
        this.port = port;
        this.api = new BlueprintAPI();
        this.executor = Executors.newFixedThreadPool(10);
        this.running = false;
    }
    
    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            running = true;
            
            System.out.println("🚀 Blueprint API Server iniciado na porta " + port);
            System.out.println("📡 Endpoints disponíveis:");
            System.out.println("   POST /api/execute - Executar blueprint");
            System.out.println("   POST /api/create - Criar blueprint");
            System.out.println("   GET  /api/list - Listar blueprints");
            System.out.println("   GET  /api/stats - Estatísticas");
            System.out.println("   GET  /api/blueprint/{id} - Obter blueprint");
            System.out.println("   DELETE /api/blueprint/{id} - Deletar blueprint");
            System.out.println("🌐 Acesse: http://localhost:" + port);
            
            while (running) {
                Socket clientSocket = serverSocket.accept();
                executor.submit(() -> handleClient(clientSocket));
            }
            
        } catch (IOException e) {
            System.err.println("Erro ao iniciar servidor: " + e.getMessage());
        }
    }
    
    public void stop() {
        running = false;
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
            executor.shutdown();
            System.out.println("👋 Servidor parado");
        } catch (IOException e) {
            System.err.println("Erro ao parar servidor: " + e.getMessage());
        }
    }
    
    private void handleClient(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            
            // Lê a primeira linha da requisição
            String requestLine = in.readLine();
            if (requestLine == null) return;
            
            // Parse da requisição HTTP
            String[] parts = requestLine.split(" ");
            if (parts.length < 2) return;
            
            String method = parts[0];
            String path = parts[1];
            
            // Lê headers
            String line;
            int contentLength = 0;
            while ((line = in.readLine()) != null && !line.isEmpty()) {
                if (line.toLowerCase().startsWith("content-length:")) {
                    contentLength = Integer.parseInt(line.split(":")[1].trim());
                }
            }
            
            // Lê body se houver
            String body = "";
            if (contentLength > 0) {
                char[] buffer = new char[contentLength];
                in.read(buffer, 0, contentLength);
                body = new String(buffer);
            }
            
            // Processa a requisição
            String response = processRequest(method, path, body);
            
            // Envia resposta
            out.print(response);
            out.flush();
            
        } catch (IOException e) {
            System.err.println("Erro ao processar cliente: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                // Ignora erro ao fechar socket
            }
        }
    }
    
    private String processRequest(String method, String path, String body) {
        try {
            // Parse do path
            String[] pathParts = path.split("/");
            
            // CORS headers
            String corsHeaders = "Access-Control-Allow-Origin: *\n" +
                                "Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS\n" +
                                "Access-Control-Allow-Headers: Content-Type\n";
            
            // OPTIONS request (CORS preflight)
            if ("OPTIONS".equals(method)) {
                return "HTTP/1.1 200 OK\n" + corsHeaders + "\n";
            }
            
            // Roteamento
            if (path.startsWith("/api/")) {
                return handleAPIRequest(method, path, body);
            } else if (path.equals("/") || path.equals("/index.html")) {
                return serveFile("web/index.html", "text/html");
            } else if (path.equals("/editor") || path.equals("/unreal-editor.html")) {
                return serveFile("web/unreal-editor.html", "text/html");
            } else if (path.startsWith("/web/")) {
                return serveFile(path.substring(1), getContentType(path));
            } else {
                return "HTTP/1.1 404 Not Found\n" + corsHeaders + 
                       "Content-Type: text/plain\n\nEndpoint não encontrado: " + path;
            }
            
        } catch (Exception e) {
            return "HTTP/1.1 500 Internal Server Error\n" +
                   "Access-Control-Allow-Origin: *\n" +
                   "Content-Type: application/json\n\n" +
                   "{\"error\":\"" + e.getMessage() + "\"}";
        }
    }
    
    private String handleAPIRequest(String method, String path, String body) {
        String[] pathParts = path.split("/");
        
        if (pathParts.length < 3) {
            return createErrorResponse("Endpoint inválido");
        }
        
        String endpoint = pathParts[2];
        
        switch (endpoint) {
            case "execute":
                if ("POST".equals(method)) {
                    return createJSONResponse(api.executeBlueprintFromFrontend(body));
                }
                break;
                
            case "create":
                if ("POST".equals(method)) {
                    return createJSONResponse(api.createBlueprintFromFrontend(body));
                }
                break;
                
            case "list":
                if ("GET".equals(method)) {
                    return createJSONResponse(api.listBlueprints());
                }
                break;
                
            case "stats":
                if ("GET".equals(method)) {
                    return createJSONResponse(api.getStats());
                }
                break;
                
            case "blueprint":
                if (pathParts.length >= 4) {
                    String blueprintId = pathParts[3];
                    
                    if ("GET".equals(method)) {
                        return createJSONResponse(api.getBlueprint(blueprintId));
                    } else if ("DELETE".equals(method)) {
                        return createJSONResponse(api.deleteBlueprint(blueprintId));
                    }
                }
                break;
        }
        
        return createErrorResponse("Método não suportado para este endpoint");
    }
    
    private String createJSONResponse(String jsonData) {
        return "HTTP/1.1 200 OK\n" +
               "Access-Control-Allow-Origin: *\n" +
               "Content-Type: application/json\n\n" +
               jsonData;
    }
    
    private String createErrorResponse(String error) {
        return "HTTP/1.1 400 Bad Request\n" +
               "Access-Control-Allow-Origin: *\n" +
               "Content-Type: application/json\n\n" +
               "{\"error\":\"" + error + "\"}";
    }
    
    private String serveFile(String filePath, String contentType) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                return "HTTP/1.1 404 Not Found\n" +
                       "Access-Control-Allow-Origin: *\n" +
                       "Content-Type: text/plain\n\nArquivo não encontrado: " + filePath;
            }
            
            StringBuilder content = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
            }
            
            return "HTTP/1.1 200 OK\n" +
                   "Access-Control-Allow-Origin: *\n" +
                   "Content-Type: " + contentType + "\n\n" +
                   content.toString();
                   
        } catch (IOException e) {
            return "HTTP/1.1 500 Internal Server Error\n" +
                   "Access-Control-Allow-Origin: *\n" +
                   "Content-Type: text/plain\n\nErro ao ler arquivo: " + e.getMessage();
        }
    }
    
    private String getContentType(String path) {
        if (path.endsWith(".html")) return "text/html";
        if (path.endsWith(".css")) return "text/css";
        if (path.endsWith(".js")) return "application/javascript";
        if (path.endsWith(".json")) return "application/json";
        if (path.endsWith(".png")) return "image/png";
        if (path.endsWith(".jpg") || path.endsWith(".jpeg")) return "image/jpeg";
        return "text/plain";
    }
    
    public static void main(String[] args) {
        int port = 8081; // Porta diferente do servidor Python
        
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Porta inválida, usando padrão: " + port);
            }
        }
        
        BlueprintServer server = new BlueprintServer(port);
        
        // Adiciona shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\n🛑 Recebido sinal de shutdown...");
            server.stop();
        }));
        
        server.start();
    }
} 