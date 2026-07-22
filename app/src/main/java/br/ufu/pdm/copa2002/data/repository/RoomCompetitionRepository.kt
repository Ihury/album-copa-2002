package br.ufu.pdm.copa2002.data.repository

import br.ufu.pdm.copa2002.data.local.CompetitionDao
import br.ufu.pdm.copa2002.data.mapper.buildCompetition
import br.ufu.pdm.copa2002.data.mapper.toCoachEntities
import br.ufu.pdm.copa2002.data.mapper.toDomain
import br.ufu.pdm.copa2002.data.mapper.toMetaEntity
import br.ufu.pdm.copa2002.data.mapper.toPlayerEntities
import br.ufu.pdm.copa2002.data.mapper.toTeamEntities
import br.ufu.pdm.copa2002.data.source.AssetCompetitionDataSource
import br.ufu.pdm.copa2002.domain.model.Coach
import br.ufu.pdm.copa2002.domain.model.Competition
import br.ufu.pdm.copa2002.domain.model.Player
import br.ufu.pdm.copa2002.domain.model.Team
import br.ufu.pdm.copa2002.domain.repository.CompetitionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementação offline-first do [CompetitionRepository].
 *
 * - O Room é a Single Source of Truth: a UI observa o cache local (reativo).
 * - [refresh] busca na fonte remota ([remoteSource]) e regrava o cache.
 *
 * Nesta etapa a "fonte remota" é o JSON local ([AssetCompetitionDataSource]).
 * Para o RF06, basta trocar essa dependência por uma fonte Firebase/Retrofit —
 * o restante do repositório e da UI permanece igual. Ver FIREBASE_SETUP.md.
 */
@Singleton
class RoomCompetitionRepository @Inject constructor(
    private val dao: CompetitionDao,
    private val remoteSource: AssetCompetitionDataSource
) : CompetitionRepository {

    override fun observeCompetition(): Flow<Competition?> =
        combine(dao.observeMeta(), dao.observeTeamsWithDetails()) { meta, teams ->
            meta?.let { buildCompetition(it, teams) }
        }

    override suspend fun refresh() {
        val dto = remoteSource.loadCompetition()
        dao.replaceAll(
            meta = dto.toMetaEntity(),
            teams = dto.toTeamEntities(),
            coaches = dto.toCoachEntities(),
            players = dto.toPlayerEntities()
        )
    }

    /** Garante que o cache foi populado ao menos uma vez (para deep-links diretos). */
    private suspend fun ensureSeeded() {
        if (dao.teamCount() == 0) refresh()
    }

    override suspend fun getTeam(teamId: String): Team? {
        ensureSeeded()
        return dao.getTeamWithDetails(teamId)?.toDomain()
    }

    override suspend fun getPlayer(playerId: String): Player? {
        ensureSeeded()
        return dao.getPlayer(playerId)?.toDomain()
    }

    override suspend fun getCoach(coachId: String): Coach? {
        ensureSeeded()
        return dao.getCoach(coachId)?.toDomain()
    }
}
