package nz.rita.rcc

import android.app.Application
import nz.rita.rcc.utils.TypefaceUtil
import timber.log.Timber

/**
 * Created by Shvarev Mikhail on 1/26/2018.
 */
class AppMain : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        TypefaceUtil.initialize(this)
    }
}