# kotlin-showcase

Petits exemples de Kotlin idiomatique, chacun lisible en une minute et couvert par un test.

| Fichier | Ce qu'il montre |
|---|---|
| [`Result.kt`](src/main/kotlin/fr/cindano/showcase/Result.kt) | `sealed interface`, `when` exhaustif, erreurs comme valeurs |
| [`Money.kt`](src/main/kotlin/fr/cindano/showcase/Money.kt) | `value class`, opérateurs surchargés, propriété d'extension |
| [`OrderDsl.kt`](src/main/kotlin/fr/cindano/showcase/OrderDsl.kt) | DSL typé avec `@DslMarker` et lambda avec receveur |
| [`Pipeline.kt`](src/main/kotlin/fr/cindano/showcase/Pipeline.kt) | coroutines : concurrence bornée par `Semaphore`, `Flow` froid |

Le test de concurrence tourne en temps virtuel (`kotlinx-coroutines-test`) : il vérifie l'ordre, la limite de parallélisme et la durée totale sans attendre pour de vrai.

```bash
./gradlew test
```

Voir aussi [sport-discovery-api](https://github.com/snooker4real/sport-discovery-api), une API Kotlin / Spring Boot avec JPA, Flyway et un moteur de recommandations.
