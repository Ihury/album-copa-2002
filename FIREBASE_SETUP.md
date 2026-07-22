# RF06 — Ativar a fonte de dados remota (Firebase Firestore)

O app já está **offline-first**: a UI observa o cache local (Room) e chama
`refresh()` para atualizar a partir de uma **fonte remota**. Hoje essa fonte é o
JSON local (`AssetCompetitionDataSource`). Para cumprir o RF06, basta substituir
essa fonte pelo Firestore — **nenhuma tela, ViewModel ou o Room mudam**.

> ⚠️ Estes passos exigem uma **conta Google/Firebase** (login no console,
> `google-services.json`, popular o Firestore). Por isso ficam como guia, e não
> aplicados no build — assim o projeto continua compilando sem a conta.

## 1. Criar o projeto no Firebase
1. https://console.firebase.google.com → **Add project**.
2. **Add app → Android**, package name **`br.ufu.pdm.copa2002`**.
3. Baixe o **`google-services.json`** e coloque em **`app/google-services.json`**
   (já está no `.gitignore`? adicione `app/google-services.json` se for privado).

## 2. Gradle
`gradle/libs.versions.toml` (adicionar):
```toml
[versions]
googleServices = "4.4.2"
firebaseBom = "33.1.2"
coroutinesPlayServices = "1.8.1"

[libraries]
firebase-bom = { group = "com.google.firebase", name = "firebase-bom", version.ref = "firebaseBom" }
firebase-firestore = { group = "com.google.firebase", name = "firebase-firestore-ktx" }
kotlinx-coroutines-play-services = { group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-play-services", version.ref = "coroutinesPlayServices" }

[plugins]
google-services = { id = "com.google.gms.google-services", version.ref = "googleServices" }
```
`build.gradle.kts` (raiz): `alias(libs.plugins.google.services) apply false`
`app/build.gradle.kts`:
```kotlin
plugins { /* ... */ alias(libs.plugins.google.services) }
dependencies {
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.firestore)
    implementation(libs.kotlinx.coroutines.play.services)
}
```

## 3. Estrutura no Firestore
Coleção **`teams`** (um documento por seleção), espelhando o JSON atual — mais um
documento de metadados em **`competition/meta`**:
```
competition (coleção)
  meta (doc): { competition, edition, year, subtitle, stadium_background_url }
teams (coleção)
  team_bra (doc): { name, group, crest_url, victories, title_years: [...],
                    final_result, matches_played, primary_color, secondary_color,
                    description, coach: {...}, players: [ {...}, ... ] }
```
Dá para popular manualmente pelo console, ou importar o `app/src/main/assets/competition.json`.

## 4. Nova fonte remota (código pronto)
Crie `data/source/RemoteCompetitionDataSource.kt` (interface) e faça o
`AssetCompetitionDataSource` implementá-la, para o Room depender da abstração:
```kotlin
interface RemoteCompetitionDataSource {
    suspend fun loadCompetition(): CompetitionDto
}
```
`data/source/FirebaseCompetitionDataSource.kt`:
```kotlin
class FirebaseCompetitionDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) : RemoteCompetitionDataSource {
    override suspend fun loadCompetition(): CompetitionDto {
        val meta = firestore.collection("competition").document("meta").get().await()
        val teamsSnap = firestore.collection("teams").get().await()
        val teams = teamsSnap.documents.map { doc ->
            TeamDto(
                id = doc.id,
                name = doc.getString("name") ?: "",
                group = doc.getString("group") ?: "",
                crestUrl = doc.getString("crest_url") ?: "",
                victories = (doc.getLong("victories") ?: 0).toInt(),
                titleYears = (doc.get("title_years") as? List<String>).orEmpty(),
                finalResult = doc.getString("final_result") ?: "",
                matchesPlayed = (doc.getLong("matches_played") ?: 0).toInt(),
                primaryColor = doc.getString("primary_color") ?: "#1E1E1E",
                secondaryColor = doc.getString("secondary_color") ?: "#121212",
                description = doc.getString("description") ?: "",
                coach = /* mapear doc.get("coach") -> CoachDto */ TODO(),
                players = /* mapear doc.get("players") -> List<PlayerDto> */ TODO()
            )
        }
        return CompetitionDto(
            competition = meta.getString("competition") ?: "",
            edition = meta.getString("edition") ?: "",
            year = meta.getString("year") ?: "",
            subtitle = meta.getString("subtitle") ?: "",
            stadiumBackgroundUrl = meta.getString("stadium_background_url") ?: "",
            teams = teams
        )
    }
}
```
> Alternativa mais simples: modelar os documentos com nomes de campo iguais aos
> DTOs (`@PropertyName`) e usar `doc.toObject(TeamDto::class.java)`.

## 5. Injeção (Hilt)
`di/FirebaseModule.kt`:
```kotlin
@Module @InstallIn(SingletonComponent::class)
object FirebaseModule {
    @Provides @Singleton
    fun provideFirestore(): FirebaseFirestore = Firebase.firestore
}
```
E ligue a fonte remota à implementação do Firebase (troca de 1 binding):
```kotlin
@Binds @Singleton
abstract fun bindRemoteDataSource(impl: FirebaseCompetitionDataSource): RemoteCompetitionDataSource
```
Ajuste `RoomCompetitionRepository` para receber `RemoteCompetitionDataSource` no
lugar de `AssetCompetitionDataSource`. Pronto: a UI passa a ler do Firestore,
com o Room como cache offline. Para manter o modo offline de exemplo, mantenha o
`AssetCompetitionDataSource` como fallback.

## 6. Regras de segurança (leitura pública para o app)
```
rules_version = '2';
service cloud.firestore {
  match /databases/{db}/documents {
    match /{document=**} { allow read: if true; allow write: if false; }
  }
}
```
(Leitura liberada só para consumo do app; escrita bloqueada.)
