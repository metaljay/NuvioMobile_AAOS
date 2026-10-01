package com.JF_Nuvio

import androidx.compose.runtime.Composable
import com.JF_Nuvio.features.downloads.DownloadItem
import com.JF_Nuvio.features.downloads.DownloadsScreen
import com.JF_Nuvio.navigation.DownloadShowRoute
import com.JF_Nuvio.navigation.DownloadsRoute
import com.JF_Nuvio.navigation.NuvioNavigator

@Composable
internal fun DownloadsDestination(
    route: DownloadsRoute,
    navController: NuvioNavigator,
    useNativeNavigation: Boolean,
    onOpenDownload: (DownloadItem) -> Unit,
) {
    val onBack = rememberGuardedPopBackStack(navController, route)
    DownloadsScreen(
        onBack = onBack,
        onOpenDownload = onOpenDownload,
        onNavigateToShow = if (useNativeNavigation) {
            { showId, title -> navController.navigate(DownloadShowRoute(showId, title)) }
        } else {
            null
        },
    )
}

@Composable
internal fun DownloadShowDestination(
    route: DownloadShowRoute,
    navController: NuvioNavigator,
    onOpenDownload: (DownloadItem) -> Unit,
) {
    val onBack = rememberGuardedPopBackStack(navController, route)
    DownloadsScreen(
        onBack = onBack,
        onOpenDownload = onOpenDownload,
        initialShowId = route.showId,
        onBackFromShow = onBack,
    )
}
