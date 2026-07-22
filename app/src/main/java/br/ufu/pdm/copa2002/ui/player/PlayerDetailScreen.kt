package br.ufu.pdm.copa2002.ui.player

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.TouchApp
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.ufu.pdm.copa2002.domain.model.Player
import br.ufu.pdm.copa2002.ui.common.UiState
import br.ufu.pdm.copa2002.ui.components.AvatarImage
import br.ufu.pdm.copa2002.ui.theme.BlueInstitutional
import br.ufu.pdm.copa2002.ui.theme.GoldYellow
import br.ufu.pdm.copa2002.ui.theme.TextGrayLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerDetailScreen(
    onBackClick: () -> Unit,
    viewModel: PlayerDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Figurinha") },
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
        Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
            when (val state = uiState) {
                is UiState.Loading -> CircularProgressIndicator(color = GoldYellow)
                is UiState.Error -> Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(24.dp)
                )
                is UiState.Success -> FlipCard(player = state.data)
            }
        }
    }
}

/**
 * Figurinha com animação de giro tridimensional no eixo Y (RF05).
 * Toque alterna entre a frente (foto/identificação) e o verso (biografia/estatísticas).
 */
@Composable
private fun FlipCard(player: Player) {
    var flipped by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(
        targetValue = if (flipped) 180f else 0f,
        animationSpec = tween(durationMillis = 500),
        label = "cardFlip"
    )
    val density = LocalDensity.current.density

    Column(
        modifier = Modifier.fillMaxWidth().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.66f)
                .graphicsLayer {
                    rotationY = rotation
                    cameraDistance = 12f * density
                }
                .clip(RoundedCornerShape(24.dp))
                .background(MaterialTheme.colorScheme.surface)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { flipped = !flipped }
        ) {
            if (rotation <= 90f) {
                CardFront(player)
            } else {
                // Corrige o espelhamento do verso girando o conteúdo em 180°.
                Box(modifier = Modifier.fillMaxSize().graphicsLayer { rotationY = 180f }) {
                    CardBack(player)
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.TouchApp, contentDescription = null, tint = TextGrayLight, modifier = Modifier.size(16.dp))
            Spacer(Modifier.size(6.dp))
            Text(
                text = "Toque na figurinha para virar",
                style = MaterialTheme.typography.labelSmall,
                color = TextGrayLight
            )
        }
    }
}

@Composable
private fun CardFront(player: Player) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AvatarImage(
            url = player.photoUrl,
            fallbackText = player.name,
            backgroundColor = BlueInstitutional,
            contentDescription = "Foto de ${player.name}",
            modifier = Modifier.fillMaxWidth().weight(1f).clip(RoundedCornerShape(16.dp))
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = "#${player.number}",
            color = GoldYellow,
            fontWeight = FontWeight.Black,
            fontSize = 32.sp
        )
        Text(
            text = player.name,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
        Text(
            text = player.position,
            style = MaterialTheme.typography.bodyMedium,
            color = TextGrayLight
        )
    }
}

@Composable
private fun CardBack(player: Player) {
    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "BIOGRAFIA",
            style = MaterialTheme.typography.labelSmall,
            color = GoldYellow,
            letterSpacing = 2.sp
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = player.biography,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.height(24.dp))
        Text(
            text = "ESTATÍSTICAS NA COMPETIÇÃO",
            style = MaterialTheme.typography.labelSmall,
            color = GoldYellow,
            letterSpacing = 2.sp
        )
        Spacer(Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatItem(value = player.goals, label = "Gols")
            StatItem(value = player.matches, label = "Jogos")
            StatItem(value = player.assists, label = "Assist.")
        }
    }
}

@Composable
private fun StatItem(value: Int, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value.toString(),
            color = GoldYellow,
            fontWeight = FontWeight.Black,
            fontSize = 28.sp
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = TextGrayLight
        )
    }
}
