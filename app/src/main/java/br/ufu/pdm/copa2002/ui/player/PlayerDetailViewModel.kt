package br.ufu.pdm.copa2002.ui.player

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.ufu.pdm.copa2002.domain.model.Player
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
class PlayerDetailViewModel @Inject constructor(
    private val repository: CompetitionRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val playerId: String = checkNotNull(savedStateHandle[Screen.ARG_PLAYER_ID])

    private val _uiState = MutableStateFlow<UiState<Player>>(UiState.Loading)
    val uiState: StateFlow<UiState<Player>> = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val player = repository.getPlayer(playerId)
                    ?: throw NoSuchElementException("Jogador não encontrado.")
                _uiState.value = UiState.Success(player)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Erro ao carregar o jogador.")
            }
        }
    }
}
