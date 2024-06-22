package cl.itau.challenge.presentation

import android.app.Application
import cl.itau.challenge.appModuleTest
import cl.itau.challenge.data.di.dataModule
import cl.itau.challenge.domain.di.domainModule
import cl.itau.challenge.presentation.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ChallengeAppTest: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ChallengeAppTest)
            modules(appModuleTest, dataModule, domainModule, presentationModule)
        }
    }
}