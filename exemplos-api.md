# 📚 Exemplos de Uso da API

## 🔐 Autenticação

### 1. Registrar um novo usuário

```bash
curl -X POST http://localhost:8080/api/auth/registro \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva",
    "email": "joao@exemplo.com",
    "senha": "123456"
  }'
```

**Resposta:**
```json
{
  "id": 1,
  "nome": "João Silva",
  "email": "joao@exemplo.com",
  "role": "USER",
  "dataCriacao": "2024-01-15T10:30:00",
  "ativo": true
}
```

### 2. Fazer login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "joao@exemplo.com",
    "senha": "123456"
  }'
```

**Resposta:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tipo": "Bearer",
  "expiracao": 1705312200000
}
```

### 3. Obter perfil do usuário logado

```bash
curl -X GET http://localhost:8080/api/auth/perfil \
  -H "Authorization: Bearer SEU_TOKEN_AQUI"
```

## 👥 Gerenciamento de Usuários

### 1. Listar todos os usuários (ADMIN)

```bash
curl -X GET http://localhost:8080/api/usuarios \
  -H "Authorization: Bearer SEU_TOKEN_ADMIN_AQUI"
```

### 2. Listar usuários ativos (ADMIN)

```bash
curl -X GET http://localhost:8080/api/usuarios/ativos \
  -H "Authorization: Bearer SEU_TOKEN_ADMIN_AQUI"
```

### 3. Buscar usuário por ID

```bash
curl -X GET http://localhost:8080/api/usuarios/1 \
  -H "Authorization: Bearer SEU_TOKEN_AQUI"
```

### 4. Atualizar usuário

```bash
curl -X PUT http://localhost:8080/api/usuarios/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer SEU_TOKEN_AQUI" \
  -d '{
    "nome": "João Silva Atualizado",
    "email": "joao.novo@exemplo.com",
    "senha": "nova123456",
    "role": "ADMIN",
    "ativo": true
  }'
```

### 5. Deletar usuário (ADMIN)

```bash
curl -X DELETE http://localhost:8080/api/usuarios/1 \
  -H "Authorization: Bearer SEU_TOKEN_ADMIN_AQUI"
```

### 6. Contar usuários ativos (ADMIN)

```bash
curl -X GET http://localhost:8080/api/usuarios/contagem \
  -H "Authorization: Bearer SEU_TOKEN_ADMIN_AQUI"
```

## 🐍 Exemplos em Python

```python
import requests
import json

# Configurações
BASE_URL = "http://localhost:8080/api"
headers = {"Content-Type": "application/json"}

# 1. Registrar usuário
def registrar_usuario(nome, email, senha):
    data = {
        "nome": nome,
        "email": email,
        "senha": senha
    }
    response = requests.post(f"{BASE_URL}/auth/registro", 
                           headers=headers, 
                           data=json.dumps(data))
    return response.json()

# 2. Fazer login
def fazer_login(email, senha):
    data = {
        "email": email,
        "senha": senha
    }
    response = requests.post(f"{BASE_URL}/auth/login", 
                           headers=headers, 
                           data=json.dumps(data))
    return response.json()

# 3. Listar usuários (com token)
def listar_usuarios(token):
    headers_with_token = headers.copy()
    headers_with_token["Authorization"] = f"Bearer {token}"
    response = requests.get(f"{BASE_URL}/usuarios", 
                          headers=headers_with_token)
    return response.json()

# Exemplo de uso
if __name__ == "__main__":
    # Registrar usuário
    usuario = registrar_usuario("Maria Silva", "maria@exemplo.com", "123456")
    print("Usuário registrado:", usuario)
    
    # Fazer login
    login = fazer_login("maria@exemplo.com", "123456")
    token = login["token"]
    print("Token obtido:", token)
    
    # Listar usuários
    usuarios = listar_usuarios(token)
    print("Usuários:", usuarios)
```

## 🔧 Exemplos em JavaScript/Node.js

```javascript
const axios = require('axios');

const BASE_URL = 'http://localhost:8080/api';
const headers = { 'Content-Type': 'application/json' };

// 1. Registrar usuário
async function registrarUsuario(nome, email, senha) {
    try {
        const response = await axios.post(`${BASE_URL}/auth/registro`, {
            nome,
            email,
            senha
        }, { headers });
        return response.data;
    } catch (error) {
        console.error('Erro ao registrar:', error.response.data);
    }
}

// 2. Fazer login
async function fazerLogin(email, senha) {
    try {
        const response = await axios.post(`${BASE_URL}/auth/login`, {
            email,
            senha
        }, { headers });
        return response.data;
    } catch (error) {
        console.error('Erro ao fazer login:', error.response.data);
    }
}

// 3. Listar usuários
async function listarUsuarios(token) {
    try {
        const response = await axios.get(`${BASE_URL}/usuarios`, {
            headers: { ...headers, Authorization: `Bearer ${token}` }
        });
        return response.data;
    } catch (error) {
        console.error('Erro ao listar usuários:', error.response.data);
    }
}

// Exemplo de uso
async function exemplo() {
    // Registrar usuário
    const usuario = await registrarUsuario('Pedro Santos', 'pedro@exemplo.com', '123456');
    console.log('Usuário registrado:', usuario);
    
    // Fazer login
    const login = await fazerLogin('pedro@exemplo.com', '123456');
    const token = login.token;
    console.log('Token obtido:', token);
    
    // Listar usuários
    const usuarios = await listarUsuarios(token);
    console.log('Usuários:', usuarios);
}

exemplo();
```

## 📱 Exemplos em Postman

### Collection JSON para importar no Postman:

```json
{
  "info": {
    "name": "Projeto Java Foda API",
    "description": "API completa com autenticação JWT"
  },
  "item": [
    {
      "name": "Auth",
      "item": [
        {
          "name": "Registrar Usuário",
          "request": {
            "method": "POST",
            "header": [
              {
                "key": "Content-Type",
                "value": "application/json"
              }
            ],
            "url": "http://localhost:8080/api/auth/registro",
            "body": {
              "mode": "raw",
              "raw": "{\n  \"nome\": \"João Silva\",\n  \"email\": \"joao@exemplo.com\",\n  \"senha\": \"123456\"\n}"
            }
          }
        },
        {
          "name": "Login",
          "request": {
            "method": "POST",
            "header": [
              {
                "key": "Content-Type",
                "value": "application/json"
              }
            ],
            "url": "http://localhost:8080/api/auth/login",
            "body": {
              "mode": "raw",
              "raw": "{\n  \"email\": \"joao@exemplo.com\",\n  \"senha\": \"123456\"\n}"
            }
          }
        }
      ]
    },
    {
      "name": "Usuários",
      "item": [
        {
          "name": "Listar Usuários",
          "request": {
            "method": "GET",
            "header": [
              {
                "key": "Authorization",
                "value": "Bearer {{token}}"
              }
            ],
            "url": "http://localhost:8080/api/usuarios"
          }
        }
      ]
    }
  ]
}
```

## 🚀 Testando com Swagger UI

1. Acesse: http://localhost:8080/api/swagger-ui/
2. Clique em "Authorize" e insira seu token JWT
3. Teste os endpoints diretamente na interface

## 📊 Monitoramento

### Health Check
```bash
curl -X GET http://localhost:8080/api/actuator/health
```

### Info
```bash
curl -X GET http://localhost:8080/api/actuator/info
```

### Metrics
```bash
curl -X GET http://localhost:8080/api/actuator/metrics
``` 