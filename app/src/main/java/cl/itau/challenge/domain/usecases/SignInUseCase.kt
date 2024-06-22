package cl.itau.challenge.domain.usecases

import cl.itau.challenge.data.repository.UserRepository
import cl.itau.challenge.domain.UseCase
import cl.itau.challenge.domain.model.InvalidEmailException
import cl.itau.challenge.domain.model.InvalidPasswordException
import cl.itau.challenge.domain.model.SignInRequest
import cl.itau.challenge.domain.utilities.EmailValidator
import cl.itau.challenge.domain.utilities.PasswordValidator
import cl.itau.challenge.utilities.DispatcherFactory

class SignInUseCase(
    private val emailValidator: EmailValidator,
    private val passwordValidator: PasswordValidator,
    private val userRepository: UserRepository,
    dispatcherFactory: DispatcherFactory
): UseCase<SignInRequest, Boolean>(dispatcherFactory = dispatcherFactory) {

    override suspend fun request(parameters: SignInRequest): Boolean {

        check(emailValidator.validate(parameters.email)) { throw InvalidEmailException() }
        check(passwordValidator.validate(parameters.password)) { throw InvalidPasswordException() }

        return userRepository.signIn(parameters.email, parameters.password)
    }
}