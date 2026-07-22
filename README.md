# Álbum de Figurinhas Digital — Copa do Mundo 2002

Aplicativo Android da disciplina de Programação para Dispositivos Móveis (FACOM32503 — UFU).
**Fase 2 (Desenvolvimento).** A modelagem está no `Relatorio_Fase1_PDM_UFU.pdf`.

## Stack

- **Kotlin** + **Jetpack Compose** (UI declarativa, Material 3)
- **MVVM + Clean Architecture** (camadas `ui` / `domain` / `data`)
- **Navigation Compose** (Single Activity, rotas com argumentos)
- **Coroutines + StateFlow** (fluxo unidirecional de dados)
- **Hilt** (injeção de dependências, processador **KSP**)
- **Room** (persistência local / cache offline-first, processador **KSP**)
- **kotlinx.serialization** (parsing do JSON de dados)
- **Coil** (carregamento de imagens)
- **Testes**: JUnit 4 + MockK + coroutines-test (unitários) · Room in-memory + AndroidJUnit4 (instrumentados)

## Como abrir e rodar

1. Abra a pasta `album-copa-2002/` no **Android Studio** (Ladybug ou mais recente).
2. Aguarde o **Gradle Sync**. O Android Studio gera automaticamente o
   `local.properties` (caminho do SDK) e o **Gradle Wrapper** (`gradlew` +
   `gradle-wrapper.jar`), que não são versionados.
   - Alternativa por linha de comando (se tiver o Gradle instalado):
     ```bash
     gradle wrapper
     ./gradlew assembleDebug
     ```
3. Rode em um emulador ou dispositivo (**minSdk 24**).

> Se o build reclamar do SDK, crie `local.properties` na raiz com:
> `sdk.dir=/caminho/para/Android/sdk`

## Arquitetura (offline-first)

O fluxo segue o diagrama da Fase 1:

```
UI (Compose) → ViewModel (StateFlow) → Repository (Single Source of Truth)
                                          ├── Room (cache local reativo)  ← observado pela UI
                                          └── fonte remota (refresh)      ← hoje o JSON local
```

O `CompetitionRepository` é implementado por `RoomCompetitionRepository`: a UI
**observa o cache local do Room** (`observeCompetition()`, reativo) e o ViewModel
dispara `refresh()`, que lê a **fonte remota** e regrava o Room (Single Source of
Truth). Hoje a "fonte remota" é `app/src/main/assets/competition.json`; para o
RF06 basta trocá-la pelo Firestore — **nenhuma tela, ViewModel ou o Room mudam**
(ver [`FIREBASE_SETUP.md`](FIREBASE_SETUP.md)).

## Estrutura de pacotes

```
br.ufu.pdm.copa2002
├── AlbumApplication.kt        # @HiltAndroidApp
├── MainActivity.kt            # Single Activity + setContent
├── di/                        # Módulos Hilt (Data, Database, Repository)
├── domain/
│   ├── model/                 # Entidades puras (Competition, Team, Coach, Player)
│   └── repository/            # CompetitionRepository (interface)
├── data/
│   ├── dto/                   # DTOs do JSON (@Serializable)
│   ├── local/                 # Room: entities, DAO, AppDatabase, Converters
│   ├── mapper/                # DTO → entidade → domínio
│   ├── source/                # AssetCompetitionDataSource (fonte remota mock)
│   └── repository/            # RoomCompetitionRepository (offline-first)
└── ui/
    ├── theme/                 # Cores/tipografia (paleta da Fase 1)
    ├── navigation/            # Screen (rotas) + AppNavigation (NavHost)
    ├── common/                # UiState (Loading/Success/Error)
    ├── components/            # AvatarImage (imagem com fallback)
    ├── competition/           # CompetitionScreen + ViewModel
    ├── team/                  # TeamScreen + ViewModel
    ├── player/                # PlayerDetailScreen (card flip 3D) + ViewModel
    └── coach/                 # CoachDetailScreen + ViewModel
```

## Requisitos funcionais cobertos

| RF | Descrição | Status |
|----|-----------|--------|
| RF01 | Tela inicial da competição + lista de seleções | ✅ CompetitionScreen |
| RF02 | Lista de seleções (brasão + títulos) | ✅ |
| RF03 | Detalhes da equipe (comissão técnica + grid + barra de status adaptativa) | ✅ TeamScreen |
| RF04 | Perfil do atleta/treinador (figurinha) | ✅ PlayerDetailScreen / CoachDetailScreen |
| RF05 | Giro 3D da figurinha (Card Flip no eixo Y) | ✅ FlipCard |
| RF06 | Sincronização remota de dados | ⏳ offline-first pronto; falta plugar o Firestore (ver FIREBASE_SETUP.md) |

Requisitos não funcionais: RNF01 (Compose) · RNF02 (MVVM) · RNF03 (UDF/StateFlow) ·
RNF04 (Git/GitHub) · **RNF05 (testabilidade)** ✅ testes unitários + instrumentados.

## Testes

```bash
./gradlew testDebugUnitTest        # unitários (JVM): ViewModel + mappers
./gradlew connectedDebugAndroidTest # instrumentados (device/emulador): DAO Room
```

## Próximos passos sugeridos

- **RF06**: plugar o Firestore como fonte remota — passo a passo em [`FIREBASE_SETUP.md`](FIREBASE_SETUP.md)
  (depende de criar o projeto no Firebase + `google-services.json`).
- **Imagens reais** das figurinhas/brasões (hoje o `AvatarImage` usa fallback com iniciais).
- **Notificações** (ponto extra da 2ª entrega) e publicação (AAB).
