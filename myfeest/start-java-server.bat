@echo off
echo ========================================
echo    🚀 Blueprint Java API Server
echo ========================================
echo.

echo 📦 Compilando projeto Java...
if not exist "target" mkdir target
if not exist "target\classes" mkdir target\classes

:: Compila todas as classes Java
javac -cp ".;lib/*" -d target/classes src/main/java/com/myfeest/blueprint/**/*.java

if %errorlevel% neq 0 (
    echo ❌ Erro na compilação!
    pause
    exit /b 1
)

echo ✅ Compilação concluída!
echo.

echo 🌐 Iniciando servidor na porta 8081...
echo 📡 API disponível em: http://localhost:8081
echo 🎨 Editor disponível em: http://localhost:8081/editor
echo.

:: Executa o servidor
java -cp "target/classes;lib/*" com.myfeest.blueprint.api.BlueprintServer 8081

echo.
echo 👋 Servidor parado.
pause 