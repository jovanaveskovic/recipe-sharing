package net.azeti.challenge.recipe.service

import net.azeti.challenge.recipe.data.User
import net.azeti.challenge.recipe.data.repository.UserRepository
import net.azeti.challenge.recipe.exception.ApiException
import net.azeti.challenge.recipe.model.LoginRequest
import net.azeti.challenge.recipe.model.LoginResponse
import net.azeti.challenge.recipe.model.RegistrationRequest
import net.azeti.challenge.recipe.model.RegistrationResponse
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordHashService: PasswordHashService,
    private val tokenService: TokenService
) {

    fun register(registrationRequest: RegistrationRequest): RegistrationResponse {
        if (userRepository.findUserByEmail(registrationRequest.email) != null)
            throw ApiException(HttpStatus.CONFLICT, "Email already exists")

        if (!registrationRequest.email.isEmailValid())
            throw ApiException(HttpStatus.BAD_REQUEST, "Email is not valid")

        if (userRepository.findUserByUsername(registrationRequest.username) != null)
            throw ApiException(HttpStatus.CONFLICT, "Username is already taken")

        val user = User(
            email = registrationRequest.email,
            username = registrationRequest.username,
            password = passwordHashService.hashBcrypt(registrationRequest.password)
        )

        userRepository.save(user)
        return RegistrationResponse(user.email, user.username)
    }

    fun login(loginRequest: LoginRequest): LoginResponse? {
        val user = userRepository.findUserByUsername(loginRequest.username) ?: throw ApiException(
            HttpStatus.NOT_FOUND,
            "Username doesn't exist"
        )

        if (!passwordHashService.checkBcrypt(loginRequest.password, user.password))
            throw ApiException(HttpStatus.BAD_REQUEST, "Login failed - incorrect password")

        return LoginResponse(accessToken = tokenService.createToken(user))
    }

    fun String.isEmailValid(): Boolean {
        val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
        return emailRegex.toRegex().matches(this)
    }
}