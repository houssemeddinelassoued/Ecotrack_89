# Reference domaine-interface EcoTrack

## Utilisateurs cibles

- Gestionnaire du parc IT: recherche les actifs, gere leur statut et leurs affectations.
- Responsable RSE: suit l'energie, le carbone et les tendances de l'entreprise.
- Administrateur d'entreprise: gere les employes et controle l'acces au tenant.

## Entites et champs utiles

| Entite | Champs principaux | Contraintes d'interface |
|---|---|---|
| Company | legalName, registrationNumber, countryCode, active | `countryCode` contient deux lettres majuscules |
| Employee | employeeNumber, firstName, lastName, email, department, active | numero et e-mail uniques dans l'entreprise |
| ITAsset | assetTag, assetType, hostName, manufacturer, model, serialNumber, purchaseDate, status | tag unique; numero de serie optionnel mais unique |
| AssetAssignment | assetId, employeeId, assignedFrom, assignedTo, assignmentNotes | actif `ACTIVE`, meme entreprise, periodes sans chevauchement |
| DailyConsumption | assetId, consumptionDate, energyInKwh, carbonInKilogramsOfCo2e, dataSource | energie positive ou nulle; carbone recalcule par le domaine |

## Enumerations

- `AssetType`: `LAPTOP`, `SERVER`.
- `AssetStatus`: `ACTIVE`, `INACTIVE`, `RETIRED`, `DISPOSED`.

Verifier le code Java avant d'ajouter une valeur. Les libelles affiches peuvent etre traduits, mais les valeurs de transport doivent suivre le contrat API.

## Parcours prioritaires

### Vue d'ensemble

Afficher l'energie et le carbone sur une periode, leur evolution, la repartition par type d'actif et les actifs les plus consommateurs. Chaque indicateur doit preciser son unite, sa periode et son etat de donnees.

### Gestion des actifs

Permettre la recherche par tag, nom d'hote, modele ou numero de serie; filtrer par type et statut; ouvrir le detail et l'historique de consommation. Confirmer les changements irreversibles de statut.

### Affectation d'un actif

Selectionner uniquement un actif actif et un employe de la meme entreprise. Afficher la periode, signaler un chevauchement renvoye par l'API et conserver l'historique lors de la cloture.

### Saisie de consommation

Saisir l'actif, la date, l'energie en kWh et une source optionnelle. Afficher le carbone seulement apres calcul serveur. Une seule consommation est admise par actif et par date.

### Gestion des employes

Rechercher par numero, nom ou e-mail; afficher le statut et les affectations actives. Une desactivation conserve l'employe et son historique.

## Limites de securite

Le domaine actuel ne contient ni authentification ni autorisation. Toute future API doit verifier l'identite, le role et l'entreprise pour chaque lecture et mutation. Les erreurs affichees ne doivent pas permettre de deduire l'existence d'une ressource hors du tenant.
