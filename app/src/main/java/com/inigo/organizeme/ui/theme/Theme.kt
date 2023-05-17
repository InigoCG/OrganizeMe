package com.inigo.organizeme.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = naranja,
    primaryVariant = azul_claro,
    secondary = azul_oscuro,
    background = gris,
    onBackground = negro,
    onSecondary = blanco,
    surface = coral
)

private val LightColorPalette = lightColors(
    primary = naranja,
    primaryVariant = azul_claro,
    secondary = azul_oscuro,
    background = gris,
    onBackground = negro,
    onSecondary = blanco,
    surface = coral

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun OrganizeMeTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}