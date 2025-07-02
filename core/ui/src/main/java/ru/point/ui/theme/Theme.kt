package com.example.compose

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowInsetsControllerCompat
import ru.point.ui.theme.AppTypography
import ru.point.ui.theme.backgroundDark
import ru.point.ui.theme.backgroundDarkHighContrast
import ru.point.ui.theme.backgroundDarkMediumContrast
import ru.point.ui.theme.backgroundLight
import ru.point.ui.theme.backgroundLightHighContrast
import ru.point.ui.theme.backgroundLightMediumContrast
import ru.point.ui.theme.errorContainerDark
import ru.point.ui.theme.errorContainerDarkHighContrast
import ru.point.ui.theme.errorContainerDarkMediumContrast
import ru.point.ui.theme.errorContainerLight
import ru.point.ui.theme.errorContainerLightHighContrast
import ru.point.ui.theme.errorContainerLightMediumContrast
import ru.point.ui.theme.errorDark
import ru.point.ui.theme.errorDarkHighContrast
import ru.point.ui.theme.errorDarkMediumContrast
import ru.point.ui.theme.errorLight
import ru.point.ui.theme.errorLightHighContrast
import ru.point.ui.theme.errorLightMediumContrast
import ru.point.ui.theme.inverseOnSurfaceDark
import ru.point.ui.theme.inverseOnSurfaceDarkHighContrast
import ru.point.ui.theme.inverseOnSurfaceDarkMediumContrast
import ru.point.ui.theme.inverseOnSurfaceLight
import ru.point.ui.theme.inverseOnSurfaceLightHighContrast
import ru.point.ui.theme.inverseOnSurfaceLightMediumContrast
import ru.point.ui.theme.inversePrimaryDark
import ru.point.ui.theme.inversePrimaryDarkHighContrast
import ru.point.ui.theme.inversePrimaryDarkMediumContrast
import ru.point.ui.theme.inversePrimaryLight
import ru.point.ui.theme.inversePrimaryLightHighContrast
import ru.point.ui.theme.inversePrimaryLightMediumContrast
import ru.point.ui.theme.inverseSurfaceDark
import ru.point.ui.theme.inverseSurfaceDarkHighContrast
import ru.point.ui.theme.inverseSurfaceDarkMediumContrast
import ru.point.ui.theme.inverseSurfaceLight
import ru.point.ui.theme.inverseSurfaceLightHighContrast
import ru.point.ui.theme.inverseSurfaceLightMediumContrast
import ru.point.ui.theme.onBackgroundDark
import ru.point.ui.theme.onBackgroundDarkHighContrast
import ru.point.ui.theme.onBackgroundDarkMediumContrast
import ru.point.ui.theme.onBackgroundLight
import ru.point.ui.theme.onBackgroundLightHighContrast
import ru.point.ui.theme.onBackgroundLightMediumContrast
import ru.point.ui.theme.onErrorContainerDark
import ru.point.ui.theme.onErrorContainerDarkHighContrast
import ru.point.ui.theme.onErrorContainerDarkMediumContrast
import ru.point.ui.theme.onErrorContainerLight
import ru.point.ui.theme.onErrorContainerLightHighContrast
import ru.point.ui.theme.onErrorContainerLightMediumContrast
import ru.point.ui.theme.onErrorDark
import ru.point.ui.theme.onErrorDarkHighContrast
import ru.point.ui.theme.onErrorDarkMediumContrast
import ru.point.ui.theme.onErrorLight
import ru.point.ui.theme.onErrorLightHighContrast
import ru.point.ui.theme.onErrorLightMediumContrast
import ru.point.ui.theme.onPrimaryContainerDark
import ru.point.ui.theme.onPrimaryContainerDarkHighContrast
import ru.point.ui.theme.onPrimaryContainerDarkMediumContrast
import ru.point.ui.theme.onPrimaryContainerLight
import ru.point.ui.theme.onPrimaryContainerLightHighContrast
import ru.point.ui.theme.onPrimaryContainerLightMediumContrast
import ru.point.ui.theme.onPrimaryDark
import ru.point.ui.theme.onPrimaryDarkHighContrast
import ru.point.ui.theme.onPrimaryDarkMediumContrast
import ru.point.ui.theme.onPrimaryLight
import ru.point.ui.theme.onPrimaryLightHighContrast
import ru.point.ui.theme.onPrimaryLightMediumContrast
import ru.point.ui.theme.onSecondaryContainerDark
import ru.point.ui.theme.onSecondaryContainerDarkHighContrast
import ru.point.ui.theme.onSecondaryContainerDarkMediumContrast
import ru.point.ui.theme.onSecondaryContainerLight
import ru.point.ui.theme.onSecondaryContainerLightHighContrast
import ru.point.ui.theme.onSecondaryContainerLightMediumContrast
import ru.point.ui.theme.onSecondaryDark
import ru.point.ui.theme.onSecondaryDarkHighContrast
import ru.point.ui.theme.onSecondaryDarkMediumContrast
import ru.point.ui.theme.onSecondaryLight
import ru.point.ui.theme.onSecondaryLightHighContrast
import ru.point.ui.theme.onSecondaryLightMediumContrast
import ru.point.ui.theme.onSurfaceDark
import ru.point.ui.theme.onSurfaceDarkHighContrast
import ru.point.ui.theme.onSurfaceDarkMediumContrast
import ru.point.ui.theme.onSurfaceLight
import ru.point.ui.theme.onSurfaceLightHighContrast
import ru.point.ui.theme.onSurfaceLightMediumContrast
import ru.point.ui.theme.onSurfaceVariantDark
import ru.point.ui.theme.onSurfaceVariantDarkHighContrast
import ru.point.ui.theme.onSurfaceVariantDarkMediumContrast
import ru.point.ui.theme.onSurfaceVariantLight
import ru.point.ui.theme.onSurfaceVariantLightHighContrast
import ru.point.ui.theme.onSurfaceVariantLightMediumContrast
import ru.point.ui.theme.onTertiaryContainerDark
import ru.point.ui.theme.onTertiaryContainerDarkHighContrast
import ru.point.ui.theme.onTertiaryContainerDarkMediumContrast
import ru.point.ui.theme.onTertiaryContainerLight
import ru.point.ui.theme.onTertiaryContainerLightHighContrast
import ru.point.ui.theme.onTertiaryContainerLightMediumContrast
import ru.point.ui.theme.onTertiaryDark
import ru.point.ui.theme.onTertiaryDarkHighContrast
import ru.point.ui.theme.onTertiaryDarkMediumContrast
import ru.point.ui.theme.onTertiaryLight
import ru.point.ui.theme.onTertiaryLightHighContrast
import ru.point.ui.theme.onTertiaryLightMediumContrast
import ru.point.ui.theme.outlineDark
import ru.point.ui.theme.outlineDarkHighContrast
import ru.point.ui.theme.outlineDarkMediumContrast
import ru.point.ui.theme.outlineLight
import ru.point.ui.theme.outlineLightHighContrast
import ru.point.ui.theme.outlineLightMediumContrast
import ru.point.ui.theme.outlineVariantDark
import ru.point.ui.theme.outlineVariantDarkHighContrast
import ru.point.ui.theme.outlineVariantDarkMediumContrast
import ru.point.ui.theme.outlineVariantLight
import ru.point.ui.theme.outlineVariantLightHighContrast
import ru.point.ui.theme.outlineVariantLightMediumContrast
import ru.point.ui.theme.primaryContainerDark
import ru.point.ui.theme.primaryContainerDarkHighContrast
import ru.point.ui.theme.primaryContainerDarkMediumContrast
import ru.point.ui.theme.primaryContainerLight
import ru.point.ui.theme.primaryContainerLightHighContrast
import ru.point.ui.theme.primaryContainerLightMediumContrast
import ru.point.ui.theme.primaryDark
import ru.point.ui.theme.primaryDarkHighContrast
import ru.point.ui.theme.primaryDarkMediumContrast
import ru.point.ui.theme.primaryLight
import ru.point.ui.theme.primaryLightHighContrast
import ru.point.ui.theme.primaryLightMediumContrast
import ru.point.ui.theme.scrimDark
import ru.point.ui.theme.scrimDarkHighContrast
import ru.point.ui.theme.scrimDarkMediumContrast
import ru.point.ui.theme.scrimLight
import ru.point.ui.theme.scrimLightHighContrast
import ru.point.ui.theme.scrimLightMediumContrast
import ru.point.ui.theme.secondaryContainerDark
import ru.point.ui.theme.secondaryContainerDarkHighContrast
import ru.point.ui.theme.secondaryContainerDarkMediumContrast
import ru.point.ui.theme.secondaryContainerLight
import ru.point.ui.theme.secondaryContainerLightHighContrast
import ru.point.ui.theme.secondaryContainerLightMediumContrast
import ru.point.ui.theme.secondaryDark
import ru.point.ui.theme.secondaryDarkHighContrast
import ru.point.ui.theme.secondaryDarkMediumContrast
import ru.point.ui.theme.secondaryLight
import ru.point.ui.theme.secondaryLightHighContrast
import ru.point.ui.theme.secondaryLightMediumContrast
import ru.point.ui.theme.surfaceBrightDark
import ru.point.ui.theme.surfaceBrightDarkHighContrast
import ru.point.ui.theme.surfaceBrightDarkMediumContrast
import ru.point.ui.theme.surfaceBrightLight
import ru.point.ui.theme.surfaceBrightLightHighContrast
import ru.point.ui.theme.surfaceBrightLightMediumContrast
import ru.point.ui.theme.surfaceContainerDark
import ru.point.ui.theme.surfaceContainerDarkHighContrast
import ru.point.ui.theme.surfaceContainerDarkMediumContrast
import ru.point.ui.theme.surfaceContainerHighDark
import ru.point.ui.theme.surfaceContainerHighDarkHighContrast
import ru.point.ui.theme.surfaceContainerHighDarkMediumContrast
import ru.point.ui.theme.surfaceContainerHighLight
import ru.point.ui.theme.surfaceContainerHighLightHighContrast
import ru.point.ui.theme.surfaceContainerHighLightMediumContrast
import ru.point.ui.theme.surfaceContainerHighestDark
import ru.point.ui.theme.surfaceContainerHighestDarkHighContrast
import ru.point.ui.theme.surfaceContainerHighestDarkMediumContrast
import ru.point.ui.theme.surfaceContainerHighestLight
import ru.point.ui.theme.surfaceContainerHighestLightHighContrast
import ru.point.ui.theme.surfaceContainerHighestLightMediumContrast
import ru.point.ui.theme.surfaceContainerLight
import ru.point.ui.theme.surfaceContainerLightHighContrast
import ru.point.ui.theme.surfaceContainerLightMediumContrast
import ru.point.ui.theme.surfaceContainerLowDark
import ru.point.ui.theme.surfaceContainerLowDarkHighContrast
import ru.point.ui.theme.surfaceContainerLowDarkMediumContrast
import ru.point.ui.theme.surfaceContainerLowLight
import ru.point.ui.theme.surfaceContainerLowLightHighContrast
import ru.point.ui.theme.surfaceContainerLowLightMediumContrast
import ru.point.ui.theme.surfaceContainerLowestDark
import ru.point.ui.theme.surfaceContainerLowestDarkHighContrast
import ru.point.ui.theme.surfaceContainerLowestDarkMediumContrast
import ru.point.ui.theme.surfaceContainerLowestLight
import ru.point.ui.theme.surfaceContainerLowestLightHighContrast
import ru.point.ui.theme.surfaceContainerLowestLightMediumContrast
import ru.point.ui.theme.surfaceDark
import ru.point.ui.theme.surfaceDarkHighContrast
import ru.point.ui.theme.surfaceDarkMediumContrast
import ru.point.ui.theme.surfaceDimDark
import ru.point.ui.theme.surfaceDimDarkHighContrast
import ru.point.ui.theme.surfaceDimDarkMediumContrast
import ru.point.ui.theme.surfaceDimLight
import ru.point.ui.theme.surfaceDimLightHighContrast
import ru.point.ui.theme.surfaceDimLightMediumContrast
import ru.point.ui.theme.surfaceLight
import ru.point.ui.theme.surfaceLightHighContrast
import ru.point.ui.theme.surfaceLightMediumContrast
import ru.point.ui.theme.surfaceVariantDark
import ru.point.ui.theme.surfaceVariantDarkHighContrast
import ru.point.ui.theme.surfaceVariantDarkMediumContrast
import ru.point.ui.theme.surfaceVariantLight
import ru.point.ui.theme.surfaceVariantLightHighContrast
import ru.point.ui.theme.surfaceVariantLightMediumContrast
import ru.point.ui.theme.tertiaryContainerDark
import ru.point.ui.theme.tertiaryContainerDarkHighContrast
import ru.point.ui.theme.tertiaryContainerDarkMediumContrast
import ru.point.ui.theme.tertiaryContainerLight
import ru.point.ui.theme.tertiaryContainerLightHighContrast
import ru.point.ui.theme.tertiaryContainerLightMediumContrast
import ru.point.ui.theme.tertiaryDark
import ru.point.ui.theme.tertiaryDarkHighContrast
import ru.point.ui.theme.tertiaryDarkMediumContrast
import ru.point.ui.theme.tertiaryLight
import ru.point.ui.theme.tertiaryLightHighContrast
import ru.point.ui.theme.tertiaryLightMediumContrast

