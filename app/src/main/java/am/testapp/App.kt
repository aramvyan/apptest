package am.testapp

import android.app.Application
import android.content.Context

class App: Application() {

    companion object {
        lateinit var instance: App
            private set

        fun applicationContext(): Context {
            return instance.applicationContext
        }
    }

    init {
        instance = this
    }

}