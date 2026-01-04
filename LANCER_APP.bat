@echo off
echo ========================================
echo    LANCEUR APPLICATION ANDROID SOAP
echo ========================================
echo.

echo [ETAPE 1] Verification de l'environnement...
echo.

REM Verifier si Android Studio est installe
if exist "%ProgramFiles%\Android\Android Studio\bin\studio64.exe" (
    echo [OK] Android Studio detecte
    set STUDIO_PATH=%ProgramFiles%\Android\Android Studio\bin\studio64.exe
) else if exist "%LocalAppData%\Programs\Android\Android Studio\bin\studio64.exe" (
    echo [OK] Android Studio detecte
    set STUDIO_PATH=%LocalAppData%\Programs\Android\Android Studio\bin\studio64.exe
) else (
    echo [ERREUR] Android Studio n'est pas installe
    echo Veuillez installer Android Studio depuis: https://developer.android.com/studio
    pause
    exit /b 1
)

echo.
echo [ETAPE 2] Ouverture du projet dans Android Studio...
echo.
echo Instructions a suivre:
echo 1. Android Studio va s'ouvrir
echo 2. Attendez la synchronisation Gradle (barre en bas)
echo 3. Connectez votre appareil USB ou lancez un emulateur
echo 4. Cliquez sur le bouton RUN (triangle vert)
echo.
echo Appuyez sur une touche pour ouvrir Android Studio...
pause >nul

start "" "%STUDIO_PATH%" "%~dp0"

echo.
echo [INFO] Android Studio lance avec le projet TP14
echo.
echo RAPPEL:
echo - Pour emulateur: L'URL http://10.0.2.2:8080 est configuree
echo - Pour appareil USB: Modifiez Service.kt avec votre IP WiFi
echo.
echo N'oubliez pas de demarrer le backend Spring Boot:
echo   cd chemin\vers\backend
echo   mvn spring-boot:run
echo.
pause
