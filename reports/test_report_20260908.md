# Rapport de tests - EcoTrack

## Analyse du code

Le projet contient une logique de calcul carbone simple mais robuste, centrée sur :

- la validation des facteurs d’émission ;
- la validation des valeurs de consommation et de footprint ;
- les contraintes métier sur les domaines `Company`, `Employee`, `ITAsset`, `AssetAssignment` et `DailyConsumption`.

Les règles principales sont implémentées via des `record` Java avec validation dans les constructeurs, notamment :

- rejet des valeurs nulles ;
- refus des valeurs négatives ;
- contrôle des chaînes vides ou blanches ;
- validation des identifiants positifs ;
- validation des emails, des codes pays et des dates de fin d’affectation.

## Risques détectés et cas limites

Les points sensibles identifiés sont surtout :

- les valeurs nulles et les chaînes vides ;
- les nombres négatifs pour énergie et émissions ;
- des dates incohérentes (`assignedTo` avant `assignedFrom`) ;
- les champs optionnels présents mais vides ;
- la précision du calcul carbon avec arrondi à 4 décimales.

## Plan de tests ajouté

Les tests couvrent :

1. calcul normal de l’empreinte carbone ;
2. arrondi à 4 décimales ;
3. rejet des valeurs nulles ;
4. rejet des valeurs négatives ;
5. validation des identifiants, emails, codes pays, notes et dates ;
6. cas optionnels vides ou nuls.

## Fichiers de test ajoutés

- `src/test/java/com/ecotrack/carbon/EnergyBasedCarbonCalculatorTest.java`
- `src/test/java/com/ecotrack/carbon/CarbonFootprintTest.java`
- `src/test/java/com/ecotrack/carbon/EmissionFactorTest.java`
- `src/test/java/com/ecotrack/domain/AssetAssignmentTest.java`
- `src/test/java/com/ecotrack/domain/CompanyTest.java`
- `src/test/java/com/ecotrack/domain/DailyConsumptionTest.java`
- `src/test/java/com/ecotrack/domain/EmployeeTest.java`
- `src/test/java/com/ecotrack/domain/ITAssetTest.java`

## Vérification

Commande exécutée :

```bash
mvn test
```

Résultat confirmé :

- Tests run: 25
- Failures: 0
- Errors: 0
- Skipped: 0
- BUILD SUCCESS
