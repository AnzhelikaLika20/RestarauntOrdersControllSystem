package presentation.menu

import data.models.Dish
import data.models.Order
import data.models.OrderStatus
import data.models.VisitorAccount
import di.DI
import presentation.interfaces.VisitorMenu
import presentation.models.VisitorMenuOptions
import services.interfaces.MenuService
import services.interfaces.OrderService
import services.models.DishResponse
import services.models.OrderResponse
import services.models.ResponseCode
import java.time.Instant
import java.time.LocalTime
import java.util.*

class VisitorMenuImpl(private val orderService: OrderService, private val menuService: MenuService) : VisitorMenu {
    override fun displayMenuOptions() {
        print(DI.menuPresentations.visitorOptions)

    }

    override fun dealWithUser(account: VisitorAccount): OrderResponse {
        var dishResponse: OrderResponse
        do {
            displayMenuOptions()
            dishResponse = mapUserChoice(getUserOptionChoice(), account)
            println(dishResponse.hint)
        } while (dishResponse.status != ResponseCode.Exiting)
        return dishResponse
    }

    private fun mapUserChoice(userChoice: VisitorMenuOptions?, account: VisitorAccount): OrderResponse {
        return when (userChoice) {
            VisitorMenuOptions.CreateOrder -> createOrder(account)
            VisitorMenuOptions.AddDishIntoOrder -> addDishIntoOrder(account)
            VisitorMenuOptions.PayOrder -> payOrder(account)
            VisitorMenuOptions.CancelOrder -> cancelOrder(account)
            VisitorMenuOptions.Exit -> exit()
            else -> run {
                return OrderResponse(ResponseCode.BadRequest, "Incorrect input\n\n", null)
            }
        }
    }

    override fun exit(): OrderResponse {
        return OrderResponse(ResponseCode.Exiting, "Exiting\n\n", null)
    }

    private fun getUserOptionChoice(): VisitorMenuOptions? {
        val input = readlnOrNull()?.toIntOrNull()
        return when (input) {
            1 -> VisitorMenuOptions.CreateOrder
            2 -> VisitorMenuOptions.AddDishIntoOrder
            3 -> VisitorMenuOptions.PayOrder
            4 -> VisitorMenuOptions.CancelOrder
            5 -> VisitorMenuOptions.Exit
            else -> null
        }
    }

    private fun showAvailableDishes() {
        val availableDishes = menuService.getAvailableDishes()
        if (availableDishes.isEmpty()) {
            println("There is no available dishes\n")
        } else {
            println("Available dishes:")
            availableDishes.forEach { x -> println(x.name) }
        }
    }

    private fun getChosenDishesFromVisitor(): List<String>? {
        println("Enter dishes you would like to add to order separated by a comma and a space")
        val userInput = readlnOrNull() ?: return null
        val dishNames: List<String> = userInput.split(", ")
        return dishNames
    }

    override fun createOrder(account: VisitorAccount): OrderResponse {
        showAvailableDishes()
        val dishNames =
            getChosenDishesFromVisitor() ?: return OrderResponse(ResponseCode.BadRequest, "Incorrect input\n", null)
        return orderService.createOrder(dishNames, account.login)
    }

    private fun showActiveOrdersOfUser(login: String) {
        println("Your active orders:")
        val activeOrders = orderService.getActiveOrdersOfUser(login)
        activeOrders.forEach { x -> println(x) }
    }

    override fun addDishIntoOrder(account: VisitorAccount): OrderResponse {
        showActiveOrdersOfUser(account.login)
        println("Enter id of order to add dish: ")
        val orderId =
            readlnOrNull() ?: return OrderResponse(ResponseCode.BadRequest, "Order id should be not null", null)
        showAvailableDishes()
        val dishNames =
            getChosenDishesFromVisitor() ?: return OrderResponse(ResponseCode.BadRequest, "Incorrect input\n", null)
        return orderService.addDishIntoOrder(orderId, dishNames)
    }

    override fun payOrder(account: VisitorAccount): OrderResponse {
        TODO("Not yet implemented")
    }

    private fun cancelOrder(account: VisitorAccount): OrderResponse {
        TODO("Not yet implemented")
    }
}