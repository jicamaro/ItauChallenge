package cl.itau.challenge.utilities

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@OptIn(ExperimentalCoroutinesApi::class)
class TestingDispatcherFactory: DispatcherFactory {

    override val ioDispatcher: CoroutineDispatcher
        get() = UnconfinedTestDispatcher()
    override val mainDispatcher: CoroutineDispatcher
        get() = UnconfinedTestDispatcher()
    override val unconfinedDispatcher: CoroutineDispatcher
        get() = UnconfinedTestDispatcher()
}