# WikiPages

WikiPages est une application Scala qui utilise l'API de Wikipedia pour récupérer des informations sur les pages associées à un mot clé donné. L'application permet de limiter le nombre de pages renvoyées et d'analyser les informations de ces pages, telles que le titre et le nombre de mots.

## Fonctionnalités

- Recherche de pages associées à un mot clé sur Wikipedia.
- Limitation du nombre de pages renvoyées.
- Analyse des informations des pages, notamment le titre et le nombre de mots.
- Calcul du nombre total de mots pour toutes les pages récupérées.

## Installation

1. Clonez le repository GitHub :

   ```bash
   git clone https://github.com/votre_utilisateur/wikipages.git

2. Accédez au répertoire du projet :

   ```bash
   cd wikipages

3. Compilez le projet avec sbt :

   ```bash
   sbt compile


## Utilisation

Exécutez l'application en utilisant la commande run de sbt :

   ```bash
   sbt run --limit 5 scala
```

- L'argument --limit spécifie le nombre de pages à récupérer (par défaut : 10).
- L'argument <keyword> spécifie le mot clé à rechercher (obligatoire).

L'application affiche les informations des pages récupérées, y compris le nombre total de pages et le nombre total de mots.

## Tests

Vous pouvez exécuter les tests unitaires avec la commande sbt suivante :

   ```bash
   sbt test
```

Les tests vérifient le fonctionnement des différentes fonctions de l'application, telles que la création de l'URL de recherche, l'analyse des données JSON et le calcul du nombre total de mots.

## Auteurs

- Aboubacar CAMARA
- Aaricia DOMINGUEZ
- Adrien ALVAREZ
