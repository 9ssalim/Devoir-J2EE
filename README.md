# Devoir-J2EE

# **Projet Microservices**

Ce projet met en œuvre un ensemble de microservices pour la gestion des **commandes** et des **produits**, en intégrant des fonctionnalités de **découverte de services, configuration centralisée, résilience et une passerelle API**.

## **Aperçu des Microservices**

Le projet comprend les services suivants :

1. **Eureka Server (Registre de Services)**  
   - **Port :** `8761`  
   - Fournit la découverte de services pour les microservices.  
   - URL : `http://localhost:8761`

2. **Config Server (Configuration Centralisée)**  
   - **Port :** `8085`  
   - Récupère les configurations depuis un dépôt GitHub.  
   - Dépôt GitHub : `https://github.com/9ssalim/config-repo`  
   - URL : `http://localhost:8085`

3. **API Gateway (Point d'accès unique)**  
   - **Port :** `8081`  
   - Gère le routage et l'équilibrage de charge des requêtes vers les services backend.  
   - Routes configurées :
     - `/commandes/**` → `microservice-commandes`
     - `/produits/**` → `microservice-produit`

4. **Microservice Commandes (Gestion des Commandes)**  
   - **Port :** `8080`  
   - Gère les commandes et communique avec le microservice produit.  
   - Utilise une base de données H2 en mémoire.  
   - Récupère sa configuration depuis le Config Server.

5. **Microservice Produit (Gestion des Produits)**  
   - **Port :** `8082`  
   - Fournit les opérations CRUD pour les produits.  
   - Utilise une base de données H2 en mémoire.

---

## **Fonctionnalités Clés**

- **Configuration Centralisée :**  
  La configuration est gérée via **Spring Cloud Config Server**, stockée sur GitHub.
  
- **Découverte de Services :**  
  Les microservices sont enregistrés auprès de **Eureka**, permettant une découverte et un équilibrage dynamiques.

- **Résilience et Tolérance aux Pannes :**  
  Mise en œuvre avec **Resilience4j**, avec des mécanismes de circuit breaker, retry et timeout.

- **Passerelle API :**  
  Fournit un point d'accès unique pour diriger les requêtes vers les microservices.

- **Supervision et Santé des Applications :**  
  Utilisation de Spring Boot Actuator pour le monitoring des applications.

- **Mise à Jour Dynamique des Configurations :**  
  Les propriétés peuvent être mises à jour sans redémarrer les services via l'endpoint Actuator refresh.

---

## **Prérequis**

Avant d'exécuter le projet, assurez-vous d'avoir installé :

- **Java 17+**  
- **Maven**  
- **Git**

---

## **Comment Exécuter le Projet**

### **1. Cloner le Dépôt**
```bash
git clone https://github.com/your-username/your-repo.git
cd your-repo
```

### **2. Lancer les Services dans l'Ordre Suivant**

#### **Étape 1 : Lancer Eureka Server**
```bash
cd eureka-server
mvn spring-boot:run
```
URL : `http://localhost:8761`

#### **Étape 2 : Lancer le Config Server**
```bash
cd config-server
mvn spring-boot:run
```
Vérifier la configuration : `http://localhost:8085/microservice-commandes/default`

#### **Étape 3 : Lancer l'API Gateway**
```bash
cd api-gateway
mvn spring-boot:run
```

#### **Étape 4 : Lancer le Microservice Commandes**
```bash
cd microservice-commandes
mvn spring-boot:run
```
Vérification : `http://localhost:8080/actuator/health`

#### **Étape 5 : Lancer le Microservice Produit**
```bash
cd microservice-produit
mvn spring-boot:run
```

---

## **Tests du Projet**

### **1. Vérification de l'Enregistrement auprès d'Eureka**
- Accéder à `http://localhost:8761` pour voir les services enregistrés.

### **2. Vérification de la Passerelle API**
- `GET http://localhost:8081/commandes`
- `GET http://localhost:8081/produits`

### **3. Test de Mise à Jour de la Configuration**
1. Modifier la configuration dans le dépôt GitHub.  
2. Rafraîchir la configuration :
   ```bash
   curl -X POST http://localhost:8080/actuator/refresh
   ```

---

## **Contributeurs**

- **Salim Bensaber**  
- **Dina Grimel**

---

## **Licence**

Ce projet est sous licence MIT.

---

