package br.ufu.pdm.copa2002.data.repository

import br.ufu.pdm.copa2002.data.mapper.toDomain
import br.ufu.pdm.copa2002.data.source.AssetCompetitionDataSource
import br.ufu.pdm.copa2002.domain.model.Coach
import br.ufu.pdm.copa2002.domain.model.Competition
import br.ufu.pdm.copa2002.domain.model.Player
import br.ufu.pdm.copa2002.domain.model.Team
import br.ufu.pdm.copa2002.domain.repository.CompetitionRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementação mock do [CompetitionRepository]. Carrega os dados do JSON local
 * uma única vez e os mantém em cache de memória (Single Source of Truth).
 *
 * O pequeno [delay] simula latência de rede apenas para exercitar o estado de
 * carregamento da UI — remover ao integrar a fonte remota real.
 */
@Singleton
class MockCompetitionRepository @Inject constructor(
    private val dataSource: AssetCompetitionDataSource
) : CompetitionRepository {

    private val mutex = Mutex()
    private var cache: Competition? = null

    private suspend fun competition(): Competition = mutex.withLock {
        cache ?: dataSource.loadCompetition().toDomain().also { cache = it }
    }

    override suspend fun getCompetition(): Competition {
        delay(SIMULATED_LATENCY_MS)
        return competition()
    }

    override suspend fun getTeam(teamId: String): Team? {
        delay(SIMULATED_LATENCY_MS)
        return competition().teams.find { it.id == teamId }
    }

    override suspend fun getPlayer(playerId: String): Player? {
        delay(SIMULATED_LATENCY_MS)
        return competition().teams
            .flatMap { it.players }
            .find { it.id == playerId }
    }

    override suspend fun getCoach(coachId: String): Coach? {
        delay(SIMULATED_LATENCY_MS)
        return competition().teams
            .map { it.coach }
            .find { it.id == coachId }
    }

    private companion object {
        const val SIMULATED_LATENCY_MS = 350L
    }
}
