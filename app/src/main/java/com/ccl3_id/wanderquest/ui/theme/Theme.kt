package com.ccl3_id.wanderquest.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    //Buttons
    primary = Color(0xFFCBFFC3),
    onPrimary = Color(0xFF273E47),
    //Background
    background = Color(0xFF3A424A),
    onBackground = Color(0xFF97ECFF),
    surface = Color(0xFF3A424A),
    onSurface =  Color(0xFF97ECFF),
    //Nav
    secondary = Color(0xFFDFF9FF),
    //Go back/cancel
    tertiary = Color(0xFFEFFF8E),
    //ItemBg
    onSecondary = Color(0xFFCDCBC7),
    onSecondaryContainer = Color(0xFF817D78),
    //Red/Health
    onTertiary = Color(0xFFF17777),
)

private val LightColorScheme = lightColorScheme(
    //Buttons
    primary = Color(0xFFCBFFC3),
    onPrimary = Color(0xFF273E47),
    //Background
    background = Color(0xFF3A424A),
    onBackground = Color(0xFF97ECFF),
    surface = Color(0xFF3A424A),
    onSurface =  Color(0xFF97ECFF),
    //Nav
    secondary = Color(0xFFDFF9FF),
    //Go back/cancel
    tertiary = Color(0xFFEFFF8E),
    //ItemBg
    onSecondary = Color(0xFFCDCBC7),
    onSecondaryContainer = Color(0xFF817D78),
    //Red/Health
    onTertiary = Color(0xFFF17777),

)

@Composable
fun WanderQuestTheme(
        darkTheme: Boolean = isSystemInDarkTheme(),
        content: @Composable () -> Unit
) {
    val colorScheme = when {

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
    )
}