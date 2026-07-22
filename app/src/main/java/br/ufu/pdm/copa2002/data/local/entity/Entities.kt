package br.ufu.pdm.copa2002.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entidades do Room (persistência local). O modelo aninhado do domínio é
 * "achatado" em tabelas relacionais: metadados da competição, seleções,
 * treinadores e jogadores, ligados por chaves estrangeiras lógicas (teamId).
 */

@Entity(tableName = "competition_meta")
data class CompetitionMetaEntity(
    @PrimaryKey val id: Int = SINGLE_ROW_ID,
    val name: String,
    val edition: String,
    val year: String,
    val subtitle: String,
    val stadiumBackgroundUrl: String
) {
    companion object {
        const val SINGLE_ROW_ID = 1
    }
}

@Entity(tableName = "teams")
data class TeamEntity(
    @PrimaryKey val id: String,
    val name: String,
    val group: String,
    val crestUrl: String,
    val victories: Int,
    val titleYears: List<String>,
    val finalResult: String,
    val matchesPlayed: Int,
    val primaryColor: String,
    val secondaryColor: String,
    val description: String
)

@Entity(
    tableName = "coaches",
    indices = [Index("teamId")]
)
data class CoachEntity(
    @PrimaryKey val id: String,
    val teamId: String,
    val name: String,
    val role: String,
    val photoUrl: String,
    val profile: String
)

@Entity(
    tableName = "players",
    indices = [Index("teamId")]
)
data class PlayerEntity(
    @PrimaryKey val id: String,
    val teamId: String,
    val name: String,
    val position: String,
    val number: Int,
    val photoUrl: String,
    val biography: String,
    val goals: Int,
    val matches: Int,
    val assists: Int
)
