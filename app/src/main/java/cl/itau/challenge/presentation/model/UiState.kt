package cl.itau.challenge.presentation.model

sealed interface UiState {

    data object SignUpIdle: UiState
    data object SignUpLoading: UiState
    data object SignUpSuccessful: UiState
    data class SignUpError(val throwable: Throwable? = null): UiState

    data object PrivateDashboardLoading: UiState
    data class PrivateDashboard(
        val listItems: List<ListItem>
    ) : UiState
    data class PrivateDashboardError(val throwable: Throwable? = null): UiState

    data object SignInIdle: UiState
    data object SignInLoading: UiState
    data object SignInSuccessful : UiState
    data class SignInError(val throwable: Throwable? = null): UiState
}