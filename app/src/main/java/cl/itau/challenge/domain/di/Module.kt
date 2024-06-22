package cl.itau.challenge.domain.di

import cl.itau.challenge.domain.usecases.GetEarthquakesUseCase
import cl.itau.challenge.domain.usecases.SignInUseCase
import cl.itau.challenge.domain.usecases.SignUpUseCase
import cl.itau.challenge.domain.utilities.EmailValidator
import cl.itau.challenge.domain.utilities.LastNameValidator
import cl.itau.challenge.domain.utilities.NameValidator
import cl.itau.challenge.domain.utilities.PasswordValidator
import org.koin.dsl.module

val domainModule = module {

    factory {
        NameValidator()
    }

    factory {
        LastNameValidator()
    }

    factory {
        EmailValidator()
    }

    factory {
        PasswordValidator()
    }

    factory {
        GetEarthquakesUseCase(get(), get())
    }

    factory {
        SignUpUseCase(get(), get(), get(), get(), get(), get())
    }

    factory {
        SignInUseCase(get(), get(), get(), get())
    }
}