private val lightScheme =
    lightColorScheme(
        primary = primaryLight,
        onPrimary = onPrimaryLight,
        primaryContainer = primaryContainerLight,
        onPrimaryContainer = onPrimaryContainerLight,
        secondary = secondaryLight,
        onSecondary = onSecondaryLight,
        secondaryContainer = secondaryContainerLight,
        onSecondaryContainer = onSecondaryContainerLight,
        tertiary = tertiaryLight,
        onTertiary = onTertiaryLight,
        tertiaryContainer = tertiaryContainerLight,
        onTertiaryContainer = onTertiaryContainerLight,
        error = errorLight,
        onError = onErrorLight,
        errorContainer = errorContainerLight,
        onErrorContainer = onErrorContainerLight,
        background = backgroundLight,
        onBackground = onBackgroundLight,
        surface = surfaceLight,
        onSurface = onSurfaceLight,
        surfaceVariant = surfaceVariantLight,
        onSurfaceVariant = onSurfaceVariantLight,
        outline = outlineLight,
        outlineVariant = outlineVariantLight,
        scrim = scrimLight,
        inverseSurface = inverseSurfaceLight,
        inverseOnSurface = inverseOnSurfaceLight,
        inversePrimary = inversePrimaryLight,
        surfaceDim = surfaceDimLight,
        surfaceBright = surfaceBrightLight,
        surfaceContainerLowest = surfaceContainerLowestLight,
        surfaceContainerLow = surfaceContainerLowLight,
        surfaceContainer = surfaceContainerLight,
        surfaceContainerHigh = surfaceContainerHighLight,
        surfaceContainerHighest = surfaceContainerHighestLight,
    )

