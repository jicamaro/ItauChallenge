package cl.itau.challenge.presentation

import android.app.Application
import cl.itau.challenge.appModule
import cl.itau.challenge.data.di.dataModule
import cl.itau.challenge.domain.di.domainModule
import cl.itau.challenge.presentation.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ChallengeApp: Application() {

    override fun onCreate() {
        super.onCreate()
        startInjection()
    }

    private fun startInjection() {
        startKoin {
            androidContext(this@ChallengeApp)
            modules(appModule, presentationModule, domainModule, dataModule)
        }
    }
}