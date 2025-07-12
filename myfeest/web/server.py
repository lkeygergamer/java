#!/usr/bin/env python3
"""
Servidor web simples para o Blueprint Editor
"""

import http.server
import socketserver
import os
import webbrowser
from urllib.parse import urlparse

class BlueprintHandler(http.server.SimpleHTTPRequestHandler):
    def end_headers(self):
        # Adicionar headers CORS para permitir requisições
        self.send_header('Access-Control-Allow-Origin', '*')
        self.send_header('Access-Control-Allow-Methods', 'GET, POST, OPTIONS')
        self.send_header('Access-Control-Allow-Headers', 'Content-Type')
        super().end_headers()

    def do_GET(self):
        # Roteamento para diferentes páginas
        if self.path == '/':
            self.path = '/unreal-editor.html'
        elif self.path == '/editor':
            self.path = '/unreal-editor.html'
        elif self.path == '/basic':
            self.path = '/index.html'
        
        return super().do_GET()

def start_server(port=8080):
    """Inicia o servidor web"""
    os.chdir(os.path.dirname(os.path.abspath(__file__)))
    
    with socketserver.TCPServer(("", port), BlueprintHandler) as httpd:
        print(f"🚀 Servidor iniciado em http://localhost:{port}")
        print(f"📱 Editor Unreal Style: http://localhost:{port}/editor")
        print(f"📱 Editor Básico: http://localhost:{port}/basic")
        print("Pressione Ctrl+C para parar o servidor")
        
        # Abrir navegador automaticamente
        try:
            webbrowser.open(f'http://localhost:{port}/editor')
        except:
            pass
        
        try:
            httpd.serve_forever()
        except KeyboardInterrupt:
            print("\n👋 Servidor parado")

if __name__ == "__main__":
    start_server() 