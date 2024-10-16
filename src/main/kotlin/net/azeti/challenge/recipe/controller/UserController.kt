package net.azeti.challenge.recipe.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import net.azeti.challenge.recipe.model.LoginRequest
import net.azeti.challenge.recipe.model.RegistrationRequest
import net.azeti.challenge.recipe.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(UserController.BASE_URL)
class UserController(private val userService: UserService) {
    companion object {
        const val BASE_URL = "/user"
        const val REGISTER_URL = "/register"
        const val LOGIN_URL = "/login"
    }

    @Operation(summary = "Registers a new user",
        description = "During the registration user's password will be hashed and securly stored in the database.")
    @PostMapping(
        REGISTER_URL,
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseStatus(HttpStatus.CREATED)
    fun registerUser(
        @RequestBody registrationRequest: RegistrationRequest
    ) = userService.register(registrationRequest)

    @Operation(summary = "Logins a user",
        description = "After the user is successfully logged-in, this operation will return an access token which will be used as the value for bearer authentication for recipe operations.")
    @PostMapping(
        LOGIN_URL,
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseStatus(HttpStatus.CREATED)
    fun loginUser(
        @RequestBody loginRequest: LoginRequest
    ) = userService.login(loginRequest)

}