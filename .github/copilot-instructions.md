# Instructions Copilot - EcoTrack

## Contexte du projet

EcoTrack est un MVP de suivi de la consommation energetique des actifs informatiques et du calcul de leur empreinte carbone. Le coeur du projet est un domaine Java sans framework applicatif ni couche de persistance Java a ce stade.

## Stack technologique

- Java 25, avec le niveau de compilation Maven configure par `maven.compiler.release`.
- Maven pour la construction et les dependances.
- JUnit Jupiter 5 pour les tests unitaires; executer `mvn test` avant de finaliser une modification fonctionnelle.
- SQL Server pour le modele relationnel, defini dans `EcoTrack-DDL.sql`.
- Les packages applicatifs sont sous `com.ecotrack`:
  - `carbon` contient le calcul independant de l'empreinte carbone.
  - `domain` contient les entites, contrats de recherche et services metier.

## Regles de codage generales

- Ecrire le code de production en anglais: noms de classes, methodes, variables, messages d'erreur et tests.
- Respecter la mise en forme Java existante: indentation de 4 espaces, accolades sur la meme ligne, une classe publique par fichier.
- Privilegier les `record` immuables pour les objets metier de valeur ou les entites sans cycle de vie mutable.
- Declarer les services et implementations non extensibles en `final` sauf besoin explicite d'extension.
- Valider les invariants a la frontiere des objets et services avec `Objects.requireNonNull` et `IllegalArgumentException` ou une exception metier adaptee.
- Utiliser `Optional` pour les champs et retours optionnels; ne jamais y placer `null` et ne pas retourner `null` lorsqu'un `Optional` est attendu.
- Utiliser `BigDecimal` pour toute valeur d'energie ou de carbone. Ne pas utiliser `double` ou `float` pour les calculs metier.
- Utiliser `java.time` (`Instant`, `LocalDate`) pour les dates et instants; ne pas utiliser les anciennes API `Date` ou `Calendar`.
- Garder le calcul carbone decouple du package `domain` derriere l'interface `CarbonCalculator`.
- Ajouter ou mettre a jour les tests JUnit 5 pour tout comportement modifie, y compris les valeurs limites et les erreurs de validation.
- Ne pas modifier les artefacts generes ou copies: `target/` et `bin/`.
- Ne pas ajouter de dependance, de framework ou de couche technique sans besoin fonctionnel explicite et sans mise a jour de `pom.xml` et de la documentation associee.

## Regles metier generales

- Les identifiants techniques (`CompanyId`, `EmployeeId`, `AssetId`, `AssignmentId`, `ConsumptionId`) doivent etre strictement positifs.
- Les champs texte obligatoires sont non nuls et non vides; une valeur optionnelle presente ne doit jamais etre vide.
- Une entreprise possede des employes et des actifs. Les numeros d'employe et adresses e-mail sont uniques dans leur entreprise.
- Les actifs sont identifies de maniere unique par `(companyId, assetTag)`; un numero de serie renseigne est unique dans l'entreprise.
- Un actif ne peut etre affecte qu'a un employe de la meme entreprise et uniquement lorsque son statut est `ACTIVE`.
- Une affectation doit avoir `assignedTo >= assignedFrom`. Deux affectations d'un meme actif ne peuvent pas couvrir des periodes qui se chevauchent.
- La desactivation d'un employe conserve son historique; ne pas supprimer physiquement les donnees metier existantes sans exigence explicite.
- Une consommation quotidienne est rattachee a un actif existant et est unique pour le couple `(assetId, consumptionDate)`.
- L'energie consommee et le carbone calcule ne peuvent pas etre negatifs.
- Calculer l'empreinte carbone avec `energieKwh x facteurEmissionKgCO2eParKwh`, puis arrondir a 4 decimales avec `RoundingMode.HALF_UP`.
- Recalculer la valeur carbone lors de l'enregistrement d'une consommation a partir du `CarbonCalculator` et du facteur d'emission configure; ne pas faire confiance a une valeur carbone fournie par l'appelant.
- Maintenir la coherence entre les contraintes Java et le schema SQL Server: cles etrangeres, unicites, valeurs autorisees, bornes numeriques et contraintes de dates.

## Pratiques de livraison

- Limiter chaque modification au besoin demande et eviter les refactorings connexes.
- Executer `mvn test` apres les changements Java ou de regles metier et signaler clairement tout echec existant non lie a la modification.
- Lors d'une evolution du modele relationnel, modifier `EcoTrack-DDL.sql` et conserver ses controles de deploiement coherents avec le schema.