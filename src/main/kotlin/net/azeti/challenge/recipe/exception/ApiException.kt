package net.azeti.challenge.recipe.exception

import org.springframework.http.HttpStatus

class ApiException(val httpStatus: HttpStatus, message: String) : RuntimeException(message)