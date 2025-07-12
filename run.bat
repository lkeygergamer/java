@echo off
echo ========================================
echo    PROJETO JAVA FODA - RUNNER
echo ========================================
echo.

:menu
echo Escolha uma opcao:
echo 1. Compilar projeto
echo 2. Executar aplicacao
echo 3. Executar testes
echo 4. Limpar e compilar
echo 5. Executar com Docker
echo 6. Parar Docker
echo 7. Sair
echo.
set /p choice="Digite sua escolha (1-7): "

if "%choice%"=="1" goto compile
if "%choice%"=="2" goto run
if "%choice%"=="3" goto test
if "%choice%"=="4" goto clean
if "%choice%"=="5" goto docker
if "%choice%"=="6" goto stop
if "%choice%"=="7" goto exit
goto menu

:compile
echo.
echo Compilando projeto...
mvnw.cmd clean compile
echo.
echo Compilacao concluida!
pause
goto menu

:run
echo.
echo Executando aplicacao...
echo Acesse: http://localhost:8080/api
echo Swagger: http://localhost:8080/api/swagger-ui/
echo H2 Console: http://localhost:8080/api/h2-console
echo.
mvnw.cmd spring-boot:run
goto menu

:test
echo.
echo Executando testes...
mvnw.cmd test
echo.
echo Testes concluidos!
pause
goto menu

:clean
echo.
echo Limpando e compilando...
mvnw.cmd clean compile
echo.
echo Limpeza e compilacao concluidas!
pause
goto menu

:docker
echo.
echo Executando com Docker...
docker-compose up -d
echo.
echo Docker iniciado! Acesse: http://localhost:8080/api
pause
goto menu

:stop
echo.
echo Parando Docker...
docker-compose down
echo.
echo Docker parado!
pause
goto menu

:exit
echo.
echo Obrigado por usar o Projeto Java Foda!
pause
exit 