package br.ufu.pdm.copa2002.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * DTOs — espelham o esquema JSON (contrato de dados definido na Fase 1).
 * @SerialName mapeia o snake_case do JSON para camelCase do Kotlin.
 * Os valores padrão tornam o parsing resiliente a campos ausentes.
 */

@Serializable
data class CompetitionDto(
    val competition: String,
    val edition: String,
    val year: String = "",
    val subtitle: String = "",
    @SerialName("stadium_background_url") val stadiumBackgroundUrl: String = "",
    val teams: List<TeamDto> = emptyList()
)

@Serializable
data class TeamDto(
    val id: String,
    val name: String,
    val group: String = "",
    @SerialName("crest_url") val crestUrl: String = "",
    val victories: Int = 0,
    @SerialName("title_years") val titleYears: List<String> = emptyList(),
    @SerialName("final_result") val finalResult: String = "",
    @SerialName("matches_played") val matchesPlayed: Int = 0,
    @SerialName("primary_color") val primaryColor: String = "#1E1E1E",
    @SerialName("secondary_color") val secondaryColor: String = "#121212",
    val description: String = "",
    val coach: CoachDto,
    val players: List<PlayerDto> = emptyList()
)

@Serializable
data class CoachDto(
    val id: String,
    val name: String,
    val role: String = "",
    @SerialName("photo_url") val photoUrl: String = "",
    val profile: String = ""
)

@Serializable
data class PlayerDto(
    val id: String,
    val name: String,
    val position: String = "",
    val number: Int = 0,
    @SerialName("photo_url") val photoUrl: String = "",
    val biography: String = "",
    val goals: Int = 0,
    val matches: Int = 0,
    val assists: Int = 0
)
