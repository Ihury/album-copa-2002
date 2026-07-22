package br.ufu.pdm.copa2002.data.source

import android.content.Context
import br.ufu.pdm.copa2002.data.dto.CompetitionDto
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import javax.inject.Inject

/**
 * Fonte de dados local (mock). Lê o arquivo `assets/competition.json` e o
 * desserializa para DTO. A leitura de I/O roda em Dispatchers.IO (main-safe).
 *
 * Na próxima etapa, esta fonte convive com uma RemoteDataSource (Retrofit/Firebase)
 * e um cache (Room) sem que o Repository exponha essa troca às camadas superiores.
 */
class AssetCompetitionDataSource @Inject constructor(
    @ApplicationContext private val context: Context,
    private val json: Json
) {
    suspend fun loadCompetition(): CompetitionDto = withContext(Dispatchers.IO) {
        val text = context.assets.open(FILE_NAME)
            .bufferedReader()
            .use { it.readText() }
        json.decodeFromString(CompetitionDto.serializer(), text)
    }

    private companion object {
        const val FILE_NAME = "competition.json"
    }
}
