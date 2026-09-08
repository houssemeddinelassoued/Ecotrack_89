---
name: ecotrack-frontend
description: 'Concevoir, implementer, tester ou revoir le frontend EcoTrack. Utiliser pour une interface web, un dashboard Green IT, les ecrans actifs, employes, affectations et consommations, les contrats API frontend, le responsive design ou les tests UI.'
argument-hint: 'Decrire l ecran, le parcours utilisateur ou le composant frontend EcoTrack a realiser'
---

# Developpement frontend EcoTrack

## Objectif

Construire une interface operationnelle pour les equipes DSI et RSE, coherente avec le domaine Java EcoTrack, accessible, responsive et testable. Le projet ne possede actuellement ni frontend ni API: ne pas inventer une stack ou un contrat de transport sans les rendre explicites.

Les ressources fournies par ce skill restent independantes du framework. La conception d'un contrat API precede toute implementation de composant connecte.

## Decisions requises

Avant une premiere implementation, identifier les points suivants. Demander une clarification si le depot ne permet pas de les determiner.

1. Stack frontend et versions pour l'implementation demandee: React, Vue, Angular, application HTML ou autre choix approuve. Ne pas convertir les ressources partagees du skill vers un framework particulier.
2. Emplacement du frontend: module dans ce depot ou projet separe.
3. Outil de build, gestionnaire de paquets, bibliotheque de composants, icones et strategie CSS.
4. Etat du contrat API et disponibilite de son environnement. Les mocks ou fixtures doivent etre generes a partir du contrat accepte.
5. Authentification, roles et contexte d'entreprise. Toute requete metier doit etre limitee au tenant autorise.
6. Navigateurs, langues et niveaux d'accessibilite cibles.

Utiliser le [gabarit d'architecture](./assets/frontend-architecture.template.md) pour consigner ces choix. Ne pas ajouter de dependance avant validation de la stack.

## Procedure

1. Examiner `pom.xml`, le domaine sous `src/main/java/com/ecotrack`, les tests metier et les eventuels fichiers frontend deja presents.
2. Lire la [reference domaine-interface](./references/domain-ui.md) et identifier le parcours concerne, ses invariants et les actions autorisees.
3. Formuler un contrat d'ecran court: utilisateur cible, objectif, donnees, actions, etats et criteres d'acceptation.
4. Si aucune API n'existe, definir et faire accepter son contrat avant tout composant connecte. Partir du [gabarit OpenAPI](./assets/api-contract.template.yaml), puis aligner les operations, schemas et erreurs avec le domaine Java. Ne jamais appeler directement les services Java depuis le navigateur.
5. Definir les types de transport avant les composants. Transporter les valeurs decimales d'energie et de carbone sous forme de chaines afin de ne pas perdre de precision. Adapter le [modele TypeScript](./examples/domain-model.example.ts) au contrat reel.
6. Concevoir une interface de travail dense et lisible:
   - Navigation persistante vers Vue d'ensemble, Actifs, Employes, Affectations et Consommations.
   - Tableaux filtrables pour les collections, panneaux ou pages dedies pour les details, formulaires courts pour les mutations.
   - Hierarchie typographique contenue, couleurs semantiques, espacements reguliers et cartes reservees aux elements repetes ou veritablement groupes.
   - Aucun ecran marketing, hero decoratif, texte d'aide permanent ou section flottante sans fonction metier.
7. Partir des [tokens visuels](./assets/design-tokens.template.css) et du [dashboard d'exemple](./examples/dashboard.example.html), puis les adapter au framework retenu. Utiliser la bibliotheque d'icones du projet, avec libelles accessibles et info-bulles pour les icones ambigues.
8. Implementer tous les etats du parcours: initial, chargement, succes, vide, erreur, absence d'autorisation et donnees partielles. Eviter les changements de mise en page pendant le chargement.
9. Appliquer les validations metier cote interface pour guider l'utilisateur, sans les considerer comme une protection suffisante. Le backend reste autoritaire.
10. Rendre chaque interaction utilisable au clavier, associer les champs a leurs libelles, gerer le focus des dialogues et annoncer les resultats asynchrones importants.
11. Ajouter des tests proportionnes au risque: fonctions de formatage, composants interactifs, formulaires, erreurs API et parcours critiques.
12. Valider avec la [checklist qualite](./references/quality-checklist.md). Lancer les commandes de formatage, lint, typage, tests et build definies par la stack.

## Regles d'interface EcoTrack

- Afficher l'energie en `kWh` et le carbone en `kgCO2e`, avec au plus quatre decimales lorsque la precision source le justifie.
- Afficher les dates dans le format local de l'utilisateur, tout en conservant les valeurs ISO dans les echanges.
- Representer les statuts d'actif `ACTIVE`, `INACTIVE`, `RETIRED` et `DISPOSED` par un libelle et un signal visuel qui ne depend pas uniquement de la couleur.
- Interdire dans le formulaire l'affectation d'un actif non actif et signaler les chevauchements ou differences d'entreprise renvoyes par le backend.
- Ne pas permettre la saisie directe du carbone calcule lors de l'enregistrement d'une consommation.
- Ne jamais afficher directement les messages d'exception Java. Mapper les erreurs vers des messages utilisateur non enumerants et journaliser les details uniquement dans un contexte protege.
- Ne pas exposer des identifiants d'un autre tenant dans les URL, suggestions, caches ou journaux du navigateur.
- Preserver l'historique lors de la desactivation d'un employe; presenter l'action comme une desactivation et non comme une suppression.

## Criteres de completion

- Le contrat d'ecran et la source de donnees sont explicites.
- Le contrat API est accepte avant la creation de mocks ou de composants connectes.
- Les composants suivent la stack et les conventions deja etablies dans le depot.
- Les etats de chargement, vide, erreur et autorisation sont couverts.
- Le parcours fonctionne au clavier et conserve un focus visible.
- Aucun contenu ne se chevauche a 390 px, 768 px et 1440 px de largeur.
- Les valeurs, unites, dates, statuts et erreurs respectent les regles EcoTrack.
- Les controles automatises de la stack passent et le resultat visuel est verifie dans un navigateur.
