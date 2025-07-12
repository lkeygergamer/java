# Guia de Instalação - Criptografia Espectral-Ergódica Java

## 📋 Pré-requisitos

### 1. Java Development Kit (JDK) 11+

#### Windows
1. Baixe o JDK 11+ do site oficial da Oracle ou use o OpenJDK
2. Execute o instalador e siga as instruções
3. Configure a variável de ambiente `JAVA_HOME`:
   ```
   JAVA_HOME=C:\Program Files\Java\jdk-11.0.x
   ```
4. Adicione ao PATH:
   ```
   %JAVA_HOME%\bin
   ```

#### Linux (Ubuntu/Debian)
```bash
sudo apt update
sudo apt install openjdk-11-jdk
```

#### macOS
```bash
# Usando Homebrew
brew install openjdk@11

# Ou baixe do site oficial da Oracle
```

#### Verificar instalação
```bash
java -version
javac -version
```

### 2. Apache Maven 3.6+

#### Windows
1. Baixe o Maven do site oficial: https://maven.apache.org/download.cgi
2. Extraia para uma pasta (ex: `C:\Program Files\Apache\maven`)
3. Configure a variável de ambiente `MAVEN_HOME`:
   ```
   MAVEN_HOME=C:\Program Files\Apache\maven
   ```
4. Adicione ao PATH:
   ```
   %MAVEN_HOME%\bin
   ```

#### Linux (Ubuntu/Debian)
```bash
sudo apt update
sudo apt install maven
```

#### macOS
```bash
# Usando Homebrew
brew install maven

# Ou usando SDKMAN
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
sdk install maven
```

#### Verificar instalação
```bash
mvn -version
```

## 🚀 Instalação Rápida

### Windows
```cmd
# Baixe e execute o instalador do Maven
# Ou use o Chocolatey:
choco install maven

# Ou use o Scoop:
scoop install maven
```

### Linux
```bash
# Ubuntu/Debian
sudo apt update && sudo apt install openjdk-11-jdk maven

# CentOS/RHEL
sudo yum install java-11-openjdk-devel maven

# Fedora
sudo dnf install java-11-openjdk-devel maven
```

### macOS
```bash
# Usando Homebrew
brew install openjdk@11 maven

# Ou usando SDKMAN
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
sdk install java 11.0.12-open
sdk install maven
```

## 🔧 Configuração do Projeto

### 1. Clone ou baixe o projeto
```bash
git clone <url-do-repositorio>
cd javacriptografia
```

### 2. Verificar dependências
```bash
# Verificar Java
java -version

# Verificar Maven
mvn -version
```

### 3. Compilar o projeto
```bash
# Limpar e compilar
mvn clean compile

# Ou usar o script automatizado
# Windows:
build_and_test.bat

# Linux/macOS:
./build_and_test.sh
```

## 🧪 Executar Testes

### Testes unitários
```bash
mvn test
```

### Testes de segurança
```bash
# Compilar e executar teste de segurança completo
javac -cp "target/classes:target/dependency/*" teste_seguranca_completo.java
java -cp "target/classes:target/dependency/*" teste_seguranca_completo
```

### Cobertura de código
```bash
mvn jacoco:report
# Relatório disponível em: target/site/jacoco/index.html
```

## 📦 Criar JAR Executável

```bash
mvn clean package
```

O JAR será criado em: `target/javacriptografia-1.0.0.jar`

## 🎯 Executar Interface CLI

```bash
java -jar target/javacriptografia-1.0.0.jar
```

## 🔍 Solução de Problemas

### Erro: "mvn não é reconhecido"
- Verifique se o Maven está instalado
- Verifique se o PATH está configurado corretamente
- Reinicie o terminal após instalar

### Erro: "java não é reconhecido"
- Verifique se o Java está instalado
- Verifique se o JAVA_HOME está configurado
- Verifique se o PATH inclui %JAVA_HOME%\bin

### Erro de compilação
- Verifique se está usando Java 11+
- Verifique se todas as dependências estão baixadas
- Execute `mvn clean` antes de tentar novamente

### Erro de dependências
```bash
# Forçar download de dependências
mvn dependency:resolve

# Limpar cache do Maven
mvn dependency:purge-local-repository
```

## 📚 Recursos Adicionais

### IDEs Recomendadas
- **IntelliJ IDEA**: Suporte nativo ao Maven
- **Eclipse**: Plugin Maven integrado
- **VS Code**: Extensões Java e Maven

### Configuração do IDE
1. Importe o projeto como projeto Maven
2. Configure o JDK 11 como versão do projeto
3. Execute `mvn clean install` para baixar dependências

### Variáveis de Ambiente (Windows)
```cmd
JAVA_HOME=C:\Program Files\Java\jdk-11.0.x
MAVEN_HOME=C:\Program Files\Apache\maven
PATH=%JAVA_HOME%\bin;%MAVEN_HOME%\bin;%PATH%
```

### Variáveis de Ambiente (Linux/macOS)
```bash
export JAVA_HOME=/usr/lib/jvm/java-11-openjdk
export MAVEN_HOME=/usr/share/maven
export PATH=$JAVA_HOME/bin:$MAVEN_HOME/bin:$PATH
```

## ✅ Verificação Final

Execute estes comandos para verificar se tudo está funcionando:

```bash
# Verificar versões
java -version
javac -version
mvn -version

# Testar compilação
mvn clean compile

# Testar execução
mvn test

# Criar JAR
mvn package

# Executar CLI
java -jar target/javacriptografia-1.0.0.jar
```

Se todos os comandos executarem sem erro, a instalação foi bem-sucedida! 🎉 