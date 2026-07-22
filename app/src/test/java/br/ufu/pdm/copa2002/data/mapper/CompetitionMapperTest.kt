package br.ufu.pdm.copa2002.data.mapper

import br.ufu.pdm.copa2002.data.dto.CoachDto
import br.ufu.pdm.copa2002.data.dto.CompetitionDto
import br.ufu.pdm.copa2002.data.dto.PlayerDto
import br.ufu.pdm.copa2002.data.dto.TeamDto
import br.ufu.pdm.copa2002.data.local.TeamWithDetails
import org.junit.Assert.assertEquals
import org.junit.Test

class CompetitionMapperTest {

    private val dto = CompetitionDto(
        competition = "FIFA World Cup",
        edition = "Korea · Japan",
        year = "2002",
        subtitle = "Selecione uma nação",
        stadiumBackgroundUrl = "u",
        teams = listOf(
            TeamDto(
                id = "team_bra",
                name = "Brasil",
                group = "Grupo C",
                crestUrl = "c",
                victories = 5,
                titleYears = listOf("1970", "2002"),
                finalResult = "Campeão",
                matchesPlayed = 7,
                primaryColor = "#FFDF00",
                secondaryColor = "#009C3B",
                description = "d",
                coach = CoachDto(id = "coach_bra", name = "Felipão", role = "Técnico"),
                players = listOf(
                    PlayerDto(id = "p1", name = "Ronaldo", position = "Centroavante", number = 9, goals = 8)
                )
            )
        )
    )

    @Test
    fun `mapeia metadados da competicao`() {
        val meta = dto.toMetaEntity()
        assertEquals("FIFA World Cup", meta.name)
        assertEquals("2002", meta.year)
    }

    @Test
    fun `associa coach e players ao teamId correto`() {
        assertEquals(1, dto.toTeamEntities().size)
        assertEquals("team_bra", dto.toCoachEntities().first().teamId)
        val players = dto.toPlayerEntities()
        assertEquals("team_bra", players.first().teamId)
        assertEquals(9, players.first().number)
    }

    @Test
    fun `TeamWithDetails converte para dominio com coach e players`() {
        val twd = TeamWithDetails(
            team = dto.toTeamEntities().first(),
            coach = dto.toCoachEntities().first(),
            players = dto.toPlayerEntities()
        )
        val team = twd.toDomain()
        assertEquals("Brasil", team.name)
        assertEquals("Felipão", team.coach.name)
        assertEquals(1, team.players.size)
        assertEquals(listOf("1970", "2002"), team.titleYears)
    }
}
