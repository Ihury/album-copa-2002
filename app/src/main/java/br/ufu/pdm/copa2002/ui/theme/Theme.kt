package br.ufu.pdm.copa2002.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// O app adota um tema escuro fixo, coerente com a identidade visual da Fase 1.
private val Copa2002ColorScheme = darkColorScheme(
    primary = GoldYellow,
    onPrimary = BackgroundBlack,
    secondary = GreenInstitutional,
    onSecondary = TextWhite,
    tertiary = BlueInstitutional,
    onTertiary = TextWhite,
    background = BackgroundBlack,
    onBackground = TextWhite,
    surface = SurfaceGray,
    onSurface = TextWhite,
    surfaceVariant = SurfaceGray,
    onSurfaceVariant = TextGrayLight,
    error = RedInstitutional,
    onError = TextWhite,
    outline = TextGrayLight,
)

@Composable
fun Copa2002Theme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = Copa2002ColorScheme,
        typography = Copa2002Typography,
        content = content
    )
}

/**
 * Converte uma cor hexadecimal do modelo (ex.: "#FFDF00") em [Color] do Compose.
 * Retorna [fallback] em caso de valor inválido, evitando crashes de parsing.
 */
fun parseHexColor(hex: String, fallback: Color = SurfaceGray): Color = try {
    Color(android.graphics.Color.parseColor(hex))
} catch (e: IllegalArgumentException) {
    fallback
}
