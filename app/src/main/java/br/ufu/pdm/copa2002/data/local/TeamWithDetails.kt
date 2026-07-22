package br.ufu.pdm.copa2002.data.local

import androidx.room.Embedded
import androidx.room.Relation
import br.ufu.pdm.copa2002.data.local.entity.CoachEntity
import br.ufu.pdm.copa2002.data.local.entity.PlayerEntity
import br.ufu.pdm.copa2002.data.local.entity.TeamEntity

/**
 * Consulta relacional: uma seleção com seu treinador e seus jogadores.
 * O Room resolve os @Relation automaticamente a partir das tabelas.
 */
data class TeamWithDetails(
    @Embedded val team: TeamEntity,
    @Relation(parentColumn = "id", entityColumn = "teamId")
    val coach: CoachEntity?,
    @Relation(parentColumn = "id", entityColumn = "teamId")
    val players: List<PlayerEntity>
)
