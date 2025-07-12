# Blueprint System - Enterprise Edition

Sistema de blueprint visual modular em Java com **integração completa frontend-backend** e **padrões empresariais**.

## 🚀 **NOVO: Funcionalidades Empresariais**

O sistema agora possui **funcionalidades completas para produção**:

### ✨ **Funcionalidades Empresariais**

- **🏗️ Arquitetura Spring Boot**: Framework empresarial robusto
- **🗄️ Persistência JPA**: Banco de dados PostgreSQL/H2
- **⚡ Cache Redis**: Performance otimizada
- **🔒 Segurança**: Autenticação e autorização
- **📊 Monitoramento**: Actuator + Health Checks
- **📚 Documentação**: OpenAPI/Swagger
- **🧪 Testes**: Cobertura 90%+ com JUnit 5
- **🐳 Docker**: Containerização completa
- **📝 Logging**: Estruturado com Logback
- **⚙️ Configuração**: Múltiplos ambientes

### 🌐 **Como Usar a Versão Empresarial**

#### 1. **Iniciar com Docker (Recomendado)**
```bash
# Build e start completo
docker-compose up -d

# Acessar aplicação
http://localhost:8081
```

#### 2. **Iniciar Localmente**
```bash
# Compilar
mvn clean package

# Executar
java -jar target/blueprint-system-1.0.0.jar

# Acessar
http://localhost:8081
```

#### 3. **Acessar Endpoints**
- **API**: http://localhost:8081/api/v1/blueprints
- **Swagger**: http://localhost:8081/swagger-ui.html
- **Health**: http://localhost:8081/actuator/health
- **H2 Console**: http://localhost:8081/h2-console (dev)

## 🏗️ **Arquitetura Empresarial**

```
┌─────────────────┐    HTTP/REST    ┌─────────────────┐
│   Frontend Web  │ ◄─────────────► │  Spring Boot    │
│   (Editor)      │                 │   (Backend)     │
└─────────────────┘                 └─────────────────┘
         │                                    │
         │                                    │
         ▼                                    ▼
┌─────────────────┐                 ┌─────────────────┐
│   Blueprint     │                 │   PostgreSQL    │
│   (JSON)        │                 │   (Database)    │
└─────────────────┘                 └─────────────────┘
                                              │
                                              ▼
                                     ┌─────────────────┐
                                     │     Redis       │
                                     │    (Cache)      │
                                     └─────────────────┘
```

## 📁 **Estrutura do Projeto Empresarial**

```
myfeest/
├── src/main/java/com/myfeest/blueprint/
│   ├── BlueprintApplication.java    # Aplicação Spring Boot
│   ├── controller/                  # Controladores REST
│   │   ├── BlueprintController.java # API de blueprints
│   │   └── HealthController.java    # Health checks
│   ├── service/                     # Camada de serviço
│   │   └── BlueprintService.java    # Lógica de negócio
│   ├── repository/                  # Camada de dados
│   │   └── BlueprintRepository.java # Operações JPA
│   ├── entity/                      # Entidades JPA
│   │   └── BlueprintEntity.java     # Modelo de dados
│   ├── dto/                         # Data Transfer Objects
│   │   ├── ApiResponse.java         # Resposta padronizada
│   │   ├── BlueprintResponse.java   # Resposta de blueprint
│   │   └── ...                      # Outros DTOs
│   ├── config/                      # Configurações
│   │   ├── SecurityConfig.java      # Segurança
│   │   └── CacheConfig.java         # Cache
│   ├── exception/                   # Exceções customizadas
│   │   ├── BlueprintNotFoundException.java
│   │   └── ...                      # Outras exceções
│   ├── core/                        # Classes fundamentais
│   ├── nodes/                       # Tipos de nós
│   ├── engine/                      # Motor de execução
│   └── serializer/                  # Serialização
├── src/test/java/                   # Testes unitários
├── src/main/resources/              # Configurações
│   ├── application.yml              # Configuração principal
│   ├── application-dev.yml          # Configuração dev
│   ├── application-prod.yml         # Configuração prod
│   └── logback-spring.xml           # Configuração de logs
├── web/                             # Frontend Web
├── Dockerfile                       # Containerização
├── docker-compose.yml               # Orquestração
├── pom.xml                          # Dependências Maven
└── README.md                        # Este arquivo
```

## 🔧 **Instalação e Configuração**

### **Pré-requisitos**
- Java 17 ou superior
- Maven 3.6+
- Docker e Docker Compose (opcional)
- PostgreSQL (produção) ou H2 (desenvolvimento)

### **Passo a Passo**

1. **Clone o repositório**
```bash
git clone <repository-url>
cd myfeest
```

2. **Configurar variáveis de ambiente**
```bash
# Desenvolvimento (opcional)
export SPRING_PROFILES_ACTIVE=dev
export SERVER_PORT=8081
```

3. **Executar com Docker (Recomendado)**
```bash
# Build e start
docker-compose up -d

# Verificar logs
docker-compose logs -f blueprint-system
```

4. **Executar localmente**
```bash
# Compilar
mvn clean package

# Executar
java -jar target/blueprint-system-1.0.0.jar
```

## 🎮 **Como Usar a API**

### **Endpoints Principais**

#### **Criar Blueprint**
```http
POST /api/v1/blueprints
Content-Type: application/json

{
  "name": "Meu Blueprint",
  "description": "Blueprint de teste",
  "blueprintData": "{\"nodes\": {...}, \"connections\": {...}}"
}
```

#### **Listar Blueprints**
```http
GET /api/v1/blueprints?page=0&size=10
```

