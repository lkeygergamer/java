# Multi-stage build para otimizar o tamanho da imagem
FROM maven:3.9.5-openjdk-8 AS build

# Definir diretório de trabalho
WORKDIR /app

# Copiar arquivos de dependências primeiro (para aproveitar cache do Docker)
COPY pom.xml .
COPY src ./src

# Baixar dependências e compilar
RUN mvn clean package -DskipTests

# Imagem de produção
FROM openjdk:8-jre-slim

# Criar usuário não-root para segurança
RUN groupadd -r appuser && useradd -r -g appuser appuser

# Definir diretório de trabalho
WORKDIR /app

# Copiar JAR compilado
COPY --from=build /app/target/*.jar app.jar

# Mudar propriedade do arquivo
RUN chown appuser:appuser app.jar

# Mudar para usuário não-root
USER appuser

# Expor porta
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/api/actuator/health || exit 1

# Comando para executar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"] 