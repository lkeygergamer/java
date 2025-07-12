# Dockerfile para Blueprint System
FROM openjdk:17-jdk-slim

# Define variáveis de ambiente
ENV APP_NAME=blueprint-system
ENV APP_VERSION=1.0.0
ENV JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC"

# Cria diretório da aplicação
WORKDIR /app

# Copia o arquivo JAR da aplicação
COPY target/blueprint-system-${APP_VERSION}.jar app.jar

# Cria diretório para logs
RUN mkdir -p /app/logs

# Cria usuário não-root para segurança
RUN groupadd -r appuser && useradd -r -g appuser appuser
RUN chown -R appuser:appuser /app
USER appuser

# Expõe a porta da aplicação
EXPOSE 8081

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8081/actuator/health || exit 1

# Comando para executar a aplicação
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"] 