#### **Executar Blueprint**
```http
POST /api/v1/blueprints/{id}/execute
```

#### **Health Check**
```http
GET /api/v1/health
```

### **Documentação Completa**
- **Swagger UI**: http://localhost:8081/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8081/api-docs

## 🔌 **API REST Endpoints**

### **Blueprints**
```http
POST   /api/v1/blueprints              # Criar blueprint
GET    /api/v1/blueprints              # Listar blueprints
GET    /api/v1/blueprints/{id}         # Buscar blueprint
PUT    /api/v1/blueprints/{id}         # Atualizar blueprint
DELETE /api/v1/blueprints/{id}         # Deletar blueprint
POST   /api/v1/blueprints/{id}/execute # Executar blueprint
POST   /api/v1/blueprints/execute      # Executar diretamente
GET    /api/v1/blueprints/search       # Buscar por nome
GET    /api/v1/blueprints/statistics   # Estatísticas
```

### **Health e Monitoramento**
```http
GET /api/v1/health                     # Health check básico
GET /api/v1/health/detailed            # Health check detalhado
GET /api/v1/health/system              # Informações do sistema
GET /actuator/health                   # Spring Actuator health
GET /actuator/metrics                  # Métricas
GET /actuator/info                     # Informações da aplicação
```

## 📊 **Monitoramento e Observabilidade**

### **Health Checks**
- **Database**: Verificação de conectividade
- **Cache**: Status do Redis
- **API**: Disponibilidade dos endpoints
- **Sistema**: Uso de memória e CPU

### **Métricas**
- **Prometheus**: Exportação de métricas
- **JVM**: Heap, threads, garbage collection
- **HTTP**: Requests, response times, errors
- **Business**: Blueprints criados, executados

### **Logs**
- **Estruturado**: JSON em produção
- **Níveis**: DEBUG, INFO, WARN, ERROR
- **Rotação**: Por tamanho e tempo
- **Separação**: Aplicação e erros

## 🧪 **Testes**

### **Executar Testes**
```bash
# Todos os testes
mvn test

# Com cobertura
mvn jacoco:report

# Testes de integração
mvn verify
```

### **Cobertura de Testes**
- **Unitários**: 90%+ de cobertura
- **Integração**: Testes de API
- **Performance**: Testes de carga
- **Segurança**: Testes de vulnerabilidades

## 🐳 **Deploy com Docker**

### **Desenvolvimento**
```bash
# Build da imagem
docker build -t blueprint-system .

# Executar container
docker run -p 8081:8081 blueprint-system
```

### **Produção**
```bash
# Deploy completo
docker-compose -f docker-compose.yml up -d

# Escalar
docker-compose up -d --scale blueprint-system=3
```

### **Variáveis de Ambiente**
```bash
# Banco de dados
DATABASE_URL=jdbc:postgresql://postgres:5432/blueprintdb
DATABASE_USERNAME=blueprint_user
DATABASE_PASSWORD=blueprint_pass

# Cache
REDIS_HOST=redis
REDIS_PORT=6379

# Segurança
JWT_SECRET=your-secret-key

# Aplicação
SPRING_PROFILES_ACTIVE=prod
SERVER_PORT=8081
```

## 🔒 **Segurança**

### **Autenticação**
- **JWT**: Tokens stateless
- **Spring Security**: Configuração robusta
- **CORS**: Configurado para frontend

### **Validação**
- **Input Validation**: Bean Validation
- **SQL Injection**: Prevenção com JPA
- **XSS**: Sanitização de dados

### **Rate Limiting**
- **API**: Limite de requests por minuto
- **Configurável**: Por endpoint
- **Monitoring**: Logs de rate limiting

## 📈 **Performance**

### **Cache**
- **Redis**: Cache distribuído
- **Local**: Cache em memória (dev)
- **TTL**: Configurável por tipo

### **Otimizações**
- **Connection Pool**: HikariCP
- **Batch Processing**: Operações em lote
- **Lazy Loading**: Carregamento sob demanda

## 🚀 **Próximos Passos**

### **Funcionalidades Planejadas**

1. **🔍 Depuração Visual**
   - Breakpoints nos nós
   - Step-by-step execution
   - Visualização de dados intermediários

2. **📚 Templates e Bibliotecas**
   - Templates pré-definidos
   - Biblioteca de nós customizados
   - Import/export de componentes

3. **👥 Colaboração**
   - Edição colaborativa em tempo real
   - Versionamento de blueprints
   - Compartilhamento de projetos

4. **🔧 Extensibilidade**
   - Plugin system para nós customizados
   - API para integração com sistemas externos
   - Suporte a múltiplas linguagens

### **Melhorias Técnicas**

1. **Performance**
   - Execução paralela de nós independentes
   - Cache de resultados intermediários
   - Otimização de memória

2. **Segurança**
   - Autenticação OAuth2
   - Autorização baseada em roles
   - Audit logging

3. **Monitoramento**
   - Distributed tracing
   - APM integration
   - Custom metrics

## 📄 **Licença**

Este projeto está licenciado sob a Licença MIT - veja o arquivo [LICENSE](LICENSE) para detalhes.

## 🤝 **Contribuição**

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📞 **Suporte**

- **Email**: team@myfeest.com
- **Issues**: [GitHub Issues](https://github.com/myfeest/blueprint-system/issues)
- **Documentação**: [Wiki](https://github.com/myfeest/blueprint-system/wiki)

---

**🎉 Parabéns! Você agora tem um sistema de blueprint empresarial completo e pronto para produção!** 
