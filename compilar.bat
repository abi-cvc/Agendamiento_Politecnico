@echo off
echo ========================================
echo   COMPILACION Y VERIFICACION
echo   API REST - RecursoCancelarCita
echo ========================================
echo.

cd /d "%~dp0"

echo [1/3] Limpiando proyecto...
call mvn clean
echo.

echo [2/3] Compilando proyecto...
call mvn compile
if %errorlevel% neq 0 (
    echo.
    echo ❌ ERROR: La compilacion fallo
    echo Revisa los errores arriba
    pause
    exit /b 1
)
echo.

echo [3/3] Verificando archivos compilados...
echo.

if exist "target\classes\com\recursos\RecursoCancelarCita.class" (
    echo ✅ RecursoCancelarCita.class ENCONTRADA
    dir "target\classes\com\recursos\RecursoCancelarCita.class"
    echo.
    echo ========================================
    echo   COMPILACION EXITOSA
    echo ========================================
    echo.
    echo Ahora en Eclipse:
    echo 1. Servers ^> Click derecho en Tomcat ^> Clean Tomcat Work Directory
    echo 2. Click derecho en Tomcat ^> Publish
    echo 3. Stop Server ^(boton rojo^)
    echo 4. Start Server ^(boton verde^)
    echo 5. Probar: http://localhost:8080/Agendamiento_Politecnico5/rest/api/citas/1
    echo.
) else (
    echo ❌ RecursoCancelarCita.class NO ENCONTRADA
    echo.
    echo La clase no se compilo correctamente.
    echo Revisa los errores de Maven arriba.
    echo.
)

pause
