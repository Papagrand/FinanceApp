package com.example.compose

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowInsetsControllerCompat
import ru.point.homework1.ui.theme.AppTypography
import ru.point.homework1.ui.theme.backgroundDark
import ru.point.homework1.ui.theme.backgroundDarkHighContrast
import ru.point.homework1.ui.theme.backgroundDarkMediumContrast
import ru.point.homework1.ui.theme.backgroundLight
import ru.point.homework1.ui.theme.backgroundLightHighContrast
import ru.point.homework1.ui.theme.backgroundLightMediumContrast
import ru.point.homework1.ui.theme.errorContainerDark
import ru.point.homework1.ui.theme.errorContainerDarkHighContrast
import ru.point.homework1.ui.theme.errorContainerDarkMediumContrast
import ru.point.homework1.ui.theme.errorContainerLight
import ru.point.homework1.ui.theme.errorContainerLightHighContrast
import ru.point.homework1.ui.theme.errorContainerLightMediumContrast
import ru.point.homework1.ui.theme.errorDark
import ru.point.homework1.ui.theme.errorDarkHighContrast
import ru.point.homework1.ui.theme.errorDarkMediumContrast
import ru.point.homework1.ui.theme.errorLight
import ru.point.homework1.ui.theme.errorLightHighContrast
import ru.point.homework1.ui.theme.errorLightMediumContrast
import ru.point.homework1.ui.theme.inverseOnSurfaceDark
import ru.point.homework1.ui.theme.inverseOnSurfaceDarkHighContrast
import ru.point.homework1.ui.theme.inverseOnSurfaceDarkMediumContrast
import ru.point.homework1.ui.theme.inverseOnSurfaceLight
import ru.point.homework1.ui.theme.inverseOnSurfaceLightHighContrast
import ru.point.homework1.ui.theme.inverseOnSurfaceLightMediumContrast
import ru.point.homework1.ui.theme.inversePrimaryDark
import ru.point.homework1.ui.theme.inversePrimaryDarkHighContrast
import ru.point.homework1.ui.theme.inversePrimaryDarkMediumContrast
import ru.point.homework1.ui.theme.inversePrimaryLight
import ru.point.homework1.ui.theme.inversePrimaryLightHighContrast
import ru.point.homework1.ui.theme.inversePrimaryLightMediumContrast
import ru.point.homework1.ui.theme.inverseSurfaceDark
import ru.point.homework1.ui.theme.inverseSurfaceDarkHighContrast
import ru.point.homework1.ui.theme.inverseSurfaceDarkMediumContrast
import ru.point.homework1.ui.theme.inverseSurfaceLight
import ru.point.homework1.ui.theme.inverseSurfaceLightHighContrast
import ru.point.homework1.ui.theme.inverseSurfaceLightMediumContrast
import ru.point.homework1.ui.theme.onBackgroundDark
import ru.point.homework1.ui.theme.onBackgroundDarkHighContrast
import ru.point.homework1.ui.theme.onBackgroundDarkMediumContrast
import ru.point.homework1.ui.theme.onBackgroundLight
import ru.point.homework1.ui.theme.onBackgroundLightHighContrast
import ru.point.homework1.ui.theme.onBackgroundLightMediumContrast
import ru.point.homework1.ui.theme.onErrorContainerDark
import ru.point.homework1.ui.theme.onErrorContainerDarkHighContrast
import ru.point.homework1.ui.theme.onErrorContainerDarkMediumContrast
import ru.point.homework1.ui.theme.onErrorContainerLight
import ru.point.homework1.ui.theme.onErrorContainerLightHighContrast
import ru.point.homework1.ui.theme.onErrorContainerLightMediumContrast
import ru.point.homework1.ui.theme.onErrorDark
import ru.point.homework1.ui.theme.onErrorDarkHighContrast
import ru.point.homework1.ui.theme.onErrorDarkMediumContrast
import ru.point.homework1.ui.theme.onErrorLight
import ru.point.homework1.ui.theme.onErrorLightHighContrast
import ru.point.homework1.ui.theme.onErrorLightMediumContrast
import ru.point.homework1.ui.theme.onPrimaryContainerDark
import ru.point.homework1.ui.theme.onPrimaryContainerDarkHighContrast
import ru.point.homework1.ui.theme.onPrimaryContainerDarkMediumContrast
import ru.point.homework1.ui.theme.onPrimaryContainerLight
import ru.point.homework1.ui.theme.onPrimaryContainerLightHighContrast
import ru.point.homework1.ui.theme.onPrimaryContainerLightMediumContrast
import ru.point.homework1.ui.theme.onPrimaryDark
import ru.point.homework1.ui.theme.onPrimaryDarkHighContrast
import ru.point.homework1.ui.theme.onPrimaryDarkMediumContrast
import ru.point.homework1.ui.theme.onPrimaryLight
import ru.point.homework1.ui.theme.onPrimaryLightHighContrast
import ru.point.homework1.ui.theme.onPrimaryLightMediumContrast
import ru.point.homework1.ui.theme.onSecondaryContainerDark
import ru.point.homework1.ui.theme.onSecondaryContainerDarkHighContrast
import ru.point.homework1.ui.theme.onSecondaryContainerDarkMediumContrast
import ru.point.homework1.ui.theme.onSecondaryContainerLight
import ru.point.homework1.ui.theme.onSecondaryContainerLightHighContrast
import ru.point.homework1.ui.theme.onSecondaryContainerLightMediumContrast
import ru.point.homework1.ui.theme.onSecondaryDark
import ru.point.homework1.ui.theme.onSecondaryDarkHighContrast
import ru.point.homework1.ui.theme.onSecondaryDarkMediumContrast
import ru.point.homework1.ui.theme.onSecondaryLight
import ru.point.homework1.ui.theme.onSecondaryLightHighContrast
import ru.point.homework1.ui.theme.onSecondaryLightMediumContrast
import ru.point.homework1.ui.theme.onSurfaceDark
import ru.point.homework1.ui.theme.onSurfaceDarkHighContrast
import ru.point.homework1.ui.theme.onSurfaceDarkMediumContrast
import ru.point.homework1.ui.theme.onSurfaceLight
import ru.point.homework1.ui.theme.onSurfaceLightHighContrast
import ru.point.homework1.ui.theme.onSurfaceLightMediumContrast
import ru.point.homework1.ui.theme.onSurfaceVariantDark
import ru.point.homework1.ui.theme.onSurfaceVariantDarkHighContrast
import ru.point.homework1.ui.theme.onSurfaceVariantDarkMediumContrast
import ru.point.homework1.ui.theme.onSurfaceVariantLight
import ru.point.homework1.ui.theme.onSurfaceVariantLightHighContrast
import ru.point.homework1.ui.theme.onSurfaceVariantLightMediumContrast
import ru.point.homework1.ui.theme.onTertiaryContainerDark
import ru.point.homework1.ui.theme.onTertiaryContainerDarkHighContrast
import ru.point.homework1.ui.theme.onTertiaryContainerDarkMediumContrast
import ru.point.homework1.ui.theme.onTertiaryContainerLight
import ru.point.homework1.ui.theme.onTertiaryContainerLightHighContrast
import ru.point.homework1.ui.theme.onTertiaryContainerLightMediumContrast
import ru.point.homework1.ui.theme.onTertiaryDark
import ru.point.homework1.ui.theme.onTertiaryDarkHighContrast
import ru.point.homework1.ui.theme.onTertiaryDarkMediumContrast
import ru.point.homework1.ui.theme.onTertiaryLight
import ru.point.homework1.ui.theme.onTertiaryLightHighContrast
import ru.point.homework1.ui.theme.onTertiaryLightMediumContrast
import ru.point.homework1.ui.theme.outlineDark
import ru.point.homework1.ui.theme.outlineDarkHighContrast
import ru.point.homework1.ui.theme.outlineDarkMediumContrast
import ru.point.homework1.ui.theme.outlineLight
import ru.point.homework1.ui.theme.outlineLightHighContrast
import ru.point.homework1.ui.theme.outlineLightMediumContrast
import ru.point.homework1.ui.theme.outlineVariantDark
import ru.point.homework1.ui.theme.outlineVariantDarkHighContrast
import ru.point.homework1.ui.theme.outlineVariantDarkMediumContrast
import ru.point.homework1.ui.theme.outlineVariantLight
import ru.point.homework1.ui.theme.outlineVariantLightHighContrast
import ru.point.homework1.ui.theme.outlineVariantLightMediumContrast
import ru.point.homework1.ui.theme.primaryContainerDark
import ru.point.homework1.ui.theme.primaryContainerDarkHighContrast
import ru.point.homework1.ui.theme.primaryContainerDarkMediumContrast
import ru.point.homework1.ui.theme.primaryContainerLight
import ru.point.homework1.ui.theme.primaryContainerLightHighContrast
import ru.point.homework1.ui.theme.primaryContainerLightMediumContrast
import ru.point.homework1.ui.theme.primaryDark
import ru.point.homework1.ui.theme.primaryDarkHighContrast
import ru.point.homework1.ui.theme.primaryDarkMediumContrast
import ru.point.homework1.ui.theme.primaryLight
import ru.point.homework1.ui.theme.primaryLightHighContrast
import ru.point.homework1.ui.theme.primaryLightMediumContrast
import ru.point.homework1.ui.theme.scrimDark
import ru.point.homework1.ui.theme.scrimDarkHighContrast
import ru.point.homework1.ui.theme.scrimDarkMediumContrast
import ru.point.homework1.ui.theme.scrimLight
import ru.point.homework1.ui.theme.scrimLightHighContrast
import ru.point.homework1.ui.theme.scrimLightMediumContrast
import ru.point.homework1.ui.theme.secondaryContainerDark
import ru.point.homework1.ui.theme.secondaryContainerDarkHighContrast
import ru.point.homework1.ui.theme.secondaryContainerDarkMediumContrast
import ru.point.homework1.ui.theme.secondaryContainerLight
import ru.point.homework1.ui.theme.secondaryContainerLightHighContrast
import ru.point.homework1.ui.theme.secondaryContainerLightMediumContrast
import ru.point.homework1.ui.theme.secondaryDark
import ru.point.homework1.ui.theme.secondaryDarkHighContrast
import ru.point.homework1.ui.theme.secondaryDarkMediumContrast
import ru.point.homework1.ui.theme.secondaryLight
import ru.point.homework1.ui.theme.secondaryLightHighContrast
import ru.point.homework1.ui.theme.secondaryLightMediumContrast
import ru.point.homework1.ui.theme.surfaceBrightDark
import ru.point.homework1.ui.theme.surfaceBrightDarkHighContrast
import ru.point.homework1.ui.theme.surfaceBrightDarkMediumContrast
import ru.point.homework1.ui.theme.surfaceBrightLight
import ru.point.homework1.ui.theme.surfaceBrightLightHighContrast
import ru.point.homework1.ui.theme.surfaceBrightLightMediumContrast
import ru.point.homework1.ui.theme.surfaceContainerDark
import ru.point.homework1.ui.theme.surfaceContainerDarkHighContrast
import ru.point.homework1.ui.theme.surfaceContainerDarkMediumContrast
import ru.point.homework1.ui.theme.surfaceContainerHighDark
import ru.point.homework1.ui.theme.surfaceContainerHighDarkHighContrast
import ru.point.homework1.ui.theme.surfaceContainerHighDarkMediumContrast
import ru.point.homework1.ui.theme.surfaceContainerHighLight
import ru.point.homework1.ui.theme.surfaceContainerHighLightHighContrast
import ru.point.homework1.ui.theme.surfaceContainerHighLightMediumContrast
import ru.point.homework1.ui.theme.surfaceContainerHighestDark
import ru.point.homework1.ui.theme.surfaceContainerHighestDarkHighContrast
import ru.point.homework1.ui.theme.surfaceContainerHighestDarkMediumContrast
import ru.point.homework1.ui.theme.surfaceContainerHighestLight
import ru.point.homework1.ui.theme.surfaceContainerHighestLightHighContrast
import ru.point.homework1.ui.theme.surfaceContainerHighestLightMediumContrast
import ru.point.homework1.ui.theme.surfaceContainerLight
import ru.point.homework1.ui.theme.surfaceContainerLightHighContrast
import ru.point.homework1.ui.theme.surfaceContainerLightMediumContrast
import ru.point.homework1.ui.theme.surfaceContainerLowDark
import ru.point.homework1.ui.theme.surfaceContainerLowDarkHighContrast
import ru.point.homework1.ui.theme.surfaceContainerLowDarkMediumContrast
import ru.point.homework1.ui.theme.surfaceContainerLowLight
import ru.point.homework1.ui.theme.surfaceContainerLowLightHighContrast
import ru.point.homework1.ui.theme.surfaceContainerLowLightMediumContrast
import ru.point.homework1.ui.theme.surfaceContainerLowestDark
import ru.point.homework1.ui.theme.surfaceContainerLowestDarkHighContrast
import ru.point.homework1.ui.theme.surfaceContainerLowestDarkMediumContrast
import ru.point.homework1.ui.theme.surfaceContainerLowestLight
import ru.point.homework1.ui.theme.surfaceContainerLowestLightHighContrast
import ru.point.homework1.ui.theme.surfaceContainerLowestLightMediumContrast
import ru.point.homework1.ui.theme.surfaceDark
import ru.point.homework1.ui.theme.surfaceDarkHighContrast
import ru.point.homework1.ui.theme.surfaceDarkMediumContrast
import ru.point.homework1.ui.theme.surfaceDimDark
import ru.point.homework1.ui.theme.surfaceDimDarkHighContrast
import ru.point.homework1.ui.theme.surfaceDimDarkMediumContrast
import ru.point.homework1.ui.theme.surfaceDimLight
import ru.point.homework1.ui.theme.surfaceDimLightHighContrast
import ru.point.homework1.ui.theme.surfaceDimLightMediumContrast
import ru.point.homework1.ui.theme.surfaceLight
import ru.point.homework1.ui.theme.surfaceLightHighContrast
import ru.point.homework1.ui.theme.surfaceLightMediumContrast
import ru.point.homework1.ui.theme.surfaceVariantDark
import ru.point.homework1.ui.theme.surfaceVariantDarkHighContrast
import ru.point.homework1.ui.theme.surfaceVariantDarkMediumContrast
import ru.point.homework1.ui.theme.surfaceVariantLight
import ru.point.homework1.ui.theme.surfaceVariantLightHighContrast
import ru.point.homework1.ui.theme.surfaceVariantLightMediumContrast
import ru.point.homework1.ui.theme.tertiaryContainerDark
import ru.point.homework1.ui.theme.tertiaryContainerDarkHighContrast
import ru.point.homework1.ui.theme.tertiaryContainerDarkMediumContrast
import ru.point.homework1.ui.theme.tertiaryContainerLight
import ru.point.homework1.ui.theme.tertiaryContainerLightHighContrast
import ru.point.homework1.ui.theme.tertiaryContainerLightMediumContrast
import ru.point.homework1.ui.theme.tertiaryDark
import ru.point.homework1.ui.theme.tertiaryDarkHighContrast
import ru.point.homework1.ui.theme.tertiaryDarkMediumContrast
import ru.point.homework1.ui.theme.tertiaryLight
import ru.point.homework1.ui.theme.tertiaryLightHighContrast
import ru.point.homework1.ui.theme.tertiaryLightMediumContrast

private val lightScheme = lightColorScheme(
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

private val darkScheme = darkColorScheme(
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

private val mediumContrastLightColorScheme = lightColorScheme(
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

private val highContrastLightColorScheme = lightColorScheme(
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

private val mediumContrastDarkColorScheme = darkColorScheme(
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

private val highContrastDarkColorScheme = darkColorScheme(
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
    val onColorContainer: Color
)

val unspecified_scheme = ColorFamily(
    Color.Unspecified, Color.Unspecified, Color.Unspecified, Color.Unspecified
)

@Composable
fun HomeWork1Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable() () -> Unit
) {

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> darkScheme
        else -> lightScheme
    }


    MaterialTheme(
        colorScheme = colorScheme,
        typography  = AppTypography
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

