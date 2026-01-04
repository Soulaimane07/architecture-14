# Configuration pour Appareil Physique (USB-C)

## Adresse IP Détectée

Votre ordinateur utilise l'adresse IP : **172.19.0.1**

## Mise à Jour Requise

Vous devez modifier le fichier `Service.kt` pour utiliser cette adresse IP au lieu de `10.0.2.2` (qui est réservé à l'émulateur).

### Fichier à Modifier

**[Service.kt](file:///d:/architectures_projects/TP14/app/src/main/java/ma/projet/soapclient/ws/Service.kt)**

Ligne à modifier :
```kotlin
// AVANT (pour émulateur)
private val URL = "http://10.0.2.2:8080/services/BanqueWS"

// APRÈS (pour appareil USB)
private val URL = "http://172.19.0.1:8080/services/BanqueWS"
```

## Instructions Étape par Étape

### 1. Vérifier la Connexion WiFi

**IMPORTANT** : Votre téléphone et votre ordinateur doivent être sur le **même réseau WiFi**.

- ✅ Connectez votre ordinateur au WiFi
- ✅ Connectez votre téléphone au **même** réseau WiFi
- ✅ Le câble USB-C sert uniquement au débogage, pas à la connexion réseau

### 2. Trouver Votre Vraie Adresse IP WiFi

La commande a détecté `172.19.0.1`, mais vous devriez vérifier votre adresse WiFi :

**Option A - Via PowerShell** :
```powershell
Get-NetIPAddress -AddressFamily IPv4 | Where-Object {$_.InterfaceAlias -like "*Wi-Fi*"} | Select-Object IPAddress
```

**Option B - Via Paramètres Windows** :
1. Paramètres Windows > Réseau et Internet > WiFi
2. Cliquez sur votre réseau WiFi connecté
3. Cherchez "Adresse IPv4" (ex: `192.168.1.x` ou `10.0.0.x`)

### 3. Mettre à Jour le Code Android

Une fois que vous avez votre vraie adresse IP WiFi (ex: `192.168.1.10`), je vais mettre à jour le fichier pour vous.

**Confirmez votre adresse IP WiFi**, puis je ferai la modification automatiquement.

### 4. Activer le Débogage USB

Sur votre téléphone Android :
1. Paramètres > À propos du téléphone
2. Appuyez 7 fois sur "Numéro de build"
3. Retour > Options de développement
4. Activez "Débogage USB"
5. Connectez le câble USB-C
6. Autorisez le débogage sur le téléphone

### 5. Vérifier la Connexion dans Android Studio

1. Dans Android Studio, vérifiez que votre appareil apparaît dans la liste des appareils
2. Il devrait afficher le nom de votre téléphone

### 6. Autoriser le Trafic Réseau

**Sur Windows - Pare-feu** :
Si le pare-feu bloque la connexion, autorisez le port 8080 :
```powershell
# Exécutez en tant qu'administrateur
netsh advfirewall firewall add rule name="Spring Boot SOAP" dir=in action=allow protocol=TCP localport=8080
```

## Résolution de Problèmes

### Problème : "Connection refused" sur l'appareil

**Solutions** :
1. ✅ Vérifiez que le backend Spring Boot est démarré
2. ✅ Testez depuis votre téléphone en ouvrant Chrome et allant sur :
   ```
   http://VOTRE_IP:8080/services/BanqueWS?wsdl
   ```
   (Remplacez VOTRE_IP par celle de votre PC)
3. ✅ Désactivez temporairement le pare-feu Windows
4. ✅ Vérifiez que téléphone et PC sont sur le même WiFi

### Problème : "Aucun appareil détecté" dans Android Studio

**Solutions** :
1. ✅ Débranchez et rebranchez le câble USB-C
2. ✅ Autorisez le débogage USB sur le téléphone
3. ✅ Installez les pilotes USB du fabricant si nécessaire
4. ✅ Essayez un autre port USB

### Problème : L'app ne se connecte pas

**Solutions** :
1. ✅ Vérifiez l'adresse IP dans le code
2. ✅ Assurez-vous que le WiFi est activé sur le téléphone
3. ✅ Redémarrez le backend Spring Boot
4. ✅ Vérifiez les logs Logcat dans Android Studio

## Prochaine Étape

**Dites-moi l'adresse IP de votre réseau WiFi** et je mettrai à jour automatiquement le fichier `Service.kt` pour vous !

Pour la trouver rapidement :
```powershell
ipconfig
```

Cherchez la section "Wireless LAN adapter Wi-Fi" et notez l'"Adresse IPv4".
