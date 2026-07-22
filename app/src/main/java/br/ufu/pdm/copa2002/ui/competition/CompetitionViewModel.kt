package br.ufu.pdm.copa2002.ui.competition

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.ufu.pdm.copa2002.domain.model.Competition
import br.ufu.pdm.copa2002.domain.repository.CompetitionRepository
import br.ufu.pdm.copa2002.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompetitionViewModel @Inject constructor(
    private val repository: CompetitionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<Competition>>(UiState.Loading)
    val uiState: StateFlow<UiState<Competition>> = _uiState.asStateFlow()

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                _uiState.value = UiState.Success(repository.getCompetition())
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Erro ao carregar a competição.")
            }
        }
    }

    fun onQueryChange(newQuery: String) {
        _query.value = newQuery
    }
}