private val darkScheme =
    darkColorScheme(
        primary = primaryDark,
        onPrimary = onPrimaryDark,
        primaryContainer = primaryContainerDark,
        onPrimaryContainer = onPrimaryContainerDark,
        secondary = secondaryDark,
        onSecondary = onSecondaryDark,
        secondaryContainer = secondaryContainerDark,
        onSecondaryContainer = onSecondaryContainerDark,
        tertiary = tertiaryDark,
        onTertiary = onTertiaryDark,
        tertiaryContainer = tertiaryContainerDark,
        onTertiaryContainer = onTertiaryContainerDark,
        error = errorDark,
        onError = onErrorDark,
        errorContainer = errorContainerDark,
        onErrorContainer = onErrorContainerDark,
        background = backgroundDark,
        onBackground = onBackgroundDark,
        surface = surfaceDark,
        onSurface = onSurfaceDark,
        surfaceVariant = surfaceVariantDark,
        onSurfaceVariant = onSurfaceVariantDark,
        outline = outlineDark,
        outlineVariant = outlineVariantDark,
        scrim = scrimDark,
        inverseSurface = inverseSurfaceDark,
        inverseOnSurface = inverseOnSurfaceDark,
        inversePrimary = inversePrimaryDark,
        surfaceDim = surfaceDimDark,
        surfaceBright = surfaceBrightDark,
        surfaceContainerLowest = surfaceContainerLowestDark,
        surfaceContainerLow = surfaceContainerLowDark,
        surfaceContainer = surfaceContainerDark,
        surfaceContainerHigh = surfaceContainerHighDark,
        surfaceContainerHighest = surfaceContainerHighestDark,
    )

