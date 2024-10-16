package net.azeti.challenge.recipe.service

import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.stereotype.Service

@Service
class PasswordHashService {
    fun checkBcrypt(input: String, hash: String): Boolean =
        BCrypt.checkpw(input, hash)

    fun hashBcrypt(input: String): String =
        BCrypt.hashpw(input, BCrypt.gensalt(10))

}