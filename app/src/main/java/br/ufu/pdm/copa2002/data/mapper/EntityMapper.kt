package br.ufu.pdm.copa2002.data.mapper

import br.ufu.pdm.copa2002.data.dto.CompetitionDto
import br.ufu.pdm.copa2002.data.local.TeamWithDetails
import br.ufu.pdm.copa2002.data.local.entity.CoachEntity
import br.ufu.pdm.copa2002.data.local.entity.CompetitionMetaEntity
import br.ufu.pdm.copa2002.data.local.entity.PlayerEntity
import br.ufu.pdm.copa2002.data.local.entity.TeamEntity
import br.ufu.pdm.copa2002.domain.model.Coach
import br.ufu.pdm.copa2002.domain.model.Competition
import br.ufu.pdm.copa2002.domain.model.Player
import br.ufu.pdm.copa2002.domain.model.Team

// ---- DTO (fonte remota/JSON) -> Entidades (Room), para o seed/refresh ----

fun CompetitionDto.toMetaEntity(): CompetitionMetaEntity = CompetitionMetaEntity(
    name = competition,
    edition = edition,
    year = year,
    subtitle = subtitle,
    stadiumBackgroundUrl = stadiumBackgroundUrl
)

fun CompetitionDto.toTeamEntities(): List<TeamEntity> = teams.map { t ->
    TeamEntity(
        id = t.id,
        name = t.name,
        group = t.group,
        crestUrl = t.crestUrl,
        victories = t.victories,
        titleYears = t.titleYears,
        finalResult = t.finalResult,
        matchesPlayed = t.matchesPlayed,
        primaryColor = t.primaryColor,
        secondaryColor = t.secondaryColor,
        description = t.description
    )
}

fun CompetitionDto.toCoachEntities(): List<CoachEntity> = teams.map { t ->
    CoachEntity(
        id = t.coach.id,
        teamId = t.id,
        name = t.coach.name,
        role = t.coach.role,
        photoUrl = t.coach.photoUrl,
        profile = t.coach.profile
    )
}

fun CompetitionDto.toPlayerEntities(): List<PlayerEntity> = teams.flatMap { t ->
    t.players.map { p ->
        PlayerEntity(
            id = p.id,
            teamId = t.id,
            name = p.name,
            position = p.position,
            number = p.number,
            photoUrl = p.photoUrl,
            biography = p.biography,
            goals = p.goals,
            matches = p.matches,
            assists = p.assists
        )
    }
}

// ---- Entidades (Room) -> Domínio ----

fun CoachEntity.toDomain(): Coach = Coach(
    id = id,
    name = name,
    role = role,
    photoUrl = photoUrl,
    profile = profile
)

fun PlayerEntity.toDomain(): Player = Player(
    id = id,
    name = name,
    position = position,
    number = number,
    photoUrl = photoUrl,
    biography = biography,
    goals = goals,
    matches = matches,
    assists = assists
)

private val EMPTY_COACH = Coach(id = "", name = "", role = "", photoUrl = "", profile = "")

fun TeamWithDetails.toDomain(): Team = Team(
    id = team.id,
    name = team.name,
    group = team.group,
    crestUrl = team.crestUrl,
    victories = team.victories,
    titleYears = team.titleYears,
    finalResult = team.finalResult,
    matchesPlayed = team.matchesPlayed,
    primaryColor = team.primaryColor,
    secondaryColor = team.secondaryColor,
    description = team.description,
    coach = coach?.toDomain() ?: EMPTY_COACH,
    players = players.map { it.toDomain() }
)

fun buildCompetition(
    meta: CompetitionMetaEntity,
    teams: List<TeamWithDetails>
): Competition = Competition(
    name = meta.name,
    edition = meta.edition,
    year = meta.year,
    subtitle = meta.subtitle,
    stadiumBackgroundUrl = meta.stadiumBackgroundUrl,
    teams = teams.map { it.toDomain() }
)
