package di

import data.repositories.DishRepositoryImpl
import data.repositories.OrderRepositoryImpl
import data.repositories.UserRepositoryImpl
import services.AuthServiceImpl
import services.interfaces.AuthService
import presentation.models.AllMenuPresentations
import services.MenuServiceImpl
import services.OrderServiceImpl
import services.interfaces.MenuService
import services.interfaces.OrderService

object DI {

    private val userRepository: UserRepositoryImpl by lazy {
        UserRepositoryImpl("src/main/resources/UserStorage.json")
    }

    private val dishRepository: DishRepositoryImpl by lazy {
        DishRepositoryImpl("src/main/resources/DishStorage.json")
    }

    private val orderRepository: OrderRepositoryImpl by lazy {
        OrderRepositoryImpl("src/main/resources/OrderRepository.json")
    }

    val authService : AuthService
        get() = AuthServiceImpl(userRepository)

    val menuService : MenuService
        get() = MenuServiceImpl(dishRepository)

    val orderService : OrderService
        get() = OrderServiceImpl(orderRepository)

    val menuPresentations : AllMenuPresentations
        get() = AllMenuPresentations()
}