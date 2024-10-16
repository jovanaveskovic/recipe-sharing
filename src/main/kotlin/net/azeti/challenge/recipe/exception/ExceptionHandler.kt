package net.azeti.challenge.recipe.exception

import org.hibernate.exception.ConstraintViolationException
import org.springframework.http.HttpStatus.*
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.oauth2.core.OAuth2AuthenticationException
import org.springframework.security.oauth2.core.OAuth2AuthorizationException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import java.net.URISyntaxException

@ResponseBody
@ControllerAdvice
class ExceptionHandler {

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolationException(exception: ConstraintViolationException): ApiError =
        ApiError(BAD_REQUEST.value(), exception.message)

    @ResponseStatus(FORBIDDEN)
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(exception: HttpMessageNotReadableException): ApiError =
        ApiError(FORBIDDEN.value(), exception.message)

    @ResponseStatus(FORBIDDEN)
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(exception: IllegalArgumentException): ApiError =
        ApiError(FORBIDDEN.value(), exception.message)

    @ResponseStatus(FORBIDDEN)
    @ExceptionHandler(IllegalStateException::class)
    fun handleIllegalStateException(exception: IllegalArgumentException): ApiError =
        ApiError(FORBIDDEN.value(), exception.message)

    @ResponseStatus(FORBIDDEN)
    @ExceptionHandler(URISyntaxException::class)
    fun handleURISyntaxException(exception: URISyntaxException): ApiError =
        ApiError(FORBIDDEN.value(), exception.message)

    @ResponseStatus(FORBIDDEN)
    @ExceptionHandler(NoSuchElementException::class)
    fun handleNoSuchElementException(exception: NoSuchElementException): ApiError =
        ApiError(FORBIDDEN.value(), exception.message)


    @ResponseStatus(UNAUTHORIZED)
    @ExceptionHandler(OAuth2AuthenticationException::class)
    fun handleOAuth2AuthenticationException(exception: OAuth2AuthenticationException): ApiError =
        ApiError(UNAUTHORIZED.value(), exception.message)

    @ResponseStatus(UNAUTHORIZED)
    @ExceptionHandler(OAuth2AuthorizationException::class)
    fun handleOAuth2AuthorizationException(exception: OAuth2AuthorizationException): ApiError =
        ApiError(UNAUTHORIZED.value(), exception.message)

    @ResponseStatus(UNPROCESSABLE_ENTITY)
    @ExceptionHandler(UnsatisfiedServletRequestParameterException::class)
    fun handleUnsatisfiedServletRequestParameterException(
        exception: UnsatisfiedServletRequestParameterException
    ): ApiError = ApiError(UNPROCESSABLE_ENTITY.value(), exception.message)

    @ResponseStatus(UNPROCESSABLE_ENTITY)
    @ExceptionHandler(MissingServletRequestParameterException::class)
    fun handleMissingServletRequestParameterException(
        exception: MissingServletRequestParameterException
    ): ApiError =
        ApiError(UNPROCESSABLE_ENTITY.value(), exception.message)

    @ExceptionHandler(ApiException::class)
    fun handleApiException(apiException: ApiException): ResponseEntity<ApiError> {
        val status = apiException.httpStatus
        val apiError = ApiError(status.value(), apiException.message)
        return ResponseEntity(apiError, status)
    }
}
