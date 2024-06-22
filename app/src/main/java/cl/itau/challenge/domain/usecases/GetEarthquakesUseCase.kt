package cl.itau.challenge.domain.usecases

import cl.itau.challenge.data.repository.EarthquakeRepository
import cl.itau.challenge.domain.UseCase
import cl.itau.challenge.domain.model.Earthquake
import cl.itau.challenge.domain.model.GetEarthquakesRequest
import cl.itau.challenge.utilities.DispatcherFactory

class GetEarthquakesUseCase(
    private val earthquakeRepository: EarthquakeRepository,
    dispatcherFactory: DispatcherFactory
): UseCase<GetEarthquakesRequest, List<Earthquake>>(dispatcherFactory = dispatcherFactory) {

    override suspend fun request(parameters: GetEarthquakesRequest) =
        earthquakeRepository.getEarthquakes(
            parameters.startTime,
            parameters.endTime,
            parameters.format,
            parameters.limit,
            parameters.orderBy
        )
}