private val mediumContrastLightColorScheme =
    lightColorScheme(
        primary = primaryLightMediumContrast,
        onPrimary = onPrimaryLightMediumContrast,
        primaryContainer = primaryContainerLightMediumContrast,
        onPrimaryContainer = onPrimaryContainerLightMediumContrast,
        secondary = secondaryLightMediumContrast,
        onSecondary = onSecondaryLightMediumContrast,
        secondaryContainer = secondaryContainerLightMediumContrast,
        onSecondaryContainer = onSecondaryContainerLightMediumContrast,
        tertiary = tertiaryLightMediumContrast,
        onTertiary = onTertiaryLightMediumContrast,
        tertiaryContainer = tertiaryContainerLightMediumContrast,
        onTertiaryContainer = onTertiaryContainerLightMediumContrast,
        error = errorLightMediumContrast,
        onError = onErrorLightMediumContrast,
        errorContainer = errorContainerLightMediumContrast,
        onErrorContainer = onErrorContainerLightMediumContrast,
        background = backgroundLightMediumContrast,
        onBackground = onBackgroundLightMediumContrast,
        surface = surfaceLightMediumContrast,
        onSurface = onSurfaceLightMediumContrast,
        surfaceVariant = surfaceVariantLightMediumContrast,
        onSurfaceVariant = onSurfaceVariantLightMediumContrast,
        outline = outlineLightMediumContrast,
        outlineVariant = outlineVariantLightMediumContrast,
        scrim = scrimLightMediumContrast,
        inverseSurface = inverseSurfaceLightMediumContrast,
        inverseOnSurface = inverseOnSurfaceLightMediumContrast,
        inversePrimary = inversePrimaryLightMediumContrast,
        surfaceDim = surfaceDimLightMediumContrast,
        surfaceBright = surfaceBrightLightMediumContrast,
        surfaceContainerLowest = surfaceContainerLowestLightMediumContrast,
        surfaceContainerLow = surfaceContainerLowLightMediumContrast,
        surfaceContainer = surfaceContainerLightMediumContrast,
        surfaceContainerHigh = surfaceContainerHighLightMediumContrast,
        surfaceContainerHighest = surfaceContainerHighestLightMediumContrast,
    )

private val highContrastLightColorScheme =
    lightColorScheme(
        primary = primaryLightHighContrast,
        onPrimary = onPrimaryLightHighContrast,
        primaryContainer = primaryContainerLightHighContrast,
        onPrimaryContainer = onPrimaryContainerLightHighContrast,
        secondary = secondaryLightHighContrast,
        onSecondary = onSecondaryLightHighContrast,
        secondaryContainer = secondaryContainerLightHighContrast,
        onSecondaryContainer = onSecondaryContainerLightHighContrast,
        tertiary = tertiaryLightHighContrast,
        onTertiary = onTertiaryLightHighContrast,
        tertiaryContainer = tertiaryContainerLightHighContrast,
        onTertiaryContainer = onTertiaryContainerLightHighContrast,
        error = errorLightHighContrast,
        onError = onErrorLightHighContrast,
        errorContainer = errorContainerLightHighContrast,
        onErrorContainer = onErrorContainerLightHighContrast,
        background = backgroundLightHighContrast,
        onBackground = onBackgroundLightHighContrast,
        surface = surfaceLightHighContrast,
        onSurface = onSurfaceLightHighContrast,
        surfaceVariant = surfaceVariantLightHighContrast,
        onSurfaceVariant = onSurfaceVariantLightHighContrast,
        outline = outlineLightHighContrast,
        outlineVariant = outlineVariantLightHighContrast,
        scrim = scrimLightHighContrast,
        inverseSurface = inverseSurfaceLightHighContrast,
        inverseOnSurface = inverseOnSurfaceLightHighContrast,
        inversePrimary = inversePrimaryLightHighContrast,
        surfaceDim = surfaceDimLightHighContrast,
        surfaceBright = surfaceBrightLightHighContrast,
        surfaceContainerLowest = surfaceContainerLowestLightHighContrast,
        surfaceContainerLow = surfaceContainerLowLightHighContrast,
        surfaceContainer = surfaceContainerLightHighContrast,
        surfaceContainerHigh = surfaceContainerHighLightHighContrast,
        surfaceContainerHighest = surfaceContainerHighestLightHighContrast,
    )

