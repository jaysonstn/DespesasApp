@echo off
echo [1/3] Gerando lista de arquivos .java...
dir /S /B src\*.java > sources.txt

echo [2/3] Compilando...
javac -encoding UTF-8 --module-path lib\javafx-sdk-21.0.7\lib --add-modules javafx.controls,javafx.fxml -cp lib\sqlite-jdbc.jar -d out @sources.txt
IF ERRORLEVEL 1 (
    echo ❌ Erro na compilação. Corrija os erros acima.
    pause
    exit /b
)

echo [3/3] Executando aplicacao...
java --module-path lib\javafx-sdk-21.0.7\lib --add-modules javafx.controls,javafx.fxml -cp out;lib\sqlite-jdbc-3.49.1.0.jar app.Main
