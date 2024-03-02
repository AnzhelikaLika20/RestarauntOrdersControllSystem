package di

import data.repositories.DishRepositoryImpl
import data.repositories.UserRepositoryImpl
import services.AuthServiceImpl
import services.interfaces.AuthService
import presentation.models.AllMenuPresentations
import services.MenuServiceImpl
import services.interfaces.MenuService

object DI {

    private val userRepository: UserRepositoryImpl by lazy {
        UserRepositoryImpl("src/main/resources/UserStorage.json")
    }

    private val dishRepository: DishRepositoryImpl by lazy {
        DishRepositoryImpl("src/main/resources/DishStorage.json")
    }

    val authService : AuthService
        get() = AuthServiceImpl(userRepository)

    val menuService : MenuService
        get() = MenuServiceImpl(dishRepository)

    val menuPresentations : AllMenuPresentations
        get() = AllMenuPresentations()
}