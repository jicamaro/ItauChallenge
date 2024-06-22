package cl.itau.challenge

import cl.itau.challenge.utilities.DispatcherFactory
import cl.itau.challenge.utilities.RuntimeDispatcherFactory
import cl.itau.challenge.BuildConfig as AppBuildConfig
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val BASE_URL = "BASE_URL"
const val USER_DATABASE = "USER-DB"
const val USER_DATABASE_VALUE = "user-db"

val appModule = module {

    single<String>(named(BASE_URL)) {
        AppBuildConfig.API_BASE_URL
    }

    single<String>(named(USER_DATABASE)) {
        USER_DATABASE_VALUE
    }

    single<DispatcherFactory> {
        RuntimeDispatcherFactory()
    }
}