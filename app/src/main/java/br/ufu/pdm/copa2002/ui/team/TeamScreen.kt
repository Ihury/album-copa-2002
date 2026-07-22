package br.ufu.pdm.copa2002.ui.team

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.ufu.pdm.copa2002.domain.model.Coach
import br.ufu.pdm.copa2002.domain.model.Player
import br.ufu.pdm.copa2002.domain.model.Team
import br.ufu.pdm.copa2002.ui.common.UiState
import br.ufu.pdm.copa2002.ui.components.AvatarImage
import br.ufu.pdm.copa2002.ui.theme.BackgroundBlack
import br.ufu.pdm.copa2002.ui.theme.GoldYellow
import br.ufu.pdm.copa2002.ui.theme.TextGrayLight
import br.ufu.pdm.copa2002.ui.theme.parseHexColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamScreen(
    onPlayerClick: (String) -> Unit,
    onCoachClick: (String) -> Unit,
    onBackClick: () -> Unit,
    viewModel: TeamViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text((uiState as? UiState.Success)?.data?.name ?: "Seleção")
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when (val state = uiState) {
                is UiState.Loading -> CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = GoldYellow
                )

                is UiState.Error -> Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center).padding(24.dp)
                )

                is UiState.Success -> TeamContent(
                    team = state.data,
                    onPlayerClick = onPlayerClick,
                    onCoachClick = onCoachClick
                )
            }
        }
    }
}

@Composable
private fun TeamContent(
    team: Team,
    onPlayerClick: (String) -> Unit,
    onCoachClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { TeamHeader(team) }

        item {
            SectionTitle("Comissão Técnica")
            CoachCard(coach = team.coach, onClick = { onCoachClick(team.coach.id) })
        }

        item { SectionTitle("Jogadores de Destaque") }

        // Grid 2 colunas: agrupa jogadores em pares por linha.
        items(count = (team.players.size + 1) / 2) { rowIndex ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val first = team.players[rowIndex * 2]
                PlayerCard(
                    player = first,
                    accentColor = parseHexColor(team.primaryColor),
                    onClick = { onPlayerClick(first.id) },
                    modifier = Modifier.weight(1f)
                )
                val secondIndex = rowIndex * 2 + 1
                if (secondIndex < team.players.size) {
                    val second = team.players[secondIndex]
                    PlayerCard(
                        player = second,
                        accentColor = parseHexColor(team.primaryColor),
                        onClick = { onPlayerClick(second.id) },
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    Spacer(Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun TeamHeader(team: Team) {
    val primary = parseHexColor(team.primaryColor)
    val secondary = parseHexColor(team.secondaryColor)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(listOf(secondary.copy(alpha = 0.55f), BackgroundBlack))
            )
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AvatarImage(
                url = team.crestUrl,
                fallbackText = team.name,
                backgroundColor = secondary,
                contentDescription = "Brasão da seleção ${team.name}",
                textColor = primary,
                modifier = Modifier.size(72.dp).clip(RoundedCornerShape(16.dp))
            )
            Spacer(Modifier.width(16.dp))
            Column {
                Text(
                    text = team.name.uppercase(),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = team.group,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextGrayLight
                )
                if (team.victories > 0) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        repeat(team.victories.coerceAtMost(5)) {
                            Icon(
                                Icons.Filled.EmojiEvents,
                                contentDescription = null,
                                tint = GoldYellow,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = "${team.victories}x Campeã",
                            style = MaterialTheme.typography.labelSmall,
                            color = GoldYellow,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(14.dp))
        Text(
            text = team.description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = GoldYellow,
        modifier = Modifier.padding(horizontal = 20.dp)
    )
}

@Composable
private fun CoachCard(coach: Coach, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable(onClick = onClick)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AvatarImage(
            url = coach.photoUrl,
            fallbackText = coach.name,
            backgroundColor = MaterialTheme.colorScheme.tertiary,
            contentDescription = "Foto do treinador ${coach.name}",
            modifier = Modifier.size(48.dp).clip(RoundedCornerShape(24.dp))
        )
        Spacer(Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = coach.name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = coach.role,
                style = MaterialTheme.typography.bodyMedium,
                color = TextGrayLight
            )
        }
        Icon(Icons.Filled.KeyboardArrowRight, contentDescription = null, tint = TextGrayLight)
    }
}

@Composable
private fun PlayerCard(
    player: Player,
    accentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable(onClick = onClick)
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {
            AvatarImage(
                url = player.photoUrl,
                fallbackText = player.name,
                backgroundColor = accentColor.copy(alpha = 0.85f),
                contentDescription = "Figurinha de ${player.name}",
                modifier = Modifier.size(88.dp).clip(RoundedCornerShape(12.dp))
            )
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .clip(RoundedCornerShape(8.dp))
                    .background(GoldYellow)
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "#${player.number}",
                    style = MaterialTheme.typography.labelSmall,
                    color = BackgroundBlack,
                    fontWeight = FontWeight.Black
                )
            }
        }
        Spacer(Modifier.height(8.dp))
        Text(
            text = player.name,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = player.position,
            style = MaterialTheme.typography.labelSmall,
            color = TextGrayLight
        )
    }
}
