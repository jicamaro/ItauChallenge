package cl.itau.challenge.domain.usecases

import cl.itau.challenge.data.repository.UserRepository
import cl.itau.challenge.domain.UseCase
import cl.itau.challenge.domain.model.InvalidEmailException
import cl.itau.challenge.domain.model.InvalidLastNameException
import cl.itau.challenge.domain.model.InvalidNameException
import cl.itau.challenge.domain.model.InvalidPasswordException
import cl.itau.challenge.domain.model.SignUpRequest
import cl.itau.challenge.domain.model.User
import cl.itau.challenge.domain.utilities.EmailValidator
import cl.itau.challenge.domain.utilities.LastNameValidator
import cl.itau.challenge.domain.utilities.NameValidator
import cl.itau.challenge.domain.utilities.PasswordValidator
import cl.itau.challenge.domain.utilities.toEntity
import cl.itau.challenge.utilities.DispatcherFactory

class SignUpUseCase(
    private val nameValidator: NameValidator,
    private val lastNameValidator: LastNameValidator,
    private val emailValidator: EmailValidator,
    private val passwordValidator: PasswordValidator,
    private val userRepository: UserRepository,
    dispatcherFactory: DispatcherFactory
): UseCase<SignUpRequest, User>(dispatcherFactory = dispatcherFactory) {

    override suspend fun request(parameters: SignUpRequest): User {

        check(emailValidator.validate(parameters.email)) { throw InvalidEmailException() }
        check(passwordValidator.validate(parameters.password)) { throw InvalidPasswordException() }

        check(nameValidator.validate(parameters.name)) { throw InvalidNameException() }
        check(lastNameValidator.validate(parameters.lastName)) { throw InvalidLastNameException() }

        val user = User(
            id = parameters.id,
            name = parameters.name,
            lastName = parameters.lastName,
            email = parameters.email,
            password = parameters.password
        )

        userRepository.signUp(
            user.toEntity()
        )

        return user
    }
}