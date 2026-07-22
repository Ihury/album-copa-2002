package br.ufu.pdm.copa2002.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import coil.compose.AsyncImage

/**
 * Imagem com fallback: exibe as iniciais sobre um fundo colorido enquanto a
 * imagem remota não carrega (ou não existe). Ao integrar a fonte remota real,
 * a foto sobrepõe o placeholder automaticamente.
 *
 * @param contentDescription descrição para acessibilidade (TalkBack). Passe null
 *        apenas quando a imagem for puramente decorativa.
 */
@Composable
fun AvatarImage(
    url: String,
    fallbackText: String,
    backgroundColor: Color,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    textColor: Color = Color.White
) {
    Box(
        modifier = modifier.background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = fallbackText.take(3).uppercase(),
            color = textColor,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium
        )
        AsyncImage(
            model = url,
            contentDescription = contentDescription,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}
