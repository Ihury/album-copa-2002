package br.ufu.pdm.copa2002.ui.team

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.ufu.pdm.copa2002.domain.model.Team
import br.ufu.pdm.copa2002.domain.repository.CompetitionRepository
import br.ufu.pdm.copa2002.ui.common.UiState
import br.ufu.pdm.copa2002.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamViewModel @Inject constructor(
    private val repository: CompetitionRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Argumento de rota injetado automaticamente pelo Navigation via SavedStateHandle.
    private val teamId: String = checkNotNull(savedStateHandle[Screen.ARG_TEAM_ID])

    private val _uiState = MutableStateFlow<UiState<Team>>(UiState.Loading)
    val uiState: StateFlow<UiState<Team>> = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val team = repository.getTeam(teamId)
                    ?: throw NoSuchElementException("Seleção não encontrada.")
                _uiState.value = UiState.Success(team)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Erro ao carregar a seleção.")
            }
        }
    }
}
