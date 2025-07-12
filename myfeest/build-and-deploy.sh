#!/bin/bash

# Script de Build e Deploy para Blueprint System
# Autor: MyFeest Team
# Versão: 1.0.0

set -e  # Para o script se houver erro

# Cores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Função para log
log() {
    echo -e "${GREEN}[$(date +'%Y-%m-%d %H:%M:%S')] $1${NC}"
}

warn() {
    echo -e "${YELLOW}[$(date +'%Y-%m-%d %H:%M:%S')] WARNING: $1${NC}"
}

error() {
    echo -e "${RED}[$(date +'%Y-%m-%d %H:%M:%S')] ERROR: $1${NC}"
}

info() {
    echo -e "${BLUE}[$(date +'%Y-%m-%d %H:%M:%S')] INFO: $1${NC}"
}

# Verificar se o Maven está instalado
check_maven() {
    if ! command -v mvn &> /dev/null; then
        error "Maven não está instalado. Por favor, instale o Maven primeiro."
        exit 1
    fi
    log "Maven encontrado: $(mvn --version | head -n 1)"
}

# Verificar se o Java está instalado
check_java() {
    if ! command -v java &> /dev/null; then
        error "Java não está instalado. Por favor, instale o Java 17+ primeiro."
        exit 1
    fi
    
    JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
    if [ "$JAVA_VERSION" -lt 17 ]; then
        error "Java 17+ é necessário. Versão atual: $JAVA_VERSION"
        exit 1
    fi
    log "Java encontrado: $(java -version 2>&1 | head -n 1)"
}

# Verificar se o Docker está instalado
check_docker() {
    if ! command -v docker &> /dev/null; then
        warn "Docker não está instalado. Deploy local será usado."
        return 1
    fi
    
    if ! command -v docker-compose &> /dev/null; then
        warn "Docker Compose não está instalado. Deploy local será usado."
        return 1
    fi
    
    log "Docker encontrado: $(docker --version)"
    log "Docker Compose encontrado: $(docker-compose --version)"
    return 0
}

# Limpar build anterior
clean_build() {
    log "Limpando build anterior..."
    mvn clean
    log "Build anterior limpo com sucesso"
}

# Executar testes
run_tests() {
    log "Executando testes..."
    mvn test
    
    if [ $? -eq 0 ]; then
        log "Testes executados com sucesso"
    else
        error "Testes falharam"
        exit 1
    fi
}

# Gerar relatório de cobertura
generate_coverage() {
    log "Gerando relatório de cobertura..."
    mvn jacoco:report
    
    if [ $? -eq 0 ]; then
        log "Relatório de cobertura gerado em target/site/jacoco/index.html"
    else
        warn "Falha ao gerar relatório de cobertura"
    fi
}

# Build do projeto
build_project() {
    log "Compilando projeto..."
    mvn package -DskipTests
    
    if [ $? -eq 0 ]; then
        log "Projeto compilado com sucesso"
        log "JAR gerado em: target/blueprint-system-1.0.0.jar"
    else
        error "Falha na compilação"
        exit 1
    fi
}

# Deploy com Docker
deploy_docker() {
    log "Iniciando deploy com Docker..."
    
    # Parar containers existentes
    log "Parando containers existentes..."
    docker-compose down || true
    
    # Build das imagens
    log "Build das imagens Docker..."
    docker-compose build
    
    # Iniciar serviços
    log "Iniciando serviços..."
    docker-compose up -d
    
    # Aguardar inicialização
    log "Aguardando inicialização dos serviços..."
    sleep 30
    
    # Verificar status
    log "Verificando status dos serviços..."
    docker-compose ps
    
    log "Deploy com Docker concluído!"
    log "Acesse: http://localhost:8081"
    log "Swagger: http://localhost:8081/swagger-ui.html"
    log "Health: http://localhost:8081/actuator/health"
}

# Deploy local
deploy_local() {
    log "Iniciando deploy local..."
    
    # Verificar se a porta está livre
    if lsof -Pi :8081 -sTCP:LISTEN -t >/dev/null ; then
        error "Porta 8081 já está em uso"
        exit 1
    fi
    
    # Executar aplicação
    log "Executando aplicação..."
    nohup java -jar target/blueprint-system-1.0.0.jar > logs/app.log 2>&1 &
    
    # Aguardar inicialização
    log "Aguardando inicialização..."
    sleep 10
    
    # Verificar se está rodando
    if curl -f http://localhost:8081/actuator/health >/dev/null 2>&1; then
        log "Aplicação iniciada com sucesso!"
        log "Acesse: http://localhost:8081"
        log "Swagger: http://localhost:8081/swagger-ui.html"
        log "Health: http://localhost:8081/actuator/health"
        log "Logs: tail -f logs/app.log"
    else
        error "Falha ao iniciar aplicação"
        exit 1
    fi
}

# Função principal
main() {
    log "=== Blueprint System - Build e Deploy ==="
    
    # Verificar pré-requisitos
    check_java
    check_maven
    DOCKER_AVAILABLE=$(check_docker)
    
    # Criar diretório de logs se não existir
    mkdir -p logs
    
    # Processo de build
    clean_build
    run_tests
    generate_coverage
    build_project
    
    # Deploy
    if [ $DOCKER_AVAILABLE -eq 0 ]; then
        read -p "Docker está disponível. Deseja usar Docker para deploy? (y/n): " -n 1 -r
        echo
        if [[ $REPLY =~ ^[Yy]$ ]]; then
            deploy_docker
        else
            deploy_local
        fi
    else
        deploy_local
    fi
    
    log "=== Build e Deploy concluído com sucesso! ==="
}

# Verificar argumentos
case "${1:-}" in
    "docker")
        check_java
        check_maven
        check_docker
        clean_build
        run_tests
        build_project
        deploy_docker
        ;;
    "local")
        check_java
        check_maven
        clean_build
        run_tests
        build_project
        deploy_local
        ;;
    "test")
        check_java
        check_maven
        clean_build
        run_tests
        generate_coverage
        ;;
    "build")
        check_java
        check_maven
        clean_build
        build_project
        ;;
    "help"|"-h"|"--help")
        echo "Uso: $0 [comando]"
        echo ""
        echo "Comandos:"
        echo "  docker    - Build e deploy com Docker"
        echo "  local     - Build e deploy local"
        echo "  test      - Executar apenas testes"
        echo "  build     - Apenas build do projeto"
        echo "  help      - Mostrar esta ajuda"
        echo ""
        echo "Se nenhum comando for especificado, será executado o processo completo."
        ;;
    *)
        main
        ;;
esac 