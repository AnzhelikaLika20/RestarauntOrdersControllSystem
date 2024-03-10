package presentation.menu

import di.DI
import services.interfaces.AuthService
import presentation.interfaces.AuthMenu
import presentation.models.AuthMenuOptions
import presentation.models.Role
import services.models.AuthResponse
import services.models.ResponseCode

class AuthMenuImpl(private val authService: AuthService) : AuthMenu {
    override fun displayMenuOptions() {
        print(DI.menuPresentations.authMenuOptions)
    }

    private fun mapUserChoice(userChoice: AuthMenuOptions?): AuthResponse {
        return when (userChoice) {
            AuthMenuOptions.SignUpAsVisitor -> signUpUser(Role.Visitor)
            AuthMenuOptions.SignUpAsAdmin -> signUpUser(Role.Admin)
            AuthMenuOptions.SignIn -> signInUser()
            AuthMenuOptions.Exit -> exit()
            else -> run {
                return AuthResponse(ResponseCode.BadRequest, "Incorrect input\n", null)
            }
        }
    }

    private fun exit(): AuthResponse {
        return AuthResponse(ResponseCode.Exiting, "Exiting\n\n", null)
    }

    private fun askToEnterLoginAndPassword(): Pair<String, String>? {
        println("Enter login: ")
        val userLogin: String = readlnOrNull() ?: return null
        println("Enter password: ")
        val userPassword: String = readlnOrNull() ?: return null
        if (userPassword == "" || userLogin == "") {
            return null
        }
        return Pair(userLogin, userPassword)
    }

    private fun signInUser(): AuthResponse {
        val loginAndPassword: Pair<String, String> = askToEnterLoginAndPassword()
            ?: return AuthResponse(ResponseCode.BadRequest, "Login and password should be not null\n\n", null)
        val response = authService.loginUser(loginAndPassword.first, loginAndPassword.second)
        return response
    }

    private fun signUpUser(role: Role): AuthResponse {
        val loginAndPassword: Pair<String, String> = askToEnterLoginAndPassword()
            ?: return AuthResponse(ResponseCode.BadRequest, "Login and password should be not null\n\n", null)
        val response = authService.registerUser(loginAndPassword.first, loginAndPassword.second, role)
        return response
    }

    private fun getUserOptionChoice(): AuthMenuOptions? {
        val input = readlnOrNull()?.toIntOrNull()
        return when (input) {
            1 -> AuthMenuOptions.SignUpAsVisitor
            2 -> AuthMenuOptions.SignUpAsAdmin
            3 -> AuthMenuOptions.SignIn
            4 -> AuthMenuOptions.Exit
            else -> null
        }
    }

    override fun dealWithUser(): AuthResponse {
        var authResponse: AuthResponse
        do {
            displayMenuOptions()
            authResponse = mapUserChoice(getUserOptionChoice())
            if (authResponse.status == ResponseCode.Exiting)
                return authResponse
            print(authResponse.hint)
        } while (authResponse.status != ResponseCode.Success)
        return authResponse
    }
}