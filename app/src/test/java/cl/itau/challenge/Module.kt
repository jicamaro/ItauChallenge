package cl.itau.challenge

import cl.itau.challenge.utilities.DispatcherFactory
import cl.itau.challenge.utilities.TestingDispatcherFactory
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModuleTest = module {

    single<String>(named(BASE_URL)) {
        BuildConfig.API_BASE_URL
    }

    single<String>(named(USER_DATABASE)) {
        USER_DATABASE_VALUE
    }

    single<DispatcherFactory> {
        TestingDispatcherFactory()
    }
}