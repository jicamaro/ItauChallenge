package cl.itau.challenge.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.itau.challenge.domain.model.GetEarthquakesRequest
import cl.itau.challenge.domain.model.SignInRequest
import cl.itau.challenge.domain.model.SignUpRequest
import cl.itau.challenge.domain.usecases.GetEarthquakesUseCase
import cl.itau.challenge.domain.usecases.SignInUseCase
import cl.itau.challenge.domain.usecases.SignUpUseCase
import cl.itau.challenge.domain.utilities.toModel
import cl.itau.challenge.presentation.model.ListItem
import cl.itau.challenge.presentation.model.UiState
import cl.itau.challenge.presentation.utilities.YEAR_FIRST_FORMAT
import cl.itau.challenge.presentation.utilities.toFormattedString
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class MainViewModel(
    private val signUpUseCase: SignUpUseCase,
    private val signInUseCase: SignInUseCase,
    private val getEarthquakesUseCase: GetEarthquakesUseCase
): ViewModel() {

    private val _signInUiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.SignInIdle)
    val signInUiState: StateFlow<UiState>
        get() = _signInUiState

    private val _dashboardUiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.PrivateDashboardLoading)
    val dashboardUiState: StateFlow<UiState>
        get() = _dashboardUiState

    private val _signUpUiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.SignUpIdle)
    val signUpUiState: StateFlow<UiState>
        get() = _signUpUiState

    fun signUp(id: String, name: String, lastName: String, email: String, password: String) {
        viewModelScope.launch {
            _signUpUiState.update { UiState.SignUpLoading }
            signUpUseCase.execute(
                SignUpRequest(
                    id = id,
                    name = name.trim(),
                    lastName = lastName.trim(),
                    email = email.trim(),
                    password = password.trim()
                )
            ).collect { response ->
                when {
                    response.isSuccess -> {
                        _signUpUiState.update { UiState.SignUpSuccessful }
                    }
                    response.isFailure -> {
                        _signUpUiState.update { UiState.SignUpError(
                            throwable = response.exceptionOrNull()
                        ) }
                    }
                }
            }
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _signInUiState.update { UiState.SignInLoading }
            signInUseCase.execute(parameters = SignInRequest(
                email = email,
                password = password
            )).collect { response ->
                when {
                    response.isSuccess -> {
                        _signInUiState.update { UiState.SignInSuccessful }
                    }
                    response.isFailure -> {
                        _signInUiState.update { UiState.SignInError(
                            throwable = response.exceptionOrNull()
                        ) }
                    }
                }
            }
        }
    }

    fun getEarthquakes(
        startTime: LocalDate,
        endTime: LocalDate,
        limit: Int = 10
    ) {
        viewModelScope.launch {
            _dashboardUiState.update { UiState.PrivateDashboardLoading }
            getEarthquakesUseCase.execute(
                parameters = GetEarthquakesRequest(
                    startTime = startTime.toFormattedString(YEAR_FIRST_FORMAT),
                    endTime = endTime.toFormattedString(YEAR_FIRST_FORMAT),
                    limit = limit
                )
            ).collect { response ->
                when {
                    response.isSuccess -> {
                        val list = response.getOrNull()?.map {
                            it.toModel()
                        }.orEmpty()
                        _dashboardUiState.update { UiState.PrivateDashboard(
                            mutableListOf<ListItem>().apply {
                                add(0, ListItem.Header(startTime, endTime))
                                addAll(list.map {
                                    ListItem.Item(it)
                                })
                            }
                        ) }
                    }
                    response.isFailure -> {
                        _dashboardUiState.update { UiState.PrivateDashboardError(
                            response.exceptionOrNull()
                        ) }
                    }
                }
            }
        }
    }
}