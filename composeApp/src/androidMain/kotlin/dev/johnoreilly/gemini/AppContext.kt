package dev.johnoreilly.gemini

import android.content.Context

/**
 * Holds the application [Context] for the shared module's Android actual
 * implementations. Populated by the Android app's MainActivity on startup.
 */
object AppContext {
    lateinit var instance: Context
}
