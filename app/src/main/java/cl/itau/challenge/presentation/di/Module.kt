package cl.itau.challenge.presentation.di

import cl.itau.challenge.presentation.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {

    viewModel {
        MainViewModel(get(), get(), get())
    }
}