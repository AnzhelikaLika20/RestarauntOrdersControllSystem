package presentation.menu

import data.models.*
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

    private fun getActiveOrdersOfUser(login: String) : List<String>? {
        val activeOrdersOfUser = orderService.getActiveOrdersOfUser(login)
        if(activeOrdersOfUser.isEmpty())
            return null
        println("Your active orders:")
        activeOrdersOfUser.forEach { x -> println(x) }
        return activeOrdersOfUser
    }

    override fun addDishIntoOrder(account: VisitorAccount): OrderResponse {
        val activeOrdersOfUser = getActiveOrdersOfUser(account.login)
            ?: return  OrderResponse(ResponseCode.Success, "There is no any active orders\n", null)
        println("Enter id of order to add dish: ")
        val orderId =
            readlnOrNull() ?: return OrderResponse(ResponseCode.BadRequest, "Order id should be not null\n", null)
        if(!activeOrdersOfUser.contains(orderId))
            return OrderResponse(ResponseCode.BadRequest, "There is no such order amount active orders\n", null)
        showAvailableDishes()
        val dishNames =
            getChosenDishesFromVisitor() ?: return OrderResponse(ResponseCode.BadRequest, "Incorrect input\n", null)
        return orderService.addDishIntoOrder(orderId, dishNames)
    }

    override fun payOrder(account: VisitorAccount): OrderResponse {
        if(!showReadyForPaymentOrders(account.login))
            return OrderResponse(ResponseCode.Success, "There is no any ready for payment orders\n", null)
        println("Enter id of order to pay it: ")
        val orderId =
            readlnOrNull() ?: return OrderResponse(ResponseCode.BadRequest, "Order id should be not null\n", null)
        return orderService.payOrder(orderId, orderId)
    }

    private fun showReadyForPaymentOrders(login: String) : Boolean {
        val readyForPaymentOrdersOfUser = orderService.getReadyForPaymentOrdersOfUser(login)
        if(readyForPaymentOrdersOfUser.isEmpty())
            return false
        println("Your ready for payment orders:")
        readyForPaymentOrdersOfUser.forEach { x -> println(x) }
        return true
    }

    private fun cancelOrder(account: VisitorAccount): OrderResponse {
        if(!showCookingOrders(account.login))
            return OrderResponse(ResponseCode.Success, "There is no any preparing orders\n", null)
        println("Enter id of order to cancel it: ")
        val orderId =
            readlnOrNull() ?: return OrderResponse(ResponseCode.BadRequest, "Order id should be not null\n", null)
        return orderService.cancelOrder(account.login, orderId)
    }

    private fun showCookingOrders(login: String) : Boolean{
        val preparingOrdersOfUser = orderService.getPreparingOrdersOfUser(login)
        if(preparingOrdersOfUser.isEmpty())
            return false
        println("Your preparing orders:")
        preparingOrdersOfUser.forEach { x -> println(x) }
        return true
    }
}