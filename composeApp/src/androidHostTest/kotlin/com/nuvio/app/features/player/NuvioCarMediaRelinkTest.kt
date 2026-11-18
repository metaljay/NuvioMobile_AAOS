package com.nuvio.app.features.player

import android.app.Application
import android.content.ComponentName
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

// AAOS fork (C9): the first time a process plays and the car has not linked, the other launchable twin
// takes over (once), so the car sees a new media source and rebuilds the media card's link.
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class NuvioCarMediaRelinkTest {
    private lateinit var app: Application
    private lateinit var twinA: ComponentName
    private lateinit var twinB: ComponentName
    private lateinit var plain: ComponentName

    @Before
    fun prepare() {
        app = RuntimeEnvironment.getApplication()
        NuvioCarMediaSession.resetRelinkStateForTest()
        twinA = ComponentName(app.packageName, NuvioCarMediaLaunchableService::class.java.name)
        twinB = ComponentName(app.packageName, NuvioCarMediaLaunchableServiceB::class.java.name)
        plain = ComponentName(app.packageName, NuvioCarMediaBrowserService::class.java.name)
    }

    private fun state(component: ComponentName) = app.packageManager.getComponentEnabledSetting(component)

    private fun carWithFeature(enabled: Boolean = true) {
        shadowOf(app.packageManager).setSystemFeature(NuvioCarMediaSession.FEATURE_CAR_TEMPLATES_HOST_MEDIA, enabled)
    }

    private fun sessionNames(): String? = NuvioCarMediaSession.lastNamedBrowseService

    @Test
    fun noLinkSwapsOnceAndTheExtraNamesTheNewTwin() {
        carWithFeature()
        NuvioCarMediaSession.selectBrowseService(app)

        NuvioCarMediaSession.relinkIfNeeded(app)

        assertEquals(PackageManager.COMPONENT_ENABLED_STATE_ENABLED, state(twinB))
        assertEquals(PackageManager.COMPONENT_ENABLED_STATE_DISABLED, state(twinA))
        assertEquals(PackageManager.COMPONENT_ENABLED_STATE_DISABLED, state(plain))
        assertEquals(NuvioCarMediaLaunchableServiceB::class.java, NuvioCarMediaSession.activeBrowseServiceClass(app))
        assertEquals(NuvioCarMediaLaunchableServiceB::class.java.name, sessionNames())
    }

    @Test
    fun aSecondPlayDoesNotSwapAgain() {
        carWithFeature()
        NuvioCarMediaSession.selectBrowseService(app)
        NuvioCarMediaSession.relinkIfNeeded(app)

        NuvioCarMediaSession.relinkIfNeeded(app)

        assertEquals(PackageManager.COMPONENT_ENABLED_STATE_ENABLED, state(twinB))
        assertEquals(PackageManager.COMPONENT_ENABLED_STATE_DISABLED, state(twinA))
    }

    @Test
    fun carLinkedMeansNoSwap() {
        carWithFeature()
        NuvioCarMediaSession.selectBrowseService(app)
        NuvioCarMediaSession.markCarLinked()

        NuvioCarMediaSession.relinkIfNeeded(app)

        assertEquals(PackageManager.COMPONENT_ENABLED_STATE_ENABLED, state(twinA))
        assertEquals(PackageManager.COMPONENT_ENABLED_STATE_DISABLED, state(twinB))
    }

    @Test
    fun theBrowseServiceMarksTheCarLinkedOnlyForOtherPackages() {
        carWithFeature()
        NuvioCarMediaSession.selectBrowseService(app)
        val service = Robolectric.buildService(NuvioCarMediaLaunchableService::class.java).create().get()

        service.onGetRoot(app.packageName, 1, null)
        NuvioCarMediaSession.relinkIfNeeded(app)
        assertEquals(PackageManager.COMPONENT_ENABLED_STATE_ENABLED, state(twinB))

        NuvioCarMediaSession.resetRelinkStateForTest()
        service.onGetRoot("com.android.car.media", 2, null)
        NuvioCarMediaSession.relinkIfNeeded(app)
        assertEquals(PackageManager.COMPONENT_ENABLED_STATE_ENABLED, state(twinB))
    }

    @Test
    fun noFeatureMeansNoSwap() {
        carWithFeature(enabled = false)
        NuvioCarMediaSession.selectBrowseService(app)

        NuvioCarMediaSession.relinkIfNeeded(app)

        assertEquals(PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, state(twinA))
        assertEquals(PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, state(twinB))
        assertEquals(NuvioCarMediaBrowserService::class.java, NuvioCarMediaSession.activeBrowseServiceClass(app))
    }

    @Test
    fun startUpKeepsWhicheverTwinIsOn() {
        carWithFeature()
        NuvioCarMediaSession.selectBrowseService(app)
        NuvioCarMediaSession.relinkIfNeeded(app)

        NuvioCarMediaSession.selectBrowseService(app)

        assertEquals(PackageManager.COMPONENT_ENABLED_STATE_ENABLED, state(twinB))
        assertEquals(PackageManager.COMPONENT_ENABLED_STATE_DISABLED, state(twinA))
        assertEquals(PackageManager.COMPONENT_ENABLED_STATE_DISABLED, state(plain))
    }
}
