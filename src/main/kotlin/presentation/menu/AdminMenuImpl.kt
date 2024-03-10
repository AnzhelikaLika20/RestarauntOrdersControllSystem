package presentation.menu

import data.models.Dish
import di.DI
import presentation.interfaces.AdminMenu
import presentation.models.DishMenuOptions
import services.interfaces.MenuService
import services.interfaces.OrderService
import services.interfaces.StatisticsService
import services.models.DishResponse
import services.models.ResponseCode

class AdminMenuImpl(
    private val menuService: MenuService,
    private val statisticsService: StatisticsService,
    private val orderService: OrderService
) : AdminMenu {
    private val hintNotEmptyString = "Name should be not empty\n\n"
    private fun setDifficulty(): DishResponse {
        val name = getAndValidateDishName() ?: return DishResponse(ResponseCode.BadRequest, hintNotEmptyString, null)
        val difficulty =
            getValidatedDifficulty() ?: return DishResponse(ResponseCode.BadRequest, "Not valid duration\n\n", null)
        val response = menuService.setDifficultyOfDish(name, difficulty)
        return response
    }

    private fun setPrice(): DishResponse {
        val name = getAndValidateDishName() ?: return DishResponse(ResponseCode.BadRequest, hintNotEmptyString, null)
        val price = getValidatedPrice() ?: return DishResponse(ResponseCode.BadRequest, "Not valid price\n\n", null)
        val response = menuService.setPriceOfDish(name, price)
        return response
    }


    private fun setAmount(): DishResponse {
        val name = getAndValidateDishName() ?: return DishResponse(ResponseCode.BadRequest, hintNotEmptyString, null)
        val amount = getValidatedAmount() ?: return DishResponse(ResponseCode.BadRequest, "Not valid amount\n\n", null)
        val response = menuService.setAmountOfDish(name, amount)
        return response
    }

    private fun dropDish(): DishResponse {
        val name = getAndValidateDishName() ?: return DishResponse(ResponseCode.BadRequest, hintNotEmptyString, null)
        val response = menuService.dropDish(name)
        return response
    }

    private fun addOrReplaceDish(): DishResponse {
        var response = createAndValidateDishParameters()
        val dish = response.dish
        if (response.status != ResponseCode.Success || dish == null)
            return response
        response = menuService.addDish(dish)
        if (response.status == ResponseCode.Success)
            return response
        println(response.hint)
        val input = readlnOrNull()
        response = when (input) {
            "Yes" -> menuService.updateDish(dish)
            "No" -> DishResponse(ResponseCode.Success, "Dish was not updated\n\n", null)
            else -> DishResponse(ResponseCode.BadRequest, "Incorrect input\n\n", null)
        }
        return response
    }

    private fun getValidatedPrice(): Double? {
        println("Enter price of dish: ")
        val price: Double? = readlnOrNull()?.toDoubleOrNull()
        if (price == null || price < 0)
            return null
        return price
    }

    private fun getValidatedDifficulty(): Int? {
        println("Enter duration of preparing in minutes of dish: ")
        val duration: Int? = readlnOrNull()?.toIntOrNull()
        if (duration == null || duration < 0)
            return null
        return duration
    }

    private fun getAndValidateDishName(): String? {
        println("Enter name of dish: ")
        val name: String? = readlnOrNull()
        if (name == "" || name == null)
            return null
        return name
    }

    private fun getValidatedAmount(): Int? {
        println("Enter amount of dish: ")
        val amount: Int? = readlnOrNull()?.toIntOrNull()
        if (amount == null || amount < 0)
            return null
        return amount
    }

    private fun createAndValidateDishParameters(): DishResponse {
        val name = getAndValidateDishName() ?: return DishResponse(ResponseCode.BadRequest, hintNotEmptyString, null)
        val amount = getValidatedAmount() ?: return DishResponse(ResponseCode.BadRequest, "Not valid amount\n\n", null)
        val price = getValidatedPrice() ?: return DishResponse(ResponseCode.BadRequest, "Not valid price\n\n", null)
        val difficulty =
            getValidatedDifficulty() ?: return DishResponse(ResponseCode.BadRequest, "Not valid duration\n\n", null)
        val newDish = Dish(name, amount, price, difficulty)
        return DishResponse(ResponseCode.Success, "Dish was successfully created\n\n", newDish)
    }

    private fun exit(): DishResponse {
        orderService.clearOrders()
        return DishResponse(ResponseCode.Exiting, "Exiting\n\n", null)
    }

    private fun getUserOptionChoice(): DishMenuOptions? {
        val input = readlnOrNull()?.toIntOrNull()
        return when (input) {
            1 -> DishMenuOptions.AddDish
            2 -> DishMenuOptions.DropDish
            3 -> DishMenuOptions.SetAmount
            4 -> DishMenuOptions.SetPrice
            5 -> DishMenuOptions.SetDifficulty
            6 -> DishMenuOptions.GetReviews
            7 -> DishMenuOptions.GetStatistics
            8 -> DishMenuOptions.Exit
            else -> null
        }
    }

    override fun displayMenuOptions() {
        print(DI.menuPresentations.dishMenuOptions)
    }

    override fun dealWithUser(): DishResponse {
        var dishResponse: DishResponse
        do {
            displayMenuOptions()
            dishResponse = mapUserChoice(getUserOptionChoice())
            print(dishResponse.hint)
        } while (dishResponse.status != ResponseCode.Exiting)
        return dishResponse
    }

    private fun mapUserChoice(userChoice: DishMenuOptions?): DishResponse {
        return when (userChoice) {
            DishMenuOptions.AddDish -> addOrReplaceDish()
            DishMenuOptions.DropDish -> dropDish()
            DishMenuOptions.SetAmount -> setAmount()
            DishMenuOptions.SetPrice -> setPrice()
            DishMenuOptions.SetDifficulty -> setDifficulty()
            DishMenuOptions.GetReviews -> getReviews()
            DishMenuOptions.GetStatistics -> getStatistics()
            DishMenuOptions.Exit -> exit()
            else -> run {
                return DishResponse(ResponseCode.BadRequest, "Incorrect input\n\n", null)
            }
        }
    }

    private fun getStatistics(): DishResponse {
        /*val revenue = statisticsService.getRevenue()
        val popularDishes = statisticsService.getPopularDishes()
        val orderCount = statisticsService.getOrderCountOverPeriod()
        val averageRating = statisticsService.getAverageRatingOfDishes()*/
        TODO("Not yet implemented")

    }

    private fun getReviews(): DishResponse {
        TODO("Not yet implemented")
    }
}