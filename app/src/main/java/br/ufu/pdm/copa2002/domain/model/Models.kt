package br.ufu.pdm.copa2002.domain.model

/**
 * Entidades de domínio — modelos puros de negócio, independentes de framework.
 * A camada de dados mapeia os DTOs (JSON/rede) para estas entidades antes de
 * entregá-las às ViewModels (regra: o domínio não conhece a origem dos dados).
 */

data class Competition(
    val name: String,
    val edition: String,
    val year: String,
    val subtitle: String,
    val stadiumBackgroundUrl: String,
    val teams: List<Team>
)

data class Team(
    val id: String,
    val name: String,
    val group: String,
    val crestUrl: String,
    val victories: Int,
    val titleYears: List<String>,
    val finalResult: String,
    val matchesPlayed: Int,
    val primaryColor: String,
    val secondaryColor: String,
    val description: String,
    val coach: Coach,
    val players: List<Player>
)

data class Coach(
    val id: String,
    val name: String,
    val role: String,
    val photoUrl: String,
    val profile: String
)

data class Player(
    val id: String,
    val name: String,
    val position: String,
    val number: Int,
    val photoUrl: String,
    val biography: String,
    val goals: Int,
    val matches: Int,
    val assists: Int
)
