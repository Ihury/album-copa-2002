package br.ufu.pdm.copa2002.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.ufu.pdm.copa2002.data.local.entity.CoachEntity
import br.ufu.pdm.copa2002.data.local.entity.CompetitionMetaEntity
import br.ufu.pdm.copa2002.data.local.entity.PlayerEntity
import br.ufu.pdm.copa2002.data.local.entity.TeamEntity
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CompetitionDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var dao: CompetitionDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.competitionDao()
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun insereEConsultaTeamComDetalhes() = runTest {
        // Arrange
        dao.upsertMeta(
            CompetitionMetaEntity(
                name = "FIFA World Cup", edition = "Korea · Japan",
                year = "2002", subtitle = "", stadiumBackgroundUrl = ""
            )
        )
        dao.upsertTeams(
            listOf(
                TeamEntity(
                    id = "team_bra", name = "Brasil", group = "Grupo C", crestUrl = "",
                    victories = 5, titleYears = listOf("2002"), finalResult = "Campeão",
                    matchesPlayed = 7, primaryColor = "#FFDF00", secondaryColor = "#009C3B",
                    description = ""
                )
            )
        )
        dao.upsertCoaches(
            listOf(CoachEntity(id = "c1", teamId = "team_bra", name = "Felipão", role = "Técnico", photoUrl = "", profile = ""))
        )
        dao.upsertPlayers(
            listOf(
                PlayerEntity(
                    id = "p1", teamId = "team_bra", name = "Ronaldo", position = "Centroavante",
                    number = 9, photoUrl = "", biography = "", goals = 8, matches = 7, assists = 0
                )
            )
        )

        // Act + Assert
        assertEquals(1, dao.teamCount())
        val twd = dao.getTeamWithDetails("team_bra")
        assertNotNull(twd)
        assertEquals("Brasil", twd!!.team.name)
        assertEquals("Felipão", twd.coach?.name)
        assertEquals(1, twd.players.size)
        assertEquals(listOf("2002"), twd.team.titleYears)
    }

    @Test
    fun replaceAllSubstituiOAcervoInteiro() = runTest {
        dao.upsertTeams(listOf(teamEntity("team_old", "Antiga")))
        assertEquals(1, dao.teamCount())

        dao.replaceAll(
            meta = CompetitionMetaEntity(name = "FIFA", edition = "e", year = "2002", subtitle = "", stadiumBackgroundUrl = ""),
            teams = listOf(teamEntity("team_new", "Nova")),
            coaches = emptyList(),
            players = emptyList()
        )

        assertEquals(1, dao.teamCount())
        assertNotNull(dao.getTeamWithDetails("team_new"))
        assertEquals(null, dao.getTeamWithDetails("team_old"))
    }

    private fun teamEntity(id: String, name: String) = TeamEntity(
        id = id, name = name, group = "", crestUrl = "", victories = 0,
        titleYears = emptyList(), finalResult = "", matchesPlayed = 0,
        primaryColor = "#1E1E1E", secondaryColor = "#121212", description = ""
    )
}
