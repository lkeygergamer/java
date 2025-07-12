@echo off
echo ========================================
echo Criptografia Espectral-Ergodica Java
echo Script de Build e Teste
echo ========================================
echo.

REM Verificar se o Maven está instalado
mvn --version >nul 2>&1
if errorlevel 1 (
    echo ERRO: Maven nao encontrado!
    echo Instale o Maven e adicione ao PATH
    pause
    exit /b 1
)

REM Verificar se o Java está instalado
java -version >nul 2>&1
if errorlevel 1 (
    echo ERRO: Java nao encontrado!
    echo Instale o Java 11+ e adicione ao PATH
    pause
    exit /b 1
)

echo [1/6] Limpando projeto...
call mvn clean
if errorlevel 1 (
    echo ERRO: Falha na limpeza do projeto
    pause
    exit /b 1
)

echo [2/6] Compilando projeto...
call mvn compile
if errorlevel 1 (
    echo ERRO: Falha na compilacao
    pause
    exit /b 1
)

echo [3/6] Executando testes unitarios...
call mvn test
if errorlevel 1 (
    echo ERRO: Alguns testes falharam
    pause
    exit /b 1
)

echo [4/6] Gerando relatorio de cobertura...
call mvn jacoco:report
if errorlevel 1 (
    echo AVISO: Falha ao gerar relatorio de cobertura
)

echo [5/6] Criando JAR executavel...
call mvn package
if errorlevel 1 (
    echo ERRO: Falha ao criar JAR
    pause
    exit /b 1
)

echo [6/6] Executando teste de seguranca completo...
if exist "target\classes" (
    javac -cp "target\classes;target\dependency\*" teste_seguranca_completo.java
    if errorlevel 1 (
        echo AVISO: Falha ao compilar teste de seguranca
    ) else (
        java -cp "target\classes;target\dependency\*" teste_seguranca_completo
    )
) else (
    echo AVISO: Classes nao encontradas para teste de seguranca
)

echo.
echo ========================================
echo BUILD E TESTES CONCLUIDOS!
echo ========================================
echo.
echo Arquivos gerados:
echo - JAR: target\javacriptografia-1.0.0.jar
echo - Relatorio de cobertura: target\site\jacoco\index.html
echo.
echo Para executar a interface CLI:
echo java -jar target\javacriptografia-1.0.0.jar
echo.
pause 