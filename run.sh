#!/bin/bash

echo "========================================"
echo "   PROJETO JAVA FODA - RUNNER"
echo "========================================"
echo

while true; do
    echo "Escolha uma opção:"
    echo "1. Compilar projeto"
    echo "2. Executar aplicação"
    echo "3. Executar testes"
    echo "4. Limpar e compilar"
    echo "5. Executar com Docker"
    echo "6. Parar Docker"
    echo "7. Sair"
    echo
    read -p "Digite sua escolha (1-7): " choice

    case $choice in
        1)
            echo
            echo "Compilando projeto..."
            ./mvnw clean compile
            echo
            echo "Compilação concluída!"
            read -p "Pressione Enter para continuar..."
            ;;
        2)
            echo
            echo "Executando aplicação..."
            echo "Acesse: http://localhost:8080/api"
            echo "Swagger: http://localhost:8080/api/swagger-ui/"
            echo "H2 Console: http://localhost:8080/api/h2-console"
            echo
            ./mvnw spring-boot:run
            ;;
        3)
            echo
            echo "Executando testes..."
            ./mvnw test
            echo
            echo "Testes concluídos!"
            read -p "Pressione Enter para continuar..."
            ;;
        4)
            echo
            echo "Limpando e compilando..."
            ./mvnw clean compile
            echo
            echo "Limpeza e compilação concluídas!"
            read -p "Pressione Enter para continuar..."
            ;;
        5)
            echo
            echo "Executando com Docker..."
            docker-compose up -d
            echo
            echo "Docker iniciado! Acesse: http://localhost:8080/api"
            read -p "Pressione Enter para continuar..."
            ;;
        6)
            echo
            echo "Parando Docker..."
            docker-compose down
            echo
            echo "Docker parado!"
            read -p "Pressione Enter para continuar..."
            ;;
        7)
            echo
            echo "Obrigado por usar o Projeto Java Foda!"
            exit 0
            ;;
        *)
            echo "Opção inválida!"
            read -p "Pressione Enter para continuar..."
            ;;
    esac
done 