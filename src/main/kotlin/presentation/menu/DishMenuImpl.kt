package presentation.menu

import data.models.Dish
import di.DI
import presentation.interfaces.DishMenu
import presentation.models.DishMenuOptions
import services.interfaces.MenuService
import services.models.AdminResponse

class DishMenuImpl(private val menuService: MenuService) : DishMenu {

    private fun setDifficulty(): AdminResponse {
        TODO("Not yet implemented")
    }

    private fun setPrice(): AdminResponse {
        TODO("Not yet implemented")
    }

    private fun setAmount(): AdminResponse {
        TODO("Not yet implemented")
    }

    private fun dropDish(): AdminResponse {
        TODO("Not yet implemented")
    }

    fun createAndValidateDishParameters() : AdminResponse {
        println("Enter name of dish: ")
        val name: String = readlnOrNull() ?: return AdminResponse(404, "Name should be not empty\n\n", null)
        println("Enter amount of dish: ")
        val amount: Int =
            readlnOrNull()?.toIntOrNull() ?: return AdminResponse(404, "Amount should be not empty\n\n", null)
        println("Enter price of dish: ")
        val price: Double = readlnOrNull()?.toDoubleOrNull() ?: return AdminResponse(
            404,
            "Price should be not empty\n\n",
            null
        )
        println("Enter duration of preparing in minutes of dish: ")
        val duration: Int =
            readlnOrNull()?.toIntOrNull() ?: return AdminResponse(404, "Duration should be not empty\n\n", null)
        val newDish = Dish(name, amount, price, duration)
        return AdminResponse(200, "Dish was successfully created\n\n", newDish)
    }
    private fun addOrReplaceDish(): AdminResponse {
        var response = createAndValidateDishParameters()
        val dish = response.dish
        if(response.status != 200 || dish == null)
            return response
        response = menuService.addDish(dish)
        if (response.status == 200)
            return response
        println(response.hint)
        val input = readlnOrNull()
        response = when (input) {
            "Yes" -> menuService.updateDish(dish)
            "No" -> AdminResponse(200, "Dish was not updated\n\n", null)
            else -> AdminResponse(404, "Incorrect input\n\n", null)
        }
        return response
    }

    private fun exit(): AdminResponse {
        return AdminResponse(400, "Exiting\n\n", null)
    }

    private fun getUserOptionChoice(): DishMenuOptions? {
        val input = readlnOrNull()?.toIntOrNull()
        return when (input) {
            1 -> DishMenuOptions.AddDish
            2 -> DishMenuOptions.DropDish
            3 -> DishMenuOptions.SetAmount
            4 -> DishMenuOptions.SetPrice
            5 -> DishMenuOptions.SetDifficulty
            6 -> DishMenuOptions.Exit
            else -> null
        }
    }

    override fun displayMenuOptions() {
        print(DI.menuPresentations.dishMenuOptions)
    }

    override fun dealWithUser(): AdminResponse {
        var adminResponse: AdminResponse
        do {
            displayMenuOptions()
            adminResponse = mapUserChoice(getUserOptionChoice())
            print(adminResponse.hint)
        } while (adminResponse.status != 400)
        return adminResponse
    }

    private fun mapUserChoice(userChoice: DishMenuOptions?): AdminResponse {
        return when (userChoice) {
            DishMenuOptions.AddDish -> addOrReplaceDish()
            DishMenuOptions.DropDish -> dropDish()
            DishMenuOptions.SetAmount -> setAmount()
            DishMenuOptions.SetPrice -> setPrice()
            DishMenuOptions.SetDifficulty -> setDifficulty()
            DishMenuOptions.Exit -> exit()
            else -> run {
                return AdminResponse(400, "Incorrect input\n\n", null)
            }
        }
    }
}