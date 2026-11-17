package com.nuvio.app.features.player

import android.app.Activity
import android.os.Bundle

// AAOS fork: no screen of its own. On cars reporting android.software.car.templates_host.media the
// car's media app opens this activity (named in NuvioCarMediaLaunchableService's
// CalMediaActivityComponent meta-data) instead of its own media screen; it opens Nuvio and closes.
class NuvioCarMediaEntryActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NuvioCarMediaSession.buildLaunchIntent(this)?.let(::startActivity)
        finish()
    }
}
