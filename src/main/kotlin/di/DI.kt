package di

import data.interfaces.*
import data.repositories.*
import presentation.models.AllMenuPresentations
import services.*
import services.interfaces.*

object DI {

    private val userRepository: UserRepository by lazy {
        UserRepositoryImpl("src/main/resources/UserStorage.json")
    }

    private val dishRepository: DishRepository by lazy {
        DishRepositoryImpl("src/main/resources/DishStorage.json")
    }

    private val orderRepository: OrderRepository by lazy {
        OrderRepositoryImpl("src/main/resources/OrderRepository.json")
    }

    private val reviewRepository: ReviewRepository by lazy {
        ReviewRepositoryImpl("src/main/resources/ReviewRepository.json")
    }

    private val statisticsRepository: StatisticsRepository by lazy {
        StatisticsRepositoryImpl("src/main/resources/StatisticsRepository.json")
    }

    val authService: AuthService
        get() = AuthServiceImpl(userRepository)

    val statisticsService: StatisticsService
        get() = StatisticsServiceImpl(statisticsRepository, orderRepository, reviewRepository)

    val menuService: MenuService
        get() = MenuServiceImpl(dishRepository)

    val orderService: OrderService
        get() = OrderServiceImpl(orderRepository, statisticsRepository)

    val reviewService: ReviewService
        get() = ReviewServiceImpl(reviewRepository)

    val menuPresentations: AllMenuPresentations
        get() = AllMenuPresentations()
}