private val mediumContrastDarkColorScheme =
    darkColorScheme(
        primary = primaryDarkMediumContrast,
        onPrimary = onPrimaryDarkMediumContrast,
        primaryContainer = primaryContainerDarkMediumContrast,
        onPrimaryContainer = onPrimaryContainerDarkMediumContrast,
        secondary = secondaryDarkMediumContrast,
        onSecondary = onSecondaryDarkMediumContrast,
        secondaryContainer = secondaryContainerDarkMediumContrast,
        onSecondaryContainer = onSecondaryContainerDarkMediumContrast,
        tertiary = tertiaryDarkMediumContrast,
        onTertiary = onTertiaryDarkMediumContrast,
        tertiaryContainer = tertiaryContainerDarkMediumContrast,
        onTertiaryContainer = onTertiaryContainerDarkMediumContrast,
        error = errorDarkMediumContrast,
        onError = onErrorDarkMediumContrast,
        errorContainer = errorContainerDarkMediumContrast,
        onErrorContainer = onErrorContainerDarkMediumContrast,
        background = backgroundDarkMediumContrast,
        onBackground = onBackgroundDarkMediumContrast,
        surface = surfaceDarkMediumContrast,
        onSurface = onSurfaceDarkMediumContrast,
        surfaceVariant = surfaceVariantDarkMediumContrast,
        onSurfaceVariant = onSurfaceVariantDarkMediumContrast,
        outline = outlineDarkMediumContrast,
        outlineVariant = outlineVariantDarkMediumContrast,
        scrim = scrimDarkMediumContrast,
        inverseSurface = inverseSurfaceDarkMediumContrast,
        inverseOnSurface = inverseOnSurfaceDarkMediumContrast,
        inversePrimary = inversePrimaryDarkMediumContrast,
        surfaceDim = surfaceDimDarkMediumContrast,
        surfaceBright = surfaceBrightDarkMediumContrast,
        surfaceContainerLowest = surfaceContainerLowestDarkMediumContrast,
        surfaceContainerLow = surfaceContainerLowDarkMediumContrast,
        surfaceContainer = surfaceContainerDarkMediumContrast,
        surfaceContainerHigh = surfaceContainerHighDarkMediumContrast,
        surfaceContainerHighest = surfaceContainerHighestDarkMediumContrast,
    )

private val highContrastDarkColorScheme =
    darkColorScheme(
        primary = primaryDarkHighContrast,
        onPrimary = onPrimaryDarkHighContrast,
        primaryContainer = primaryContainerDarkHighContrast,
        onPrimaryContainer = onPrimaryContainerDarkHighContrast,
        secondary = secondaryDarkHighContrast,
        onSecondary = onSecondaryDarkHighContrast,
        secondaryContainer = secondaryContainerDarkHighContrast,
        onSecondaryContainer = onSecondaryContainerDarkHighContrast,
        tertiary = tertiaryDarkHighContrast,
        onTertiary = onTertiaryDarkHighContrast,
        tertiaryContainer = tertiaryContainerDarkHighContrast,
        onTertiaryContainer = onTertiaryContainerDarkHighContrast,
        error = errorDarkHighContrast,
        onError = onErrorDarkHighContrast,
        errorContainer = errorContainerDarkHighContrast,
        onErrorContainer = onErrorContainerDarkHighContrast,
        background = backgroundDarkHighContrast,
        onBackground = onBackgroundDarkHighContrast,
        surface = surfaceDarkHighContrast,
        onSurface = onSurfaceDarkHighContrast,
        surfaceVariant = surfaceVariantDarkHighContrast,
        onSurfaceVariant = onSurfaceVariantDarkHighContrast,
        outline = outlineDarkHighContrast,
        outlineVariant = outlineVariantDarkHighContrast,
        scrim = scrimDarkHighContrast,
        inverseSurface = inverseSurfaceDarkHighContrast,
        inverseOnSurface = inverseOnSurfaceDarkHighContrast,
        inversePrimary = inversePrimaryDarkHighContrast,
        surfaceDim = surfaceDimDarkHighContrast,
        surfaceBright = surfaceBrightDarkHighContrast,
        surfaceContainerLowest = surfaceContainerLowestDarkHighContrast,
        surfaceContainerLow = surfaceContainerLowDarkHighContrast,
        surfaceContainer = surfaceContainerDarkHighContrast,
        surfaceContainerHigh = surfaceContainerHighDarkHighContrast,
        surfaceContainerHighest = surfaceContainerHighestDarkHighContrast,
    )

@Immutable
data class ColorFamily(
    val color: Color,
    val onColor: Color,
    val colorContainer: Color,
    val onColorContainer: Color,
)

val unspecified_scheme =
    ColorFamily(
        Color.Unspecified,
        Color.Unspecified,
        Color.Unspecified,
        Color.Unspecified,
    )

@Composable
fun HomeWork1Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content:
        @Composable()
        () -> Unit,
) {
    val colorScheme =
        when {
            dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                val context = LocalContext.current
                if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
            }

            darkTheme -> darkScheme
            else -> lightScheme
        }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
    ) {
        val view = LocalView.current

        SideEffect {
            val window = (view.context as Activity).window

            if (!darkTheme) {
                window.statusBarColor = colorScheme.primaryContainer.toArgb()
                WindowInsetsControllerCompat(window, view)
                    .isAppearanceLightStatusBars = true
            } else {
                window.statusBarColor = colorScheme.background.toArgb()
                WindowInsetsControllerCompat(window, view)
                    .isAppearanceLightStatusBars = false
            }
        }

        content()
    }
}
