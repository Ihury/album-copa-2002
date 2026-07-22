package br.ufu.pdm.copa2002.ui.common

/**
 * Estado genérico de UI para operações assíncronas (Máquina de Estados Finitos).
 * As telas reagem a cada estado emitido via StateFlow (fluxo unidirecional).
 */
sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Error(val message: String) : UiState<Nothing>
}
