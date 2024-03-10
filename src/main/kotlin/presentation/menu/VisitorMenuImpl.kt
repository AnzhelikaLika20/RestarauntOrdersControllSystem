package presentation.menu

import data.models.OrderStatus
import data.models.VisitorAccount
import di.DI
import presentation.interfaces.VisitorMenu
import presentation.models.VisitorMenuOptions
import services.interfaces.MenuService
import services.interfaces.OrderService
import services.interfaces.ReviewService
import services.models.OrderResponse
import services.models.Response
import services.models.ResponseCode
import services.models.ReviewResponse

class VisitorMenuImpl(
    private val orderService: OrderService,
    private val menuService: MenuService,
    private val reviewService: ReviewService
) : VisitorMenu {
    override fun displayMenuOptions() {
        print(DI.menuPresentations.visitorOptions)

    }

    override fun dealWithUser(account: VisitorAccount): Response {
        var dishResponse: Response
        do {
            displayMenuOptions()
            dishResponse = mapUserChoice(getUserOptionChoice(), account)
            println(dishResponse.hint)
        } while (dishResponse.status != ResponseCode.Exiting)
        return dishResponse
    }

    private fun mapUserChoice(userChoice: VisitorMenuOptions?, account: VisitorAccount): Response {
        return when (userChoice) {
            VisitorMenuOptions.CreateOrder -> createOrder(account)
            VisitorMenuOptions.AddDishIntoOrder -> addDishIntoOrder(account)
            VisitorMenuOptions.PayOrder -> payOrder(account)
            VisitorMenuOptions.CancelOrder -> cancelOrder(account)
            VisitorMenuOptions.LeaveReview -> createReview(account)
            VisitorMenuOptions.Exit -> exit()
            else -> run {
                return OrderResponse(ResponseCode.BadRequest, "Incorrect input\n\n", null)
            }
        }
    }

    fun createReview(account: VisitorAccount): ReviewResponse {
        val paidOrders =
            orderService.getAllOrders().filter { it.status == OrderStatus.Paid && it.visitorLogin == account.login }
        if (paidOrders.isEmpty()) {
            return ReviewResponse(ResponseCode.BadRequest, "You have no paid orders. Cannot leave a review.\n\n", null)
        }
        println("Enter the name of the dish you want to leave a review on:")
        val dishName = readlnOrNull() ?: return ReviewResponse(ResponseCode.BadRequest, "Incorrect dish\n", null)
        val paidDishes = paidOrders.flatMap { it.dishes }.distinct()
        if (paidDishes.none { it.name == dishName }) {
            return ReviewResponse(
                ResponseCode.BadRequest,
                "Dish \"$dishName\" was not found in any order you paid for. Cannot leave a review on it.\n",
                null
            )
        }
        println("Enter the text comment to the dish")
        val comment =
            readlnOrNull() ?: return ReviewResponse(ResponseCode.BadRequest, "Comment should be not null\n", null)
        println("Enter the rate of the dish from 1 to 5")
        val rate = readlnOrNull()?.toIntOrNull() ?: return ReviewResponse(
            ResponseCode.BadRequest,
            "Rate should be not null\n",
            null
        )
        return reviewService.leaveReview(account.login, dishName, rate, comment)
    }

    fun exit(): OrderResponse {
        orderService.clearOrders()
        return OrderResponse(ResponseCode.Exiting, "Exiting\n\n", null)
    }

    private fun getUserOptionChoice(): VisitorMenuOptions? {
        val input = readlnOrNull()?.toIntOrNull()
        return when (input) {
            1 -> VisitorMenuOptions.CreateOrder
            2 -> VisitorMenuOptions.AddDishIntoOrder
            3 -> VisitorMenuOptions.PayOrder
            4 -> VisitorMenuOptions.CancelOrder
            5 -> VisitorMenuOptions.LeaveReview
            6 -> VisitorMenuOptions.Exit
            else -> null
        }
    }

    private fun showAvailableDishes() {
        val availableDishes = menuService.getAvailableDishes()
        println("Available dishes:")
        availableDishes.forEach { x -> println(x.name) }
    }

    private fun getChosenDishesFromVisitor(): List<String>? {
        println("Enter dishes you would like to add to order separated by a comma and a space")
        val userInput = readlnOrNull() ?: return null
        val dishNames: List<String> = userInput.split(", ")
        return dishNames
    }

    fun createOrder(account: VisitorAccount): OrderResponse {
        if (menuService.getAvailableDishes().isEmpty())
            return OrderResponse(ResponseCode.Success, "There is no available dishes\n", null)
        showAvailableDishes()
        val dishNames =
            getChosenDishesFromVisitor() ?: return OrderResponse(ResponseCode.BadRequest, "Incorrect input\n", null)
        return orderService.createOrder(dishNames, account.login)
    }

    private fun getActiveOrdersOfUser(login: String): List<String>? {
        val activeOrdersOfUser = orderService.getActiveOrdersOfUser(login)
        if (activeOrdersOfUser.isEmpty())
            return null
        println("Your active orders:")
        activeOrdersOfUser.forEach { x -> println(x) }
        return activeOrdersOfUser
    }

    fun addDishIntoOrder(account: VisitorAccount): OrderResponse {
        val activeOrdersOfUser = getActiveOrdersOfUser(account.login)
            ?: return OrderResponse(ResponseCode.Success, "There is no any active orders\n", null)
        println("Enter id of order to add dish: ")
        val orderId =
            readlnOrNull() ?: return OrderResponse(ResponseCode.BadRequest, "Order id should be not null\n", null)
        if (!activeOrdersOfUser.contains(orderId))
            return OrderResponse(ResponseCode.BadRequest, "There is no such order amount active orders\n", null)
        showAvailableDishes()
        val dishNames =
            getChosenDishesFromVisitor() ?: return OrderResponse(ResponseCode.BadRequest, "Incorrect input\n", null)
        return orderService.addDishIntoOrder(orderId, dishNames)
    }

    fun payOrder(account: VisitorAccount): OrderResponse {
        if (!showReadyForPaymentOrders(account.login))
            return OrderResponse(ResponseCode.Success, "There is no any ready for payment orders\n", null)
        println("Enter id of order to pay it: ")
        val orderId =
            readlnOrNull() ?: return OrderResponse(ResponseCode.BadRequest, "Order id should be not null\n", null)
        return orderService.payOrder(account.login, orderId)
    }

    private fun showReadyForPaymentOrders(login: String): Boolean {
        val readyForPaymentOrdersOfUser = orderService.getReadyForPaymentOrdersOfUser(login)
        if (readyForPaymentOrdersOfUser.isEmpty())
            return false
        println("Your ready for payment orders:")
        readyForPaymentOrdersOfUser.forEach { x -> println(x) }
        return true
    }

    private fun cancelOrder(account: VisitorAccount): OrderResponse {
        if (!showCookingOrders(account.login))
            return OrderResponse(ResponseCode.Success, "There is no any preparing orders\n", null)
        println("Enter id of order to cancel it: ")
        val orderId =
            readlnOrNull() ?: return OrderResponse(ResponseCode.BadRequest, "Order id should be not null\n", null)
        return orderService.cancelOrder(account.login, orderId)
    }

    private fun showCookingOrders(login: String): Boolean {
        val preparingOrdersOfUser = orderService.getPreparingOrdersOfUser(login)
        if (preparingOrdersOfUser.isEmpty())
            return false
        println("Your preparing orders:")
        preparingOrdersOfUser.forEach { x -> println(x) }
        return true
    }
}