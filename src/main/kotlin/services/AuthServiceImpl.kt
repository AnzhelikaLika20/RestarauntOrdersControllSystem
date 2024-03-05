package services

import data.models.AdminAccount
import data.models.VisitorAccount
import data.repositories.UserRepositoryImpl
import org.mindrot.jbcrypt.BCrypt
import services.interfaces.AuthService
import services.models.AuthResponse
import presentation.models.Role
import services.models.ResponseCode

class AuthServiceImpl(private val userRepository: UserRepositoryImpl) : AuthService {

    override fun loginUser(login: String, password: String): AuthResponse {
        return if (checkLoginExistence(login)) {
            checkPassword(login, password)
        } else {
            AuthResponse(ResponseCode.BadRequest, "No user with such login\n\n", null)
        }
    }

    private fun checkPassword(login: String, password: String): AuthResponse {
        val user = userRepository.getAccountByLogin(login)
        val hashedPassword = user.password
        if (BCrypt.checkpw(password, hashedPassword)) {
            userRepository.enterAccount(login)
            return AuthResponse (ResponseCode.Success, "User was successfully logged in\n\n", user)
        }
        return AuthResponse(ResponseCode.BadRequest, "Incorrect password\n\n", user)
    }

    override fun registerUser(login: String, password: String, role: Role): AuthResponse {
        if (!checkLoginExistence(login)) {
            return createAccount(login, password, role)
        }
        return AuthResponse(ResponseCode.BadRequest, "User with such login already exists\n\n", null)
    }

    private fun checkLoginExistence(login: String): Boolean {
        return userRepository.containsLogin(login)
    }

    private fun hashPassword(password: String): String {
        val salt = BCrypt.gensalt()
        return BCrypt.hashpw(password, salt)
    }

    private fun createAccount(login: String, password: String, role: Role): AuthResponse {
        val hashedPassword = hashPassword(password)
        val user = when (role) {
            Role.Admin -> AdminAccount(login, hashedPassword)
            Role.Visitor -> VisitorAccount(login, hashedPassword)
        }
        userRepository.createAccount(user)
        return AuthResponse (ResponseCode.Success, "User was successfully signed up\n\n", user)
    }
}