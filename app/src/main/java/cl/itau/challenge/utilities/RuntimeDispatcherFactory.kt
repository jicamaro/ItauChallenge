package cl.itau.challenge.utilities

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class RuntimeDispatcherFactory: DispatcherFactory {

    override val ioDispatcher: CoroutineDispatcher
        get() = Dispatchers.IO

    override val mainDispatcher: CoroutineDispatcher
        get() = Dispatchers.Main

    override val unconfinedDispatcher: CoroutineDispatcher
        get() = Dispatchers.Unconfined
}