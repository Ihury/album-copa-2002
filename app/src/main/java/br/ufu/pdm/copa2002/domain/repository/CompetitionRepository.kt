package br.ufu.pdm.copa2002.domain.repository

import br.ufu.pdm.copa2002.domain.model.Coach
import br.ufu.pdm.copa2002.domain.model.Competition
import br.ufu.pdm.copa2002.domain.model.Player
import br.ufu.pdm.copa2002.domain.model.Team
import kotlinx.coroutines.flow.Flow

/**
 * Contrato da fonte de dados (Single Source of Truth), offline-first.
 *
 * A UI observa [observeCompetition] (cache local reativo via Room) e dispara
 * [refresh] para atualizar o cache a partir da fonte remota. As telas de
 * detalhe leem do cache local. A implementação concreta é injetada pelo Hilt
 * e pode ser trocada (mock/Firebase/Retrofit) sem afetar as camadas superiores.
 */
interface CompetitionRepository {

    /** Emite a competição a partir do cache local; null quando ainda não há dados. */
    fun observeCompetition(): Flow<Competition?>

    /** Busca os dados na fonte remota e regrava o cache local (Single Source of Truth). */
    suspend fun refresh()

    suspend fun getTeam(teamId: String): Team?
    suspend fun getPlayer(playerId: String): Player?
    suspend fun getCoach(coachId: String): Coach?
}
