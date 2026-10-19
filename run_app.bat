@echo off
cd /d "%~dp0"

if not exist out mkdir out
javac -cp "lib\*" -d out .\app\*.java .\modelo\*.java .\servicio\*.java .\vista\*.java
if errorlevel 1 (
    echo.
    echo Error de compilacion.
    pause
    exit /b 1
)

java -cp "out;lib\*" app.Main
