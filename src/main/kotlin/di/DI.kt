package di

import data.Interfaces.StatisticsRepository
import data.repositories.*
import presentation.models.AllMenuPresentations
import services.*
import services.interfaces.*

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

    private val reviewRepository: ReviewRepositoryImpl by lazy {
        ReviewRepositoryImpl("src/main/resources/ReviewRepository.json")
    }

    private val statisticsRepository: StatisticsRepositoryImpl by lazy {
        StatisticsRepositoryImpl("src/main/resources/StatisticsRepository.json")
    }

    val authService : AuthService
        get() = AuthServiceImpl(userRepository)

    val statisticsService : StatisticsService
        get() = StatisticsServiceImpl(statisticsRepository)

    val menuService : MenuService
        get() = MenuServiceImpl(dishRepository)

    val orderService : OrderService
        get() = OrderServiceImpl(orderRepository)

    val reviewService : ReviewService
        get() = ReviewServiceImpl(reviewRepository)

    val menuPresentations : AllMenuPresentations
        get() = AllMenuPresentations()
}