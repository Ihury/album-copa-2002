package br.ufu.pdm.copa2002.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.ufu.pdm.copa2002.ui.coach.CoachDetailScreen
import br.ufu.pdm.copa2002.ui.competition.CompetitionScreen
import br.ufu.pdm.copa2002.ui.player.PlayerDetailScreen
import br.ufu.pdm.copa2002.ui.team.TeamScreen

/**
 * NavHost central — controlador declarativo do fluxo de telas.
 * As telas recebem apenas callbacks de navegação; os IDs de rota são resolvidos
 * dentro de cada ViewModel (SavedStateHandle), mantendo os Composables isolados.
 */
@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Screen.Competition.route
    ) {
        composable(Screen.Competition.route) {
            CompetitionScreen(
                onTeamClick = { teamId ->
                    navController.navigate(Screen.Team.createRoute(teamId))
                }
            )
        }

        composable(
            route = Screen.Team.route,
            arguments = listOf(navArgument(Screen.ARG_TEAM_ID) { type = NavType.StringType })
        ) {
            TeamScreen(
                onPlayerClick = { playerId ->
                    navController.navigate(Screen.PlayerDetail.createRoute(playerId))
                },
                onCoachClick = { coachId ->
                    navController.navigate(Screen.CoachDetail.createRoute(coachId))
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.PlayerDetail.route,
            arguments = listOf(navArgument(Screen.ARG_PLAYER_ID) { type = NavType.StringType })
        ) {
            PlayerDetailScreen(onBackClick = { navController.popBackStack() })
        }

        composable(
            route = Screen.CoachDetail.route,
            arguments = listOf(navArgument(Screen.ARG_COACH_ID) { type = NavType.StringType })
        ) {
            CoachDetailScreen(onBackClick = { navController.popBackStack() })
        }
    }
}
