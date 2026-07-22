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
        observeCache()
        refresh()
    }

    /** Offline-first: exibe o cache local assim que existir (reativo via Room). */
    private fun observeCache() {
        viewModelScope.launch {
            repository.observeCompetition().collect { competition ->
                if (competition != null) {
                    _uiState.value = UiState.Success(competition)
                }
            }
        }
    }

    /** Atualiza o cache local a partir da fonte remota. */
    fun refresh() {
        viewModelScope.launch {
            try {
                repository.refresh()
            } catch (e: Exception) {
                // Só sinaliza erro se ainda não há dados em cache para exibir.
                if (_uiState.value !is UiState.Success) {
                    _uiState.value = UiState.Error(e.message ?: "Erro ao carregar a competição.")
                }
            }
        }
    }

    fun onQueryChange(newQuery: String) {
        _query.value = newQuery
    }
}
