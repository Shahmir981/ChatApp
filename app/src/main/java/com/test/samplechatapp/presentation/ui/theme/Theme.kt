package com.test.samplechatapp.presentation.ui.theme


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
    primary = Gray,
    secondary = Black50,
    primaryContainer = Gray,
    tertiary = Red,
    background = Color.White,
    surface = SurfaceDark,
    onPrimary = OnPrimaryDark,
    onSecondary = OnSecondaryDark,
    onTertiary = OnTertiaryDark,
    onBackground = OnBackgroundDark,
    onSurface = OnSurfaceDark
)

private val LightColorScheme = lightColorScheme(
    primary = Gray,
    primaryContainer = Gray,
    secondary = PurpleGrey40,
    tertiary = Ping50,
    background = Color.White,
    surface = SurfaceLight,
    onPrimary = OnPrimaryLight,
    onSecondary = OnSecondaryLight,
    onTertiary = OnTertiaryLight,
    onBackground = OnBackgroundLight,
    onSurface = OnSurfaceLight
)

@Composable
fun ChatsAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S -> {
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
