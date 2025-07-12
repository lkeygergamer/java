#!/bin/bash

echo "========================================"
echo "Criptografia Espectral-Ergodica Java"
echo "Script de Build e Teste"
echo "========================================"
echo

# Verificar se o Maven está instalado
if ! command -v mvn &> /dev/null; then
    echo "ERRO: Maven não encontrado!"
    echo "Instale o Maven e adicione ao PATH"
    exit 1
fi

# Verificar se o Java está instalado
if ! command -v java &> /dev/null; then
    echo "ERRO: Java não encontrado!"
    echo "Instale o Java 11+ e adicione ao PATH"
    exit 1
fi

# Verificar versão do Java
JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 11 ]; then
    echo "ERRO: Java 11+ é necessário. Versão atual: $JAVA_VERSION"
    exit 1
fi

echo "[1/6] Limpando projeto..."
mvn clean
if [ $? -ne 0 ]; then
    echo "ERRO: Falha na limpeza do projeto"
    exit 1
fi

echo "[2/6] Compilando projeto..."
mvn compile
if [ $? -ne 0 ]; then
    echo "ERRO: Falha na compilação"
    exit 1
fi

echo "[3/6] Executando testes unitários..."
mvn test
if [ $? -ne 0 ]; then
    echo "ERRO: Alguns testes falharam"
    exit 1
fi

echo "[4/6] Gerando relatório de cobertura..."
mvn jacoco:report
if [ $? -ne 0 ]; then
    echo "AVISO: Falha ao gerar relatório de cobertura"
fi

echo "[5/6] Criando JAR executável..."
mvn package
if [ $? -ne 0 ]; then
    echo "ERRO: Falha ao criar JAR"
    exit 1
fi

echo "[6/6] Executando teste de segurança completo..."
if [ -d "target/classes" ]; then
    javac -cp "target/classes:target/dependency/*" teste_seguranca_completo.java
    if [ $? -eq 0 ]; then
        java -cp "target/classes:target/dependency/*" teste_seguranca_completo
    else
        echo "AVISO: Falha ao compilar teste de segurança"
    fi
else
    echo "AVISO: Classes não encontradas para teste de segurança"
fi

echo
echo "========================================"
echo "BUILD E TESTES CONCLUÍDOS!"
echo "========================================"
echo
echo "Arquivos gerados:"
echo "- JAR: target/javacriptografia-1.0.0.jar"
echo "- Relatório de cobertura: target/site/jacoco/index.html"
echo
echo "Para executar a interface CLI:"
echo "java -jar target/javacriptografia-1.0.0.jar"
echo 