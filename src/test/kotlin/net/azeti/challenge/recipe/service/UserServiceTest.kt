package net.azeti.challenge.recipe.service

import net.azeti.challenge.recipe.data.User
import net.azeti.challenge.recipe.data.repository.UserRepository
import net.azeti.challenge.recipe.exception.ApiException
import net.azeti.challenge.recipe.model.LoginRequest
import net.azeti.challenge.recipe.model.RegistrationRequest
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.junit.jupiter.MockitoSettings
import org.mockito.quality.Strictness
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@ExtendWith(MockitoExtension::class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceTest {

    @InjectMocks
    private lateinit var userService: UserService

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var passwordHashService: PasswordHashService

    @Mock
    private lateinit var tokenService: TokenService

    @BeforeAll
    fun beforeAll() {
        MockitoAnnotations.openMocks(this)

        val user = User(
            email = "test@gmail.com",
            username = "test",
            password = "test123"
        )

        Mockito.`when`(userRepository.save(user)).thenReturn(user)
        Mockito.`when`(passwordHashService.hashBcrypt("test123")).thenReturn("hashedpassword")
        Mockito.`when`(userRepository.findUserByUsername(user.username)).thenReturn(user)
        Mockito.`when`(userRepository.findUserByEmail(user.email)).thenReturn(user)
        Mockito.`when`(passwordHashService.checkBcrypt("test123", user.password)).thenReturn(true)
        Mockito.`when`(tokenService.createToken(user)).thenReturn("accessToken")
    }

    @Test
    fun testRegisterUser() {
        val registrationResponse = userService.register(
            RegistrationRequest(
                email = "test1@gmail.com",
                username = "test1",
                password = "test123"
            )
        )
        assertNotNull(registrationResponse)
        assertEquals("test1", registrationResponse.username)
    }

    @Test
    fun testRegisterUserWithDuplicateEmail() {
        val exception = assertThrows<ApiException> {
            userService.register(
                RegistrationRequest(
                    email = "test@gmail.com",
                    username = "test1",
                    password = "test123"
                )
            )
        }
        assertEquals("Email already exists", exception.message)
    }

    @Test
    fun testRegisterUserWithNotValidEmail() {
        val exception = assertThrows<ApiException> {
            userService.register(
                RegistrationRequest(
                    email = "test",
                    username = "test1",
                    password = "test123"
                )
            )
        }
        assertEquals("Email is not valid", exception.message)
    }

    @Test
    fun testRegisterUserWithDuplicateUsername() {
        val exception = assertThrows<ApiException> {
            userService.register(
                RegistrationRequest(
                    email = "test1@gmail.com",
                    username = "test",
                    password = "test123"
                )
            )
        }
        assertEquals("Username is already taken", exception.message)
    }

    @Test
    fun testLoginUser() {
        val loginResponse = userService.login(LoginRequest("test", "test123"))
        assertNotNull(loginResponse)
        assertEquals("accessToken", loginResponse.accessToken)
    }

    @Test
    fun testLoginUserWithWrongUsername() {
        val exception = assertThrows<ApiException> {
            userService.login(LoginRequest("test1", "test123"))
        }
        assertEquals("Username doesn't exist", exception.message)
    }

    @Test
    fun testLoginUserWithWrongPassword() {
        val exception = assertThrows<ApiException> {
            userService.login(LoginRequest("test", "password"))
        }
        assertEquals("Login failed - incorrect password", exception.message)
    }
}