# Álbum de Figurinhas Digital — Copa do Mundo 2002

Aplicativo Android da disciplina de Programação para Dispositivos Móveis (FACOM32503 — UFU).
**Fase 2 (Desenvolvimento).** A modelagem está no `Relatorio_Fase1_PDM_UFU.pdf`.

## Stack

- **Kotlin** + **Jetpack Compose** (UI declarativa, Material 3)
- **MVVM + Clean Architecture** (camadas `ui` / `domain` / `data`)
- **Navigation Compose** (Single Activity, rotas com argumentos)
- **Coroutines + StateFlow** (fluxo unidirecional de dados)
- **Hilt** (injeção de dependências, processador **KSP**)
- **kotlinx.serialization** (parsing do JSON de dados)
- **Coil** (carregamento de imagens)

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

## Arquitetura (fonte de dados: mock-first)

O fluxo segue o diagrama da Fase 1:

```
UI (Compose) → ViewModel (StateFlow) → Repository (Single Source of Truth) → DataSource
```

Nesta entrega, o `CompetitionRepository` é implementado por `MockCompetitionRepository`,
que lê os dados de `app/src/main/assets/competition.json` (esquema JSON da Fase 1).
Como a UI depende **apenas da interface** `CompetitionRepository`, trocar a fonte
por **Firebase** ou **Retrofit** na próxima etapa exige mudar somente o binding em
`di/RepositoryModule.kt` — nenhuma tela ou ViewModel muda.

## Estrutura de pacotes

```
br.ufu.pdm.copa2002
├── AlbumApplication.kt        # @HiltAndroidApp
├── MainActivity.kt            # Single Activity + setContent
├── di/                        # Módulos Hilt (DataModule, RepositoryModule)
├── domain/
│   ├── model/                 # Entidades puras (Competition, Team, Coach, Player)
│   └── repository/            # CompetitionRepository (interface)
├── data/
│   ├── dto/                   # DTOs do JSON (@Serializable)
│   ├── mapper/                # DTO → domínio
│   ├── source/                # AssetCompetitionDataSource (lê o JSON)
│   └── repository/            # MockCompetitionRepository
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
| RF03 | Detalhes da equipe (comissão técnica + grid de jogadores + cor adaptativa) | ✅ TeamScreen |
| RF04 | Perfil do atleta/treinador (figurinha) | ✅ PlayerDetailScreen / CoachDetailScreen |
| RF05 | Giro 3D da figurinha (Card Flip no eixo Y) | ✅ FlipCard |
| RF06 | Sincronização remota de dados | ⏳ mock local (próxima etapa: Firebase/Retrofit) |

## Próximos passos sugeridos

- **RF06**: implementar `FirebaseCompetitionRepository` **ou** `RetrofitCompetitionRepository`
  e trocar o binding em `di/RepositoryModule.kt`.
- **Persistência (Room)**: adicionar cache local como fonte da verdade (offline-first).
- **Testes**: unitários das ViewModels (MockK + coroutines-test) e de UI (Compose Test).
- **Barra de status adaptativa** por cor da seleção (RF03) e imagens reais das figurinhas.
