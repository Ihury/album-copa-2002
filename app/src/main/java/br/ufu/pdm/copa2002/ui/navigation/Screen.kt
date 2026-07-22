package br.ufu.pdm.copa2002.ui.navigation

/**
 * Destinos de navegação com rotas fortemente identificadas.
 * As telas de detalhe recebem argumentos codificados na rota (teamId, playerId,
 * coachId), lidos pelas ViewModels via SavedStateHandle.
 */
sealed class Screen(val route: String) {

    data object Competition : Screen("competition")

    data object Team : Screen("team/{$ARG_TEAM_ID}") {
        fun createRoute(teamId: String) = "team/$teamId"
    }

    data object PlayerDetail : Screen("player/{$ARG_PLAYER_ID}") {
        fun createRoute(playerId: String) = "player/$playerId"
    }

    data object CoachDetail : Screen("coach/{$ARG_COACH_ID}") {
        fun createRoute(coachId: String) = "coach/$coachId"
    }

    companion object {
        const val ARG_TEAM_ID = "teamId"
        const val ARG_PLAYER_ID = "playerId"
        const val ARG_COACH_ID = "coachId"
    }
}
