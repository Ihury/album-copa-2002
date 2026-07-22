package br.ufu.pdm.copa2002.ui.competition

import br.ufu.pdm.copa2002.MainDispatcherRule
import br.ufu.pdm.copa2002.domain.model.Competition
import br.ufu.pdm.copa2002.domain.repository.CompetitionRepository
import br.ufu.pdm.copa2002.ui.common.UiState
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CompetitionViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = mockk<CompetitionRepository>(relaxed = true)

    private val sample = Competition(
        name = "FIFA World Cup",
        edition = "Korea · Japan",
        year = "2002",
        subtitle = "Selecione uma nação",
        stadiumBackgroundUrl = "",
        teams = emptyList()
    )

    @Test
    fun `emite Success quando o cache tem dados`() = runTest {
        every { repository.observeCompetition() } returns flowOf(sample)
        coEvery { repository.refresh() } returns Unit

        val viewModel = CompetitionViewModel(repository)

        val state = viewModel.uiState.value
        assertTrue("esperado Success, veio $state", state is UiState.Success)
        assertEquals("FIFA World Cup", (state as UiState.Success).data.name)
    }

    @Test
    fun `emite Error quando refresh falha e nao ha cache`() = runTest {
        every { repository.observeCompetition() } returns flowOf(null)
        coEvery { repository.refresh() } throws RuntimeException("sem rede")

        val viewModel = CompetitionViewModel(repository)

        assertTrue(viewModel.uiState.value is UiState.Error)
    }

    @Test
    fun `onQueryChange atualiza a query`() = runTest {
        every { repository.observeCompetition() } returns flowOf(null)
        coEvery { repository.refresh() } returns Unit

        val viewModel = CompetitionViewModel(repository)
        viewModel.onQueryChange("bra")

        assertEquals("bra", viewModel.query.value)
    }
}
