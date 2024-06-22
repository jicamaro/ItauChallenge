package cl.itau.challenge.utilities

import kotlinx.coroutines.CoroutineDispatcher

interface DispatcherFactory {

    val ioDispatcher: CoroutineDispatcher
    val mainDispatcher: CoroutineDispatcher
    val unconfinedDispatcher: CoroutineDispatcher
}