package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = BaobabGreenBright,
    onPrimary = Color.Black,
    primaryContainer = BaobabGreen,
    onPrimaryContainer = Color.White,
    secondary = SavannaGoldBright,
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF4C3600),
    onSecondaryContainer = SavannaGoldContainer,
    tertiary = TerracottaRedBright,
    onTertiary = Color.White,
    background = DarkBackground,
    onBackground = DarkText,
    surface = DarkSurface,
    onSurface = DarkText,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkTextSecondary
)

private val LightColorScheme = lightColorScheme(
    primary = BaobabGreen,
    onPrimary = Color.White,
    primaryContainer = BaobabGreenContainer,
    onPrimaryContainer = BaobabOnGreenContainer,
    secondary = SavannaGold,
    onSecondary = Color.Black,
    secondaryContainer = SavannaGoldContainer,
    onSecondaryContainer = SavannaOnGoldContainer,
    tertiary = TerracottaRed,
    onTertiary = Color.White,
    background = IvoryBackground,
    onBackground = CharcoalText,
    surface = IvorySurface,
    onSurface = CharcoalText,
    surfaceVariant = IvorySurfaceVariant,
    onSurfaceVariant = CharcoalTextSecondary
)

@Composable
fun BaobabbleTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep signature Baobabble branding dominant
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
