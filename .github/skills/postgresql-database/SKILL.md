---
name: postgresql-database
description: 'Configure, implement, or migrate the EcoTrack PostgreSQL database. Use when setting up PostgreSQL, converting EcoTrack-DDL.sql from SQL Server, defining schemas, constraints, indexes, migrations, database access, or validating persistence behavior.'
argument-hint: 'Describe the PostgreSQL configuration or EcoTrack database change to implement'
---

# EcoTrack PostgreSQL Database

## Objectif

Configurer et implementer une base de donnees PostgreSQL pour EcoTrack tout en preservant les invariants metier du domaine Java et du modele de donnees SQL Server actuel.

## Cas d'utilisation

- Configurer une instance PostgreSQL locale, de test ou de production pour EcoTrack.
- Migrer `EcoTrack-DDL.sql` de SQL Server vers PostgreSQL.
- Ajouter ou faire evoluer des tables, contraintes, index ou donnees initiales.
- Introduire des migrations de base de donnees ou une integration de persistance Java.
- Diagnostiquer un probleme de schema, connexion, migration ou integrite PostgreSQL.

## Decisions requises

Avant de modifier la configuration ou le schema, identifier ces choix. Les demander a l'utilisateur lorsqu'ils ne sont pas specifies.

1. Environnement cible: developpement local, tests automatises, preproduction ou production.
2. Mode de deploiement: Docker Compose, instance PostgreSQL existante ou service gere.
3. Livraison du schema: outil de migration versionnee tel que Flyway ou Liquibase, ou scripts SQL executes manuellement.
4. Acces aux donnees Java: aucune persistance Java pour le moment, JDBC, JPA/Hibernate ou autre bibliotheque approuvee.
5. Version PostgreSQL, hote de connexion, port, nom de base et mecanisme d'authentification. Ne jamais demander ni exposer de mots de passe, jetons ou chaines de connexion completes contenant des secrets.

Lors de l'introduction d'une dependance, d'un framework ou d'un outil de migration, mettre a jour `pom.xml` et la documentation pertinente du projet dans la meme modification.

## Procedure

1. Examiner les records de domaine Java concernes, les services de gestion, leurs tests JUnit et `EcoTrack-DDL.sql`.
2. Enoncer l'invariant metier exact et la frontiere de persistance qui doit le faire respecter.
3. Choisir la plus petite implementation compatible avec l'architecture existante. Ne pas ajouter de couche de persistance Java lorsque la demande se limite a la livraison du schema.
4. Convertir intentionnellement les fonctionnalites SQL Server vers PostgreSQL:
   - Remplacer `IDENTITY` par `GENERATED ... AS IDENTITY`, sauf lorsque l'application requiert une sequence.
   - Remplacer `nvarchar` par `varchar` ou `text`, `datetime2` par `timestamp`, `bit` par `boolean` et `SYSUTCDATETIME()` par une valeur par defaut PostgreSQL sure en UTC.
   - Remplacer `GO`, les controles de catalogue SQL Server, `THROW` et la verification de deploiement T-SQL par des migrations ou instructions de verification compatibles PostgreSQL.
   - Preserver le comportement transactionnel lorsque le DDL PostgreSQL le permet.
5. Implementer les tables dans un schema explicite, normalement `public` sauf besoin different. Utiliser des identifiants `snake_case` minuscules pour les nouveaux objets PostgreSQL; si les noms actuels sont conserves pour la compatibilite, documenter le mapping et eviter les identifiants mixtes entre guillemets.
6. Preserver et appliquer les invariants EcoTrack aux deux niveaux lorsque cela est pertinent:
   - Les identifiants techniques sont strictement positifs.
   - Les valeurs texte obligatoires sont non nulles et non vides.
   - Les numeros d'employe et les adresses e-mail sont uniques par entreprise.
   - Les etiquettes d'actif sont uniques par entreprise; les numeros de serie non nuls sont uniques par entreprise.
   - Les types et statuts d'actif sont limites aux valeurs prises en charge.
   - Une affectation reference un actif et un employe existants, respecte `assigned_to >= assigned_from`, appartient a une seule entreprise, ne concerne qu'un actif actif et ne chevauche aucune autre affectation du meme actif.
   - Une consommation quotidienne reference un actif existant, est unique par `(asset_id, consumption_date)` et ne peut avoir ni energie ni carbone negatifs.
   - Le carbone est calcule a partir de l'energie et du facteur d'emission configure, arrondi a quatre decimales avec `HALF_UP`; les appelants ne peuvent pas persister une valeur calculee non fiable.
7. Pour les invariants qu'une contrainte standard ne peut pas exprimer, choisir une strategie d'application explicite:
   - Regles inter-tables d'entreprise et d'affectation d'actif actif: transaction applicative, trigger de base de donnees ou les deux.
   - Chevauchement des periodes d'affectation: types intervalle PostgreSQL avec contrainte d'exclusion lorsque la conception le permet; sinon, verification ou trigger surs pour les transactions.
   Documenter l'approche retenue et son comportement en concurrence.
8. N'ajouter que les index justifies par des besoins connus de lecture ou d'unicite. Garder les cles etrangeres, contraintes d'unicite, contraintes de verification et index uniques partiels coherents avec la validation Java.
9. Configurer les connexions hors du controle de source avec des variables d'environnement ou un gestionnaire de secrets. Fournir un `.env.example` non secret ou un modele de configuration uniquement si le projet utilise deja cette convention.
10. Ajouter des tests cibles:
   - Tests unitaires pour les calculs et validations du domaine modifies.
   - Tests d'integration contre PostgreSQL pour les migrations, contraintes et requetes lorsqu'un comportement de base de donnees est introduit ou modifie.
11. Executer la migration concernee sur une base vide, la reexecuter lorsque l'idempotence est promise, puis valider les tables, contraintes, index et donnees initiales attendus.
12. Executer `mvn test` pour les changements Java. Rapporter la validation exacte realisee ainsi que les controles dependants de l'infrastructure qui n'ont pas pu etre executes.

## Regles de donnees et de securite

- Utiliser `numeric` ou `decimal` pour les quantites d'energie et de carbone; ne jamais utiliser `real` ou `double precision` pour les calculs metier.
- Stocker les horodatages de maniere coherente en UTC. Privilegier `timestamp with time zone` pour les instants d'evenement representes par Java `Instant`.
- Utiliser des requetes parametrees pour tout SQL recevant des valeurs externes. Ne pas concatener de saisie utilisateur dans le SQL.
- Accorder au compte d'execution uniquement les privileges necessaires. Utiliser un compte de migration distinct lorsque l'environnement de deploiement le permet.
- Ne pas versionner les identifiants, sauvegardes de base contenant des donnees personnelles, sorties de build generees ou fichiers de secrets propres a un environnement.
- Planifier les changements destructifs de schema selon les etapes extension, remplissage, bascule et retrait, sauf perte de donnees explicitement approuvee.

## Criteres de completion

- L'approche PostgreSQL et l'environnement retenus sont documentes sans secrets.
- Le schema ou les migrations s'executent avec succes sur une base PostgreSQL vide.
- Les contraintes de base de donnees et les regles Java concordent pour chaque comportement modifie.
- Les donnees existantes restent compatibles, ou un plan de migration et de retour arriere est fourni.
- Les tests unitaires et d'integration pertinents passent, notamment les cas limites et violations de contraintes.
- `mvn test` passe apres les changements Java ou de regles metier.