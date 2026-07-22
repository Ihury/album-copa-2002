package br.ufu.pdm.copa2002.domain.repository

import br.ufu.pdm.copa2002.domain.model.Coach
import br.ufu.pdm.copa2002.domain.model.Competition
import br.ufu.pdm.copa2002.domain.model.Player
import br.ufu.pdm.copa2002.domain.model.Team

/**
 * Contrato da fonte de dados (Single Source of Truth). A UI/ViewModel dependem
 * apenas desta interface; a implementação concreta (mock, Firebase, Retrofit)
 * é injetada pelo Hilt e pode ser trocada sem alterar as camadas superiores.
 */
interface CompetitionRepository {
    suspend fun getCompetition(): Competition
    suspend fun getTeam(teamId: String): Team?
    suspend fun getPlayer(playerId: String): Player?
    suspend fun getCoach(coachId: String): Coach?
}
