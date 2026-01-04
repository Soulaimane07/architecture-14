# Configuration du Backend Spring Boot SOAP

## Structure du Backend

Votre backend Spring Boot SOAP est configuré avec les composants suivants :

### 1. Entité Compte

```java
package com.example.demo.entities;

@Entity
@XmlRootElement
public class Compte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double solde;
    
    @Temporal(TemporalType.DATE)
    private Date dateCreation;
    
    @Enumerated(EnumType.STRING)
    private TypeCompte type;
    
    public enum TypeCompte {
        COURANT, EPARGNE
    }
}
```

### 2. Service SOAP

```java
package com.example.demo.ws;

@Component
@WebService(serviceName = "BanqueWS")
public class CompteSoapService {
    @WebMethod
    public List<Compte> getComptes()
    
    @WebMethod
    public Compte createCompte(double solde, TypeCompte type)
    
    @WebMethod
    public boolean deleteCompte(Long id)
}
```

### 3. Configuration Spring Boot

**application.properties:**
```properties
# Base de données H2 en mémoire
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.username=sa
spring.datasource.password=
spring.datasource.driverClassName=org.h2.Driver

# JPA
spring.jpa.hibernate.ddl-auto=update

# Console H2
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

## Configuration Requise pour JAX-WS

### Dépendances Maven (pom.xml)

Ajoutez ces dépendances si elles ne sont pas présentes :

```xml
<dependencies>
    <!-- Spring Boot Starter Web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    
    <!-- Spring Boot Starter Data JPA -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    
    <!-- H2 Database -->
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>runtime</scope>
    </dependency>
    
    <!-- JAX-WS (Apache CXF pour Spring Boot 3.x) -->
    <dependency>
        <groupId>org.apache.cxf</groupId>
        <artifactId>cxf-spring-boot-starter-jaxws</artifactId>
        <version>4.0.0</version>
    </dependency>
    
    <!-- Lombok -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
</dependencies>
```

### Configuration du Endpoint SOAP

Créez une classe de configuration pour enregistrer le service SOAP :

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

## URLs du Service

Une fois le backend démarré :

- **Service SOAP**: http://localhost:8080/services/BanqueWS
- **WSDL**: http://localhost:8080/services/BanqueWS?wsdl
- **Console H2**: http://localhost:8080/h2-console

## Configuration Client Android

Le client Android est maintenant configuré avec :

```kotlin
private val NAMESPACE = "http://ws.demo.example.com/"
private val URL = "http://10.0.2.2:8080/services/BanqueWS"
```

**Important**: 
- `10.0.2.2` est l'adresse pour accéder à `localhost` depuis l'émulateur Android
- Pour un appareil physique, utilisez l'adresse IP de votre machine (ex: `192.168.1.x`)

## Démarrage du Backend

1. Compilez le projet :
   ```bash
   mvn clean install
   ```

2. Démarrez l'application :
   ```bash
   mvn spring-boot:run
   ```

3. Vérifiez que le service est accessible :
   - Ouvrez http://localhost:8080/services/BanqueWS?wsdl dans un navigateur
   - Vous devriez voir le WSDL du service

## Test avec SoapUI (Optionnel)

Pour tester le service avant de lancer l'app Android :

1. Téléchargez SoapUI
2. Créez un nouveau projet SOAP avec le WSDL : http://localhost:8080/services/BanqueWS?wsdl
3. Testez les méthodes `getComptes`, `createCompte`, `deleteCompte`

## Données de Test

Pour insérer des données de test au démarrage, créez une classe :

```java
package com.example.demo;

import com.example.demo.entities.Compte;
import com.example.demo.repositories.CompteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

@Configuration
public class DataLoader {
    
    @Bean
    CommandLineRunner initDatabase(CompteRepository repository) {
        return args -> {
            repository.save(new Compte(null, 5000.0, new Date(), Compte.TypeCompte.COURANT));
            repository.save(new Compte(null, 10000.0, new Date(), Compte.TypeCompte.EPARGNE));
            repository.save(new Compte(null, 2500.0, new Date(), Compte.TypeCompte.COURANT));
        };
    }
}
```

## Troubleshooting

### Erreur: Namespace incorrect
Si vous obtenez des erreurs de namespace, vérifiez que :
- Le package du service est `com.example.demo.ws`
- Le namespace généré est `http://ws.demo.example.com/`

### Erreur: Connection refused
- Vérifiez que le backend est bien démarré sur le port 8080
- Testez avec curl : `curl http://localhost:8080/services/BanqueWS?wsdl`

### Erreur: 404 Not Found
- Vérifiez la configuration CXF
- Assurez-vous que le endpoint est publié à `/services/BanqueWS`
