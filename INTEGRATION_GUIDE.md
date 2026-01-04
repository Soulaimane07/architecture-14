# Guide d'Intégration Android-Backend SOAP

## ✅ Configuration Mise à Jour

Le client Android a été configuré pour correspondre à votre backend Spring Boot.

### Configuration Backend (Spring Boot)
- **Package**: `com.example.demo.ws`
- **Service Name**: `BanqueWS`
- **Namespace**: `http://ws.demo.example.com/`
- **Port**: `8080` (par défaut Spring Boot)
- **Base de données**: H2 en mémoire

### Configuration Android Client
- **Namespace**: `http://ws.demo.example.com/`
- **URL**: `http://10.0.2.2:8080/services/BanqueWS`
- **Méthodes**: `getComptes()`, `createCompte()`, `deleteCompte()`

## 📋 Étapes de Démarrage

### 1. Démarrer le Backend Spring Boot

```bash
# Dans le répertoire du backend
mvn spring-boot:run
```

Vérifiez que le service est accessible :
```
http://localhost:8080/services/BanqueWS?wsdl
```

### 2. Ouvrir le Projet Android

1. Lancez Android Studio
2. Ouvrez le projet : `d:\architectures_projects\TP14`
3. Attendez la synchronisation Gradle
4. Vérifiez qu'il n'y a pas d'erreurs de compilation

### 3. Lancer l'Application

1. Démarrez un émulateur Android (API 21+)
2. Cliquez sur le bouton "Run" (triangle vert)
3. L'application devrait afficher la liste des comptes

## 🔧 Configuration CXF pour le Backend

Si votre backend n'est pas encore configuré avec Apache CXF, ajoutez cette classe :

```java
package com.example.demo.config;

import com.example.demo.ws.CompteSoapService;
import jakarta.xml.ws.Endpoint;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CxfConfig {
    
    @Autowired
    private Bus bus;
    
    @Autowired
    private CompteSoapService compteSoapService;
    
    @Bean
    public Endpoint endpoint() {
        EndpointImpl endpoint = new EndpointImpl(bus, compteSoapService);
        endpoint.publish("/BanqueWS");
        return endpoint;
    }
}
```

Et ajoutez la dépendance dans `pom.xml` :

```xml
<dependency>
    <groupId>org.apache.cxf</groupId>
    <artifactId>cxf-spring-boot-starter-jaxws</artifactId>
    <version>4.0.0</version>
</dependency>
```

## 🧪 Test de l'Intégration

### Test 1: Récupération des Comptes
1. Lancez l'application Android
2. Au démarrage, la méthode `getComptes()` est appelée
3. Les comptes existants s'affichent dans la liste

### Test 2: Création d'un Compte
1. Cliquez sur "Ajouter"
2. Entrez un solde (ex: 3000.50)
3. Sélectionnez le type (COURANT ou EPARGNE)
4. Cliquez "Ajouter"
5. Le compte apparaît dans la liste

### Test 3: Suppression d'un Compte
1. Cliquez sur "Supprimer" sur un compte
2. Confirmez la suppression
3. Le compte disparaît de la liste

## 📱 Utilisation sur Appareil Physique

Si vous utilisez un téléphone Android physique au lieu d'un émulateur :

1. Trouvez l'adresse IP de votre ordinateur :
   ```bash
   # Windows
   ipconfig
   
   # Cherchez "Adresse IPv4" (ex: 192.168.1.10)
   ```

2. Modifiez le fichier `Service.kt` :
   ```kotlin
   // Remplacez 10.0.2.2 par l'IP de votre machine
   private val URL = "http://192.168.1.10:8080/services/BanqueWS"
   ```

3. Assurez-vous que le téléphone et l'ordinateur sont sur le même réseau WiFi

## 🐛 Résolution de Problèmes

### Erreur: "Connection refused"
✅ **Solution**: Le backend n'est pas démarré
- Démarrez le backend avec `mvn spring-boot:run`
- Vérifiez que le port 8080 n'est pas utilisé

### Erreur: "Aucun compte trouvé"
✅ **Solution**: La base de données est vide
- Ajoutez des données de test (voir `BACKEND_CONFIG.md`)
- Ou créez des comptes via l'application

### Erreur: "Erreur lors de l'ajout"
✅ **Solution**: Problème de communication SOAP
- Testez le WSDL dans un navigateur
- Vérifiez les logs du backend Spring Boot
- Vérifiez le namespace et l'URL dans `Service.kt`

### Gradle sync échoue
✅ **Solution**: 
- Vérifiez votre connexion internet
- Essayez: File > Invalidate Caches > Invalidate and Restart
- Réessayez la synchronisation

## 📚 Ressources

- [README.md](file:///d:/architectures_projects/TP14/README.md) - Documentation du projet Android
- [BACKEND_CONFIG.md](file:///d:/architectures_projects/TP14/BACKEND_CONFIG.md) - Configuration détaillée du backend
- [walkthrough.md](file:///C:/Users/zakaria/.gemini/antigravity/brain/d827a30a-0f2f-404d-8b6d-4f2d96b77b8e/walkthrough.md) - Guide d'implémentation complet

## ✨ Fonctionnalités Implémentées

| Fonctionnalité | Backend | Android | Status |
|---------------|---------|---------|--------|
| Lister les comptes | ✅ `getComptes()` | ✅ | Prêt |
| Créer un compte | ✅ `createCompte()` | ✅ | Prêt |
| Supprimer un compte | ✅ `deleteCompte()` | ✅ | Prêt |
| Affichage Material Design | N/A | ✅ | Prêt |
| Gestion d'erreurs | ✅ | ✅ | Prêt |
| Opérations async | N/A | ✅ Coroutines | Prêt |

---

**Prochaine étape**: Ouvrez le projet dans Android Studio et lancez l'application ! 🚀
