package com.nuvio.app.features.player

import android.app.Application
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

// AAOS fork (C9): exactly one browse service is on; the opted-in one only on cars whose media app
// redirects to NuvioCarMediaEntryActivity.
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class NuvioCarMediaBrowseServiceSelectionTest {
    private lateinit var app: Application
    private lateinit var launchable: ComponentName
    private lateinit var plain: ComponentName

    @Before
    fun prepare() {
        app = RuntimeEnvironment.getApplication()
        launchable = ComponentName(app.packageName, NuvioCarMediaLaunchableService::class.java.name)
        plain = ComponentName(app.packageName, NuvioCarMediaBrowserService::class.java.name)
    }

    private fun state(component: ComponentName) = app.packageManager.getComponentEnabledSetting(component)

    @Test
    fun carWithTemplatesHostMediaUsesTheOptedInService() {
        shadowOf(app.packageManager).setSystemFeature(NuvioCarMediaSession.FEATURE_CAR_TEMPLATES_HOST_MEDIA, true)

        NuvioCarMediaSession.selectBrowseService(app)

        assertEquals(PackageManager.COMPONENT_ENABLED_STATE_ENABLED, state(launchable))
        assertEquals(PackageManager.COMPONENT_ENABLED_STATE_DISABLED, state(plain))
        assertEquals(NuvioCarMediaLaunchableService::class.java, NuvioCarMediaSession.activeBrowseServiceClass(app))
    }

    @Test
    fun carWithoutTheFeatureKeepsThePlainService() {
        shadowOf(app.packageManager).setSystemFeature(NuvioCarMediaSession.FEATURE_CAR_TEMPLATES_HOST_MEDIA, true)
        NuvioCarMediaSession.selectBrowseService(app)
        shadowOf(app.packageManager).setSystemFeature(NuvioCarMediaSession.FEATURE_CAR_TEMPLATES_HOST_MEDIA, false)

        NuvioCarMediaSession.selectBrowseService(app)

        assertEquals(PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, state(launchable))
        assertEquals(PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, state(plain))
        assertEquals(NuvioCarMediaBrowserService::class.java, NuvioCarMediaSession.activeBrowseServiceClass(app))
    }

    @Test
    fun beforeSelectionThePlainServiceIsActiveEvenOnACarWithTheFeature() {
        shadowOf(app.packageManager).setSystemFeature(NuvioCarMediaSession.FEATURE_CAR_TEMPLATES_HOST_MEDIA, true)

        assertEquals(NuvioCarMediaBrowserService::class.java, NuvioCarMediaSession.activeBrowseServiceClass(app))
    }

    @Test
    fun selectingTwiceChangesNothing() {
        shadowOf(app.packageManager).setSystemFeature(NuvioCarMediaSession.FEATURE_CAR_TEMPLATES_HOST_MEDIA, true)
        NuvioCarMediaSession.selectBrowseService(app)
        val before = state(launchable) to state(plain)

        NuvioCarMediaSession.selectBrowseService(app)

        assertEquals(before, state(launchable) to state(plain))
    }

    @Test
    fun entryActivityOpensTheAppAndCloses() {
        val launcher = ComponentName(app.packageName, "com.nuvio.app.launcher.AppIconDefault")
        shadowOf(app.packageManager).addActivityIfNotPresent(launcher)
        shadowOf(app.packageManager).addIntentFilterForActivity(
            launcher,
            android.content.IntentFilter(Intent.ACTION_MAIN).apply { addCategory(Intent.CATEGORY_LAUNCHER) },
        )

        val controller = Robolectric.buildActivity(NuvioCarMediaEntryActivity::class.java).create()

        val started = assertNotNull(shadowOf(controller.get()).nextStartedActivity)
        assertEquals(launcher, started.component)
        assertTrue(controller.get().isFinishing)
    }
}
