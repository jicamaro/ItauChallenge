package cl.itau.challenge.domain.model

class InvalidNameException: Throwable(message = "El nombre introducido no es válido")
class InvalidLastNameException: Throwable(message = "El apellido introducido no es válido")
class InvalidEmailException: Throwable(message = "El correo electrónico introducido no es válido")
class InvalidPasswordException: Throwable(message = "La contraseña introducido no es válida")