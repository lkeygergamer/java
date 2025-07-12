# 📖 Guia de Boas Práticas - Projetos Java de Estudo

## 🎯 Introdução

Este guia apresenta as melhores práticas para organizar e desenvolver projetos Java de estudo, garantindo código limpo, bem documentado e profissional.

---

## 📁 Estrutura de Organização

### **Estrutura Recomendada para Repositório**

```
📁 java-studies/
├── 📄 README.md                    # Documentação principal
├── 📁 01-fundamentos/              # Conceitos básicos
├── 📁 02-orientacao-objetos/       # POO
├── 📁 03-interfaces-graficas/      # Swing/JavaFX
├── 📁 04-persistencia/             # Arquivos/BD
├── 📁 05-web/                      # Servlets/Spring
├── 📁 06-projetos-completos/       # Sistemas integrados
├── 📁 templates/                    # Templates reutilizáveis
└── 📁 docs/                        # Documentação geral
```

### **Estrutura de Cada Projeto**

```
📁 nome-do-projeto/
├── 📄 README.md                    # Documentação do projeto
├── 📄 pom.xml                      # Dependências Maven (se aplicável)
├── 📄 .gitignore                   # Arquivos a ignorar
├── 📁 src/
│   ├── 📁 main/
│   │   └── 📁 java/
│   │       └── 📁 com/
│   │           └── 📁 exemplo/
│   │               ├── 📄 Main.java
│   │               ├── 📁 model/
│   │               ├── 📁 service/
│   │               ├── 📁 controller/
│   │               └── 📁 util/
│   └── 📁 test/
│       └── 📁 java/
│           └── 📁 com/
│               └── 📁 exemplo/
│                   └── 📁 service/
├── 📁 docs/                        # Documentação específica
│   ├── 📄 conceitos.md
│   ├── 📄 exemplos.md
│   └── 📄 troubleshooting.md
└── 📁 resources/                   # Recursos (se aplicável)
```

---

## 📝 Convenções de Nomenclatura

### **Classes**
```java
// ✅ Bom: Nomes descritivos em PascalCase
public class CalculadoraFinanceira {
    // implementação
}

public class GerenciadorDeContatos {
    // implementação
}

// ❌ Evitar: Nomes genéricos
public class Calculadora {
    // implementação
}

public class Teste {
    // implementação
}
```

### **Métodos**
```java
// ✅ Bom: Verbos descritivos em camelCase
public double calcularJurosCompostos(double principal, double taxa, int tempo) {
    return principal * Math.pow(1 + taxa, tempo);
}

public void adicionarContato(Contato contato) {
    // implementação
}

// ❌ Evitar: Nomes genéricos
public double calc(double p, double t, int tm) {
    return p * Math.pow(1 + t, tm);
}

public void add(Contato c) {
    // implementação
}
```

### **Variáveis**
```java
// ✅ Bom: Nomes descritivos
double valorPrincipal = 1000.0;
double taxaJuros = 0.05;
int tempoAnos = 5;

// ❌ Evitar: Nomes curtos e genéricos
double v = 1000.0;
double t = 0.05;
int tm = 5;
```

### **Constantes**
```java
// ✅ Bom: UPPER_SNAKE_CASE
public static final double TAXA_JUROS_PADRAO = 0.05;
public static final int ANOS_MAXIMOS = 30;
public static final String MENSAGEM_ERRO = "Valor inválido";

// ❌ Evitar: camelCase para constantes
public static final double taxaJurosPadrao = 0.05;
```

---

## 💬 Comentários e Documentação

### **Comentários de Classe**
```java
/**
 * Calculadora para operações financeiras básicas
 * 
 * Esta classe fornece métodos para cálculos financeiros como
 * juros simples, juros compostos e amortização.
 * 
 * @author Seu Nome
 * @version 1.0
 * @since 2024
 */
public class CalculadoraFinanceira {
    // implementação
}
```

### **Comentários de Método**
```java
/**
 * Calcula o juros composto baseado nos parâmetros fornecidos
 * 
 * @param principal Valor inicial do investimento
 * @param taxa Taxa de juros (ex: 0.05 para 5%)
 * @param tempo Tempo em anos
 * @return Valor final com juros compostos
 * @throws IllegalArgumentException se algum parâmetro for negativo
 */
public double calcularJurosCompostos(double principal, double taxa, int tempo) {
    if (principal < 0 || taxa < 0 || tempo < 0) {
        throw new IllegalArgumentException("Parâmetros não podem ser negativos");
    }
    return principal * Math.pow(1 + taxa, tempo);
}
```

### **Comentários Inline**
```java
// ✅ Bom: Explica o "porquê", não o "o quê"
// Aplicar taxa de desconto para clientes VIP
double valorFinal = valor * 0.9;

// ❌ Evitar: Comentário óbvio
// Multiplicar valor por 0.9
double valorFinal = valor * 0.9;
```

---

## 🧪 Testes

### **Estrutura de Testes**
```java
/**
 * Testes para a classe CalculadoraFinanceira
 */
public class CalculadoraFinanceiraTest {
    
    private CalculadoraFinanceira calculadora;
    
    @BeforeEach
    void setUp() {
        calculadora = new CalculadoraFinanceira();
    }
    
    @Test
    @DisplayName("Deve calcular juros compostos corretamente")
    void deveCalcularJurosCompostos() {
        // Arrange
        double principal = 1000.0;
        double taxa = 0.05;
        int tempo = 2;
        
        // Act
        double resultado = calculadora.calcularJurosCompostos(principal, taxa, tempo);
        
        // Assert
        assertEquals(1102.5, resultado, 0.01);
    }
    
    @Test
    @DisplayName("Deve lançar exceção para parâmetros negativos")
    void deveLancarExcecaoParaParametrosNegativos() {
        // Arrange & Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            calculadora.calcularJurosCompostos(-1000, 0.05, 2);
        });
    }
}
```

### **Padrão AAA (Arrange, Act, Assert)**
```java
@Test
void exemploPadraoAAA() {
    // Arrange - Preparar dados
    double valor = 100.0;
    double taxa = 0.1;
    
    // Act - Executar ação
    double resultado = calculadora.calcularJuros(valor, taxa);
    
    // Assert - Verificar resultado
    assertEquals(110.0, resultado, 0.01);
}
```

---

## 📦 Organização de Pacotes

### **Estrutura Recomendada**
```
com.exemplo.projeto/
├── model/          # Entidades/Modelos
├── service/        # Lógica de negócio
├── controller/     # Controllers (se web)
├── repository/     # Acesso a dados
├── util/           # Utilitários
├── config/         # Configurações
└── exception/      # Exceções customizadas
```

### **Exemplo de Implementação**
```java
// Model
package com.exemplo.projeto.model;

public class Contato {
    private Long id;
    private String nome;
    private String email;
    // getters, setters, construtores
}

// Service
package com.exemplo.projeto.service;

public class ContatoService {
    public void adicionarContato(Contato contato) {
        // lógica de negócio
    }
}

// Util
package com.exemplo.projeto.util;

public class ValidacaoUtil {
    public static boolean emailValido(String email) {
        // validação
    }
}
```

---

## 🔧 Configuração de Projetos

### **Maven (pom.xml)**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <groupId>com.exemplo</groupId>
    <artifactId>nome-do-projeto</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>
    
    <properties>
        <maven.compiler.source>11</maven.compiler.source>
        <maven.compiler.target>11</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>
    
    <dependencies>
        <!-- JUnit para testes -->
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>5.8.2</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
    
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.8.1</version>
            </plugin>
        </plugins>
    </build>
</project>
```

### **.gitignore**
```gitignore
# Compiled class files
*.class

# Log files
*.log

# BlueJ files
*.ctxt

# Mobile Tools for Java (J2ME)
.mtj.tmp/

# Package Files
*.jar
*.war
*.nar
*.ear
*.zip
*.tar.gz
*.rar

# Virtual machine crash logs
hs_err_pid*

# Maven
target/
pom.xml.tag
pom.xml.releaseBackup
pom.xml.versionsBackup
pom.xml.next
release.properties
dependency-reduced-pom.xml
buildNumber.properties
.mvn/timing.properties
.mvn/wrapper/maven-wrapper.jar

# IDE
.idea/
*.iml
.vscode/
.settings/
.project
.classpath

# OS
.DS_Store
Thumbs.db
```

---

## 📚 Documentação

### **README.md Template**
```markdown
# 📚 [Nome do Projeto]

## 📋 Descrição
Breve descrição do projeto e objetivos de aprendizado.

## 🎯 Objetivos
- [ ] Objetivo 1
- [ ] Objetivo 2

## 🚀 Como Executar
Instruções de execução.

## 📁 Estrutura
Descrição da estrutura do projeto.

## 🧪 Testes
Como executar os testes.

## 📝 Notas de Aprendizado
O que foi aprendido com este projeto.
```

### **Documentação de Conceitos**
```markdown
# Conceito: [Nome do Conceito]

## O que é?
Explicação do conceito.

## Como funciona?
Explicação de como implementar.

## Exemplo Prático
```java
// Código de exemplo
```

## Quando usar?
Casos de uso apropriados.

## Referências
Links para mais informações.
```

---

## 🎯 Checklist de Qualidade

### **Antes de Commitar**
- [ ] Código compila sem erros
- [ ] Testes passam
- [ ] Nomes descritivos para classes, métodos e variáveis
- [ ] Comentários em código complexo
- [ ] Documentação JavaDoc em métodos públicos
- [ ] Tratamento de exceções adequado
- [ ] Validação de entrada de dados

### **Para Cada Projeto**
- [ ] README.md completo
- [ ] Estrutura de pacotes organizada
- [ ] Testes unitários implementados
- [ ] Documentação de conceitos
- [ ] Exemplos de uso
- [ ] Troubleshooting documentado

### **Para o Repositório**
- [ ] README principal atualizado
- [ ] Estrutura de pastas organizada
- [ ] Templates disponíveis
- [ ] Guias de boas práticas
- [ ] Exemplos de projetos

---

## 🚀 Próximos Passos

### **Para Melhorar**
1. **Implementar testes** em todos os projetos
2. **Adicionar documentação** detalhada
3. **Criar exemplos práticos** para cada conceito
4. **Implementar validações** de entrada
5. **Adicionar logging** para debug

### **Para Aprender**
1. **Estudar design patterns**
2. **Aprender frameworks** modernos
3. **Praticar TDD** (Test-Driven Development)
4. **Estudar clean code**
5. **Participar de projetos** open source

---

## 📞 Recursos Úteis

### **Documentação**
- [Java Documentation](https://docs.oracle.com/javase/)
- [Java Tutorial](https://docs.oracle.com/javase/tutorial/)
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)

### **Ferramentas**
- [IntelliJ IDEA](https://www.jetbrains.com/idea/)
- [Eclipse](https://www.eclipse.org/)
- [VS Code](https://code.visualstudio.com/)
- [Maven](https://maven.apache.org/)

### **Comunidade**
- [Stack Overflow](https://stackoverflow.com/questions/tagged/java)
- [Reddit r/java](https://www.reddit.com/r/java/)
- [Java Discord](https://discord.gg/java)

---

**Lembre-se: A qualidade do código reflete a qualidade do desenvolvedor!** 🚀✨ 