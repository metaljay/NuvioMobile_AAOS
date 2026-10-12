package com.nuvio.app.core.ui

import androidx.compose.ui.unit.dp

// AAOS fork-owned file (C5 AAOS readability): the larger icon sizes used on Automotive.

internal fun NuvioThemeTokens.withLargerIcons(): NuvioThemeTokens =
    copy(
        icons = icons.copy(
            xs = 16.dp,
            sm = 20.dp,
            md = 24.dp,
            lg = 30.dp,
            xl = 38.dp,
            xxl = 48.dp,
        ),
        components = components.copy(navIconSize = 40.dp),
    )
