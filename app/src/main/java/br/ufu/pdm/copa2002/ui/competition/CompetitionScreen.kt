package br.ufu.pdm.copa2002.ui.competition

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.ufu.pdm.copa2002.domain.model.Competition
import br.ufu.pdm.copa2002.domain.model.Team
import br.ufu.pdm.copa2002.ui.common.UiState
import br.ufu.pdm.copa2002.ui.components.AvatarImage
import br.ufu.pdm.copa2002.ui.theme.GoldYellow
import br.ufu.pdm.copa2002.ui.theme.TextGrayLight
import br.ufu.pdm.copa2002.ui.theme.parseHexColor

@Composable
fun CompetitionScreen(
    onTeamClick: (String) -> Unit,
    viewModel: CompetitionViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val query by viewModel.query.collectAsStateWithLifecycle()

    Box(modifier = Modifier.fillMaxSize()) {
        when (val state = uiState) {
            is UiState.Loading -> CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = GoldYellow
            )

            is UiState.Error -> Text(
                text = state.message,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(24.dp)
            )

            is UiState.Success -> CompetitionContent(
                competition = state.data,
                query = query,
                onQueryChange = viewModel::onQueryChange,
                onTeamClick = onTeamClick
            )
        }
    }
}

@Composable
private fun CompetitionContent(
    competition: Competition,
    query: String,
    onQueryChange: (String) -> Unit,
    onTeamClick: (String) -> Unit
) {
    val teams = competition.teams.filter {
        query.isBlank() ||
            it.name.contains(query, ignoreCase = true) ||
            it.group.contains(query, ignoreCase = true)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            CompetitionHeader(competition)
            Spacer(Modifier.height(20.dp))
            SearchField(query = query, onQueryChange = onQueryChange)
            Spacer(Modifier.height(16.dp))
            Text(
                text = "${teams.size} NAÇÕES · POR CLASSIFICAÇÃO",
                style = MaterialTheme.typography.labelSmall,
                color = TextGrayLight
            )
            Spacer(Modifier.height(4.dp))
        }

        items(items = teams, key = { it.id }) { team ->
            TeamRow(team = team, onClick = { onTeamClick(team.id) })
        }
    }
}

@Composable
private fun CompetitionHeader(competition: Competition) {
    Column {
        Text(
            text = competition.name.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = TextGrayLight,
            letterSpacing = 2.sp
        )
        Text(
            text = competition.edition.uppercase(),
            style = MaterialTheme.typography.displayLarge,
            color = GoldYellow
        )
        Text(
            text = "${competition.year} · ${competition.subtitle}",
            style = MaterialTheme.typography.bodyMedium,
            color = TextGrayLight
        )
        Spacer(Modifier.height(12.dp))
        // Linha de acento nas cores da seleção brasileira (verde/ouro/azul).
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            AccentBar(MaterialTheme.colorScheme.secondary)
            AccentBar(GoldYellow)
            AccentBar(MaterialTheme.colorScheme.tertiary)
        }
    }
}

@Composable
private fun AccentBar(color: androidx.compose.ui.graphics.Color) {
    Box(
        modifier = Modifier
            .width(64.dp)
            .height(3.dp)
            .clip(RoundedCornerShape(2.dp))
            .background(color)
    )
}

@Composable
private fun SearchField(query: String, onQueryChange: (String) -> Unit) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Buscar nação ou grupo...") },
        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedBorderColor = GoldYellow,
            unfocusedBorderColor = MaterialTheme.colorScheme.surface,
            focusedLeadingIconColor = GoldYellow,
            unfocusedLeadingIconColor = TextGrayLight,
            focusedPlaceholderColor = TextGrayLight,
            unfocusedPlaceholderColor = TextGrayLight
        )
    )
}

@Composable
private fun TeamRow(team: Team, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable(onClick = onClick)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AvatarImage(
            url = team.crestUrl,
            fallbackText = team.name,
            backgroundColor = parseHexColor(team.secondaryColor),
            contentDescription = "Brasão da seleção ${team.name}",
            textColor = parseHexColor(team.primaryColor),
            modifier = Modifier
                .size(52.dp)
                .clip(RoundedCornerShape(12.dp))
        )
        Spacer(Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = team.name.uppercase(),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.width(6.dp))
                repeat(team.victories.coerceAtMost(5)) {
                    Icon(
                        imageVector = Icons.Filled.EmojiEvents,
                        contentDescription = null,
                        tint = GoldYellow,
                        modifier = Modifier.size(13.dp)
                    )
                }
            }
            Text(
                text = team.group,
                style = MaterialTheme.typography.bodyMedium,
                color = TextGrayLight
            )
            if (team.titleYears.isNotEmpty()) {
                Text(
                    text = team.titleYears.joinToString(" · "),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextGrayLight,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Spacer(Modifier.width(8.dp))
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = team.finalResult,
                style = MaterialTheme.typography.labelSmall,
                color = if (team.finalResult == "Campeão") GoldYellow else MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "${team.matchesPlayed} jog.",
                style = MaterialTheme.typography.labelSmall,
                color = TextGrayLight
            )
        }
        Icon(
            imageVector = Icons.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = TextGrayLight
        )
    }
}
