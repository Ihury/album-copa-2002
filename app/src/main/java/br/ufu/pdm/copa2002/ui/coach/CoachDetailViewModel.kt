package br.ufu.pdm.copa2002.ui.coach

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.ufu.pdm.copa2002.domain.model.Coach
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
class CoachDetailViewModel @Inject constructor(
    private val repository: CompetitionRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val coachId: String = checkNotNull(savedStateHandle[Screen.ARG_COACH_ID])

    private val _uiState = MutableStateFlow<UiState<Coach>>(UiState.Loading)
    val uiState: StateFlow<UiState<Coach>> = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val coach = repository.getCoach(coachId)
                    ?: throw NoSuchElementException("Treinador não encontrado.")
                _uiState.value = UiState.Success(coach)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Erro ao carregar o treinador.")
            }
        }
    }
}
