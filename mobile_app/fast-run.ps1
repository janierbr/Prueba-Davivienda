$ANDROID_HOME = "C:\Users\JANIER USER\AppData\Local\Android\Sdk"
$JAVA_HOME = "C:\Program Files\Android\Android Studio\jbr"
$EMULATOR_EXE = "$ANDROID_HOME\emulator\emulator.exe"
$ADB_EXE = "$ANDROID_HOME\platform-tools\adb.exe"
$AVD_NAME = "Medium_Phone_API_36.1"

# Configurar variables de entorno para esta sesión
$env:JAVA_HOME = $JAVA_HOME
$env:PATH = "$JAVA_HOME\bin;" + $env:PATH
$env:ANDROID_HOME = $ANDROID_HOME

Write-Host "--- Iniciando Proceso de Ejecución Rápida ---" -ForegroundColor Cyan

# 1. Verificar si hay un emulador o dispositivo ya conectado
Write-Host "Verificando dispositivos conectados..."
$devices = & $ADB_EXE devices | Select-String -Pattern "device$"

if ($null -eq $devices) {
    Write-Host "No se detectó ningún emulador. Iniciando $AVD_NAME..." -ForegroundColor Yellow
    Start-Process -FilePath $EMULATOR_EXE -ArgumentList "-avd $AVD_NAME" -WindowStyle Minimized
    
    # Esperar a que el emulador esté listo
    Write-Host "Esperando a que el emulador arranque..."
    $ready = $false
    while (-not $ready) {
        $status = & $ADB_EXE shell getprop sys.boot_completed 2>$null
        if ($status -eq "1") {
            $ready = $true
        }
        else {
            Start-Sleep -Seconds 2
        }
    }
    Write-Host "¡Emulador listo!" -ForegroundColor Green
}
else {
    Write-Host "Dispositivo detectado. Saltando inicio de emulador." -ForegroundColor Green
}

# 2. Ejecutar la aplicación
Write-Host "Lanzando React Native..." -ForegroundColor Cyan
npm run android
