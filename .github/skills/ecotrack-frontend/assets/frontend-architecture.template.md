# Architecture frontend EcoTrack

## Decisions

| Sujet | Choix | Justification |
|---|---|---|
| Framework et version | A definir pour l'implementation | Les ressources du skill restent neutres |
| Emplacement | A definir | Monorepo ou depot separe |
| Build et paquets | A definir | |
| Routage | A definir | |
| Etat serveur | Contrat API d'abord | Aucun composant connecte avant acceptation du contrat |
| Formulaires | A definir | |
| Styles et composants | A definir | |
| Icones | A definir | |
| Tests | A definir | |
| Authentification | A definir | |
| Internationalisation | A definir | |

## Frontieres

- Le navigateur communique avec une API authentifiee; il n'accede jamais directement a PostgreSQL ou aux services Java en memoire.
- Le contrat OpenAPI est defini et accepte avant la creation de mocks, clients generes ou composants connectes.
- Les donnees serveur restent dans une couche client dediee et typee.
- Les regles de presentation et de formatage ne sont pas placees dans les composants de transport.
- Le contexte d'entreprise provient de l'identite authentifiee et ne repose pas uniquement sur un identifiant fourni par le navigateur.

## Organisation proposee

```text
src/
  app/             # demarrage, routage, fournisseurs globaux
  api/             # client HTTP, DTO, mapping et erreurs
  features/        # dashboard, assets, employees, assignments, consumptions
  components/      # composants partages sans logique metier specifique
  styles/          # tokens, styles globaux et utilitaires approuves
  test/            # fixtures, handlers de mock et setup
```

Adapter cette structure aux conventions natives du framework retenu. Ne pas creer une couche partagee avant qu'au moins deux usages reels la justifient.

## Routes minimales candidates

| Route | Responsabilite |
|---|---|
| `/` | Vue d'ensemble energie et carbone |
| `/assets` | Recherche, filtres et statut des actifs |
| `/employees` | Employes, activite et actifs affectes |
| `/assignments` | Affectations actives et historique |
| `/consumptions` | Saisie et historique quotidien |

## Contrat d'erreur attendu

Definir avec l'API des codes stables, par exemple `VALIDATION_ERROR`, `CONFLICT`, `FORBIDDEN` et `NOT_FOUND`. Le frontend choisit le message affiche a partir du code et ne montre pas le detail interne du serveur.
