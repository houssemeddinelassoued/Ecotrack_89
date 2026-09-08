# Checklist qualite frontend EcoTrack

## Comportement

- Le parcours nominal et les mutations correspondent aux criteres d'acceptation.
- Les etats chargement, vide, erreur, partiel et interdit sont visibles et testables.
- Une action soumise ne peut pas etre declenchee deux fois pendant son traitement.
- Les filtres et la pagination restent coherents avec l'URL lorsque le partage de vue est utile.
- Les erreurs de conflit et validation sont placees pres de l'action ou du champ concerne.

## Donnees

- Les decimaux provenant de l'API ne subissent pas de conversion silencieuse en nombre flottant.
- Les dates sans heure restent des dates civiles; les instants sont affiches dans le fuseau choisi.
- Le carbone calcule est en lecture seule.
- Les caches et requetes sont isoles par entreprise authentifiee.
- Aucun message interne, secret ou identifiant hors tenant n'est affiche ou journalise.

## Accessibilite

- Toutes les fonctions sont accessibles au clavier dans un ordre logique.
- Le focus est visible, restaure apres fermeture d'un dialogue et place sur les erreurs utiles.
- Les champs ont des libelles persistants et les erreurs leur sont associees.
- Les tableaux possedent des en-tetes corrects et une alternative lisible sur petit ecran.
- Les statuts ne reposent pas uniquement sur la couleur.
- Les animations respectent `prefers-reduced-motion`.

## Responsive et visuel

- Verifier au minimum 390 x 844, 768 x 1024 et 1440 x 900.
- Aucun texte, bouton, tableau ou dialogue ne deborde ou ne masque une autre action.
- Les dimensions des controles et indicateurs restent stables pendant le chargement.
- La densite convient a un outil metier utilise de facon repetee.
- Les composants reutilisent les tokens et motifs deja approuves.

## Validation technique

- Formatage, lint et verification de types passent.
- Les tests unitaires et de composants passent.
- Les parcours critiques sont couverts par des tests navigateur lorsque l'infrastructure existe.
- Le build de production reussit sans avertissement introduit par la modification.
- La console du navigateur ne contient aucune erreur pendant les parcours verifies.
