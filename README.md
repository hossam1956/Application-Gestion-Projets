# Architecture du Projet

Le projet est organisé en deux composants principaux :

## Backend
- **Technologies** : Spring Boot (Java)
- **Répertoire** : `projet-multiplateforme-e2425g3_1-back`
- **Description** : Ce module gère la logique côté serveur, y compris les points de terminaison API, la logique métier et les interactions avec la base de données.

## Frontend
- **Technologies** : Angular
- **Répertoire** : `projet-multiplateforme-e2425g3_1-front`
- **Description** : Ce module gère l'interface côté client, offrant aux utilisateurs une expérience interactive.

---

# Instructions d'Installation et de Configuration

Suivez ces étapes pour configurer et exécuter le projet sur votre machine locale :

## Prérequis

Assurez-vous d'avoir les éléments suivants installés :

- **Java Development Kit (JDK)** : Version 21
- **Node.js et npm** : Node.js version 14 ou supérieure (npm est inclus avec Node.js)
- **Angular CLI** : Installez-le globalement avec `npm install -g @angular/cli`
- **Maven** : Assurez-vous qu'il est installé et ajouté au PATH de votre système
- **Git** : Pour cloner le dépôt

## Cloner le Dépôt

```bash
git clone https://github.com/iir-projets/projet-multiplateforme-e2425g3_1.git
cd projet-multiplateforme-e2425g3_1
```

## Configuration du Backend

1. **Accédez au répertoire du backend** :

   ```bash
   cd projet-multiplateforme-e2425g3_1-back
   ```

2. **Construisez le projet avec Maven** :

   ```bash
   mvn clean install
   ```

3. **Exécutez l'application Spring Boot** :

   ```bash
   mvn spring-boot:run
   ```

   Le serveur backend doit maintenant être accessible à l'adresse [http://localhost:8080](http://localhost:8080).

## Configuration du Frontend

1. **Accédez au répertoire du frontend** :

   ```bash
   cd ../projet-multiplateforme-e2425g3_1-front
   ```

2. **Installez les dépendances** :

   ```bash
   npm install
   ```

3. **Servez l'application Angular** :

   ```bash
   ng serve
   ```

   L'application frontend doit maintenant être accessible à l'adresse [http://localhost:4200](http://localhost:4200).

## Configuration

### Backend
Les fichiers de configuration se trouvent dans `src/main/resources/application.properties`. Modifiez les paramètres de connexion à la base de données et d'autres propriétés si nécessaire.

### Frontend
Les fichiers de configuration se trouvent dans `src/environments/`. Assurez-vous que les points de terminaison API correspondent à l'URL du serveur backend.

## Configuration de la Base de Données

Assurez-vous que votre serveur de base de données est en cours d'exécution et accessible. Mettez à jour le fichier `application.properties` du backend avec les détails corrects de connexion à la base de données.

## Exécution des Tests

### Backend
Exécutez les tests avec Maven :

```bash
mvn test
```

### Frontend
Exécutez les tests avec Angular CLI :

```bash
ng test
```
