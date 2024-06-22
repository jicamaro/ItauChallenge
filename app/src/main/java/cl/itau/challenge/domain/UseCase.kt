package cl.itau.challenge.domain

import cl.itau.challenge.utilities.DispatcherFactory
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

abstract class UseCase<T, V>(
    private val dispatcherFactory: DispatcherFactory
) {

    abstract suspend fun request(parameters: T): V

    suspend fun execute(parameters: T) = flow {
        emit(Result.success(request(parameters)))
    }.catch {
        emit(Result.failure(it))
    }.flowOn(dispatcherFactory.ioDispatcher)
}