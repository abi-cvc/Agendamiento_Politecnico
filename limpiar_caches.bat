@echo off
chcp 65001 >nul
color 0A
title Limpieza de Caches - Agendamiento Politécnico

echo.
echo ╔═══════════════════════════════════════════════════════════╗
echo ║                                                           ║
echo ║     LIMPIADOR DE CACHES - AGENDAMIENTO POLITÉCNICO       ║
echo ║                                                           ║
echo ╚═══════════════════════════════════════════════════════════╝
echo.
echo Este script limpiará todos los caches de:
echo   • Maven (target)
echo   • Tomcat (work directory)
echo   • Eclipse (wtpwebapps)
echo.
echo ⚠️  ADVERTENCIA: Asegúrate de que Eclipse esté CERRADO
echo.
pause

echo.
echo ═══════════════════════════════════════════════════════════
echo [PASO 1/5] Limpiando target de Maven...
echo ═══════════════════════════════════════════════════════════
cd "C:\Users\ERICK CAICEDO\git\Agendamiento_Politecnico5"
if exist target (
    rmdir /s /q target 2>nul
    if %errorlevel%==0 (
        echo ✅ Target limpiado correctamente
    ) else (
        echo ❌ Error al limpiar target
    )
) else (
    echo ℹ️  Target no existe o ya está limpio
)

echo.
echo ═══════════════════════════════════════════════════════════
echo [PASO 2/5] Limpiando work de Tomcat...
echo ═══════════════════════════════════════════════════════════
cd "C:\Users\ERICK CAICEDO\eclipse-workspace\Servers\Tomcat v10.1 Server at localhost-config"
if exist work (
    rmdir /s /q work 2>nul
    mkdir work
    if %errorlevel%==0 (
        echo ✅ Work de Tomcat limpiado correctamente
    ) else (
        echo ❌ Error al limpiar work de Tomcat
    )
) else (
    mkdir work
    echo ℹ️  Work creado desde cero
)

echo.
echo ═══════════════════════════════════════════════════════════
echo [PASO 3/5] Limpiando wtpwebapps de Eclipse...
echo ═══════════════════════════════════════════════════════════
cd "C:\Users\ERICK CAICEDO\eclipse-workspace\.metadata\.plugins\org.eclipse.wst.server.core\tmp0"
if exist wtpwebapps (
    rmdir /s /q wtpwebapps 2>nul
    mkdir wtpwebapps
    if %errorlevel%==0 (
        echo ✅ wtpwebapps limpiado correctamente
    ) else (
        echo ❌ Error al limpiar wtpwebapps
    )
) else (
    mkdir wtpwebapps
    echo ℹ️  wtpwebapps creado desde cero
)

echo.
echo ═══════════════════════════════════════════════════════════
echo [PASO 4/5] Limpiando logs de Tomcat...
echo ═══════════════════════════════════════════════════════════
cd "C:\Users\ERICK CAICEDO\eclipse-workspace\Servers\Tomcat v10.1 Server at localhost-config"
if exist logs (
    rmdir /s /q logs 2>nul
    mkdir logs
    echo ✅ Logs de Tomcat limpiados
) else (
    mkdir logs
    echo ℹ️  Logs creados desde cero
)

echo.
echo ═══════════════════════════════════════════════════════════
echo [PASO 5/5] Ejecutando Maven Clean...
echo ═══════════════════════════════════════════════════════════
cd "C:\Users\ERICK CAICEDO\git\Agendamiento_Politecnico5"
where mvn >nul 2>&1
if %errorlevel%==0 (
    echo Ejecutando: mvn clean
    call mvn clean -q
    if %errorlevel%==0 (
        echo ✅ Maven clean ejecutado correctamente
    ) else (
        echo ⚠️  Maven clean falló, pero los caches fueron limpiados
    )
) else (
    echo ⚠️  Maven no encontrado en PATH
    echo ℹ️  Los caches fueron limpiados manualmente
)

echo.
echo ═══════════════════════════════════════════════════════════
echo ✅  LIMPIEZA COMPLETADA
echo ═══════════════════════════════════════════════════════════
echo.
echo 📝 SIGUIENTES PASOS:
echo.
echo    1. Abrir Eclipse
echo    2. Project ^> Clean...
echo    3. Click derecho en proyecto ^> Maven ^> Update Project
echo    4. Click derecho en Tomcat ^> Clean...
echo    5. Click derecho en Tomcat ^> Start
echo.
echo 🌐 Luego en el navegador:
echo    - Presionar Ctrl + Shift + R para forzar recarga
echo    - O borrar cache (Ctrl + Shift + Delete)
echo.
echo ═══════════════════════════════════════════════════════════
echo.
pause

color 07
