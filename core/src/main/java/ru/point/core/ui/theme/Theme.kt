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
import ru.point.core.ui.theme.AppTypography
import ru.point.core.ui.theme.backgroundDark
import ru.point.core.ui.theme.backgroundDarkHighContrast
import ru.point.core.ui.theme.backgroundDarkMediumContrast
import ru.point.core.ui.theme.backgroundLight
import ru.point.core.ui.theme.backgroundLightHighContrast
import ru.point.core.ui.theme.backgroundLightMediumContrast
import ru.point.core.ui.theme.errorContainerDark
import ru.point.core.ui.theme.errorContainerDarkHighContrast
import ru.point.core.ui.theme.errorContainerDarkMediumContrast
import ru.point.core.ui.theme.errorContainerLight
import ru.point.core.ui.theme.errorContainerLightHighContrast
import ru.point.core.ui.theme.errorContainerLightMediumContrast
import ru.point.core.ui.theme.errorDark
import ru.point.core.ui.theme.errorDarkHighContrast
import ru.point.core.ui.theme.errorDarkMediumContrast
import ru.point.core.ui.theme.errorLight
import ru.point.core.ui.theme.errorLightHighContrast
import ru.point.core.ui.theme.errorLightMediumContrast
import ru.point.core.ui.theme.inverseOnSurfaceDark
import ru.point.core.ui.theme.inverseOnSurfaceDarkHighContrast
import ru.point.core.ui.theme.inverseOnSurfaceDarkMediumContrast
import ru.point.core.ui.theme.inverseOnSurfaceLight
import ru.point.core.ui.theme.inverseOnSurfaceLightHighContrast
import ru.point.core.ui.theme.inverseOnSurfaceLightMediumContrast
import ru.point.core.ui.theme.inversePrimaryDark
import ru.point.core.ui.theme.inversePrimaryDarkHighContrast
import ru.point.core.ui.theme.inversePrimaryDarkMediumContrast
import ru.point.core.ui.theme.inversePrimaryLight
import ru.point.core.ui.theme.inversePrimaryLightHighContrast
import ru.point.core.ui.theme.inversePrimaryLightMediumContrast
import ru.point.core.ui.theme.inverseSurfaceDark
import ru.point.core.ui.theme.inverseSurfaceDarkHighContrast
import ru.point.core.ui.theme.inverseSurfaceDarkMediumContrast
import ru.point.core.ui.theme.inverseSurfaceLight
import ru.point.core.ui.theme.inverseSurfaceLightHighContrast
import ru.point.core.ui.theme.inverseSurfaceLightMediumContrast
import ru.point.core.ui.theme.onBackgroundDark
import ru.point.core.ui.theme.onBackgroundDarkHighContrast
import ru.point.core.ui.theme.onBackgroundDarkMediumContrast
import ru.point.core.ui.theme.onBackgroundLight
import ru.point.core.ui.theme.onBackgroundLightHighContrast
import ru.point.core.ui.theme.onBackgroundLightMediumContrast
import ru.point.core.ui.theme.onErrorContainerDark
import ru.point.core.ui.theme.onErrorContainerDarkHighContrast
import ru.point.core.ui.theme.onErrorContainerDarkMediumContrast
import ru.point.core.ui.theme.onErrorContainerLight
import ru.point.core.ui.theme.onErrorContainerLightHighContrast
import ru.point.core.ui.theme.onErrorContainerLightMediumContrast
import ru.point.core.ui.theme.onErrorDark
import ru.point.core.ui.theme.onErrorDarkHighContrast
import ru.point.core.ui.theme.onErrorDarkMediumContrast
import ru.point.core.ui.theme.onErrorLight
import ru.point.core.ui.theme.onErrorLightHighContrast
import ru.point.core.ui.theme.onErrorLightMediumContrast
import ru.point.core.ui.theme.onPrimaryContainerDark
import ru.point.core.ui.theme.onPrimaryContainerDarkHighContrast
import ru.point.core.ui.theme.onPrimaryContainerDarkMediumContrast
import ru.point.core.ui.theme.onPrimaryContainerLight
import ru.point.core.ui.theme.onPrimaryContainerLightHighContrast
import ru.point.core.ui.theme.onPrimaryContainerLightMediumContrast
import ru.point.core.ui.theme.onPrimaryDark
import ru.point.core.ui.theme.onPrimaryDarkHighContrast
import ru.point.core.ui.theme.onPrimaryDarkMediumContrast
import ru.point.core.ui.theme.onPrimaryLight
import ru.point.core.ui.theme.onPrimaryLightHighContrast
import ru.point.core.ui.theme.onPrimaryLightMediumContrast
import ru.point.core.ui.theme.onSecondaryContainerDark
import ru.point.core.ui.theme.onSecondaryContainerDarkHighContrast
import ru.point.core.ui.theme.onSecondaryContainerDarkMediumContrast
import ru.point.core.ui.theme.onSecondaryContainerLight
import ru.point.core.ui.theme.onSecondaryContainerLightHighContrast
import ru.point.core.ui.theme.onSecondaryContainerLightMediumContrast
import ru.point.core.ui.theme.onSecondaryDark
import ru.point.core.ui.theme.onSecondaryDarkHighContrast
import ru.point.core.ui.theme.onSecondaryDarkMediumContrast
import ru.point.core.ui.theme.onSecondaryLight
import ru.point.core.ui.theme.onSecondaryLightHighContrast
import ru.point.core.ui.theme.onSecondaryLightMediumContrast
import ru.point.core.ui.theme.onSurfaceDark
import ru.point.core.ui.theme.onSurfaceDarkHighContrast
import ru.point.core.ui.theme.onSurfaceDarkMediumContrast
import ru.point.core.ui.theme.onSurfaceLight
import ru.point.core.ui.theme.onSurfaceLightHighContrast
import ru.point.core.ui.theme.onSurfaceLightMediumContrast
import ru.point.core.ui.theme.onSurfaceVariantDark
import ru.point.core.ui.theme.onSurfaceVariantDarkHighContrast
import ru.point.core.ui.theme.onSurfaceVariantDarkMediumContrast
import ru.point.core.ui.theme.onSurfaceVariantLight
import ru.point.core.ui.theme.onSurfaceVariantLightHighContrast
import ru.point.core.ui.theme.onSurfaceVariantLightMediumContrast
import ru.point.core.ui.theme.onTertiaryContainerDark
import ru.point.core.ui.theme.onTertiaryContainerDarkHighContrast
import ru.point.core.ui.theme.onTertiaryContainerDarkMediumContrast
import ru.point.core.ui.theme.onTertiaryContainerLight
import ru.point.core.ui.theme.onTertiaryContainerLightHighContrast
import ru.point.core.ui.theme.onTertiaryContainerLightMediumContrast
import ru.point.core.ui.theme.onTertiaryDark
import ru.point.core.ui.theme.onTertiaryDarkHighContrast
import ru.point.core.ui.theme.onTertiaryDarkMediumContrast
import ru.point.core.ui.theme.onTertiaryLight
import ru.point.core.ui.theme.onTertiaryLightHighContrast
import ru.point.core.ui.theme.onTertiaryLightMediumContrast
import ru.point.core.ui.theme.outlineDark
import ru.point.core.ui.theme.outlineDarkHighContrast
import ru.point.core.ui.theme.outlineDarkMediumContrast
import ru.point.core.ui.theme.outlineLight
import ru.point.core.ui.theme.outlineLightHighContrast
import ru.point.core.ui.theme.outlineLightMediumContrast
import ru.point.core.ui.theme.outlineVariantDark
import ru.point.core.ui.theme.outlineVariantDarkHighContrast
import ru.point.core.ui.theme.outlineVariantDarkMediumContrast
import ru.point.core.ui.theme.outlineVariantLight
import ru.point.core.ui.theme.outlineVariantLightHighContrast
import ru.point.core.ui.theme.outlineVariantLightMediumContrast
import ru.point.core.ui.theme.primaryContainerDark
import ru.point.core.ui.theme.primaryContainerDarkHighContrast
import ru.point.core.ui.theme.primaryContainerDarkMediumContrast
import ru.point.core.ui.theme.primaryContainerLight
import ru.point.core.ui.theme.primaryContainerLightHighContrast
import ru.point.core.ui.theme.primaryContainerLightMediumContrast
import ru.point.core.ui.theme.primaryDark
import ru.point.core.ui.theme.primaryDarkHighContrast
import ru.point.core.ui.theme.primaryDarkMediumContrast
import ru.point.core.ui.theme.primaryLight
import ru.point.core.ui.theme.primaryLightHighContrast
import ru.point.core.ui.theme.primaryLightMediumContrast
import ru.point.core.ui.theme.scrimDark
import ru.point.core.ui.theme.scrimDarkHighContrast
import ru.point.core.ui.theme.scrimDarkMediumContrast
import ru.point.core.ui.theme.scrimLight
import ru.point.core.ui.theme.scrimLightHighContrast
import ru.point.core.ui.theme.scrimLightMediumContrast
import ru.point.core.ui.theme.secondaryContainerDark
import ru.point.core.ui.theme.secondaryContainerDarkHighContrast
import ru.point.core.ui.theme.secondaryContainerDarkMediumContrast
import ru.point.core.ui.theme.secondaryContainerLight
import ru.point.core.ui.theme.secondaryContainerLightHighContrast
import ru.point.core.ui.theme.secondaryContainerLightMediumContrast
import ru.point.core.ui.theme.secondaryDark
import ru.point.core.ui.theme.secondaryDarkHighContrast
import ru.point.core.ui.theme.secondaryDarkMediumContrast
import ru.point.core.ui.theme.secondaryLight
import ru.point.core.ui.theme.secondaryLightHighContrast
import ru.point.core.ui.theme.secondaryLightMediumContrast
import ru.point.core.ui.theme.surfaceBrightDark
import ru.point.core.ui.theme.surfaceBrightDarkHighContrast
import ru.point.core.ui.theme.surfaceBrightDarkMediumContrast
import ru.point.core.ui.theme.surfaceBrightLight
import ru.point.core.ui.theme.surfaceBrightLightHighContrast
import ru.point.core.ui.theme.surfaceBrightLightMediumContrast
import ru.point.core.ui.theme.surfaceContainerDark
import ru.point.core.ui.theme.surfaceContainerDarkHighContrast
import ru.point.core.ui.theme.surfaceContainerDarkMediumContrast
import ru.point.core.ui.theme.surfaceContainerHighDark
import ru.point.core.ui.theme.surfaceContainerHighDarkHighContrast
import ru.point.core.ui.theme.surfaceContainerHighDarkMediumContrast
import ru.point.core.ui.theme.surfaceContainerHighLight
import ru.point.core.ui.theme.surfaceContainerHighLightHighContrast
import ru.point.core.ui.theme.surfaceContainerHighLightMediumContrast
import ru.point.core.ui.theme.surfaceContainerHighestDark
import ru.point.core.ui.theme.surfaceContainerHighestDarkHighContrast
import ru.point.core.ui.theme.surfaceContainerHighestDarkMediumContrast
import ru.point.core.ui.theme.surfaceContainerHighestLight
import ru.point.core.ui.theme.surfaceContainerHighestLightHighContrast
import ru.point.core.ui.theme.surfaceContainerHighestLightMediumContrast
import ru.point.core.ui.theme.surfaceContainerLight
import ru.point.core.ui.theme.surfaceContainerLightHighContrast
import ru.point.core.ui.theme.surfaceContainerLightMediumContrast
import ru.point.core.ui.theme.surfaceContainerLowDark
import ru.point.core.ui.theme.surfaceContainerLowDarkHighContrast
import ru.point.core.ui.theme.surfaceContainerLowDarkMediumContrast
import ru.point.core.ui.theme.surfaceContainerLowLight
import ru.point.core.ui.theme.surfaceContainerLowLightHighContrast
import ru.point.core.ui.theme.surfaceContainerLowLightMediumContrast
import ru.point.core.ui.theme.surfaceContainerLowestDark
import ru.point.core.ui.theme.surfaceContainerLowestDarkHighContrast
import ru.point.core.ui.theme.surfaceContainerLowestDarkMediumContrast
import ru.point.core.ui.theme.surfaceContainerLowestLight
import ru.point.core.ui.theme.surfaceContainerLowestLightHighContrast
import ru.point.core.ui.theme.surfaceContainerLowestLightMediumContrast
import ru.point.core.ui.theme.surfaceDark
import ru.point.core.ui.theme.surfaceDarkHighContrast
import ru.point.core.ui.theme.surfaceDarkMediumContrast
import ru.point.core.ui.theme.surfaceDimDark
import ru.point.core.ui.theme.surfaceDimDarkHighContrast
import ru.point.core.ui.theme.surfaceDimDarkMediumContrast
import ru.point.core.ui.theme.surfaceDimLight
import ru.point.core.ui.theme.surfaceDimLightHighContrast
import ru.point.core.ui.theme.surfaceDimLightMediumContrast
import ru.point.core.ui.theme.surfaceLight
import ru.point.core.ui.theme.surfaceLightHighContrast
import ru.point.core.ui.theme.surfaceLightMediumContrast
import ru.point.core.ui.theme.surfaceVariantDark
import ru.point.core.ui.theme.surfaceVariantDarkHighContrast
import ru.point.core.ui.theme.surfaceVariantDarkMediumContrast
import ru.point.core.ui.theme.surfaceVariantLight
import ru.point.core.ui.theme.surfaceVariantLightHighContrast
import ru.point.core.ui.theme.surfaceVariantLightMediumContrast
import ru.point.core.ui.theme.tertiaryContainerDark
import ru.point.core.ui.theme.tertiaryContainerDarkHighContrast
import ru.point.core.ui.theme.tertiaryContainerDarkMediumContrast
import ru.point.core.ui.theme.tertiaryContainerLight
import ru.point.core.ui.theme.tertiaryContainerLightHighContrast
import ru.point.core.ui.theme.tertiaryContainerLightMediumContrast
import ru.point.core.ui.theme.tertiaryDark
import ru.point.core.ui.theme.tertiaryDarkHighContrast
import ru.point.core.ui.theme.tertiaryDarkMediumContrast
import ru.point.core.ui.theme.tertiaryLight
import ru.point.core.ui.theme.tertiaryLightHighContrast
import ru.point.core.ui.theme.tertiaryLightMediumContrast

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
