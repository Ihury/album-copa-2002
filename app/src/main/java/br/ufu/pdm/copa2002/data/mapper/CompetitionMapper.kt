package br.ufu.pdm.copa2002.data.mapper

import br.ufu.pdm.copa2002.data.dto.CoachDto
import br.ufu.pdm.copa2002.data.dto.CompetitionDto
import br.ufu.pdm.copa2002.data.dto.PlayerDto
import br.ufu.pdm.copa2002.data.dto.TeamDto
import br.ufu.pdm.copa2002.domain.model.Coach
import br.ufu.pdm.copa2002.domain.model.Competition
import br.ufu.pdm.copa2002.domain.model.Player
import br.ufu.pdm.copa2002.domain.model.Team

// Mapeadores DTO -> Domínio. Isolam a UI de mudanças no formato da fonte de dados.

fun CompetitionDto.toDomain(): Competition = Competition(
    name = competition,
    edition = edition,
    year = year,
    subtitle = subtitle,
    stadiumBackgroundUrl = stadiumBackgroundUrl,
    teams = teams.map { it.toDomain() }
)

fun TeamDto.toDomain(): Team = Team(
    id = id,
    name = name,
    group = group,
    crestUrl = crestUrl,
    victories = victories,
    titleYears = titleYears,
    finalResult = finalResult,
    matchesPlayed = matchesPlayed,
    primaryColor = primaryColor,
    secondaryColor = secondaryColor,
    description = description,
    coach = coach.toDomain(),
    players = players.map { it.toDomain() }
)

fun CoachDto.toDomain(): Coach = Coach(
    id = id,
    name = name,
    role = role,
    photoUrl = photoUrl,
    profile = profile
)

fun PlayerDto.toDomain(): Player = Player(
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
