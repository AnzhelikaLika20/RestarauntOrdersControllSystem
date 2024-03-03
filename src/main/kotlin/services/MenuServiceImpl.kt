package services

import data.models.Dish
import data.repositories.DishRepositoryImpl
import services.interfaces.MenuService
import services.models.AdminResponse
import services.models.AuthResponse
class MenuServiceImpl(private val dishRepository: DishRepositoryImpl) : MenuService {
    private val hintDishIsAbsent : String = "There is no dish with such name\n\n"
    override fun addDish(dish : Dish) : AdminResponse {
        if (dishRepository.containsDish(dish.name))
            return AdminResponse(401,
                "Dish with name ${dish.name} already exists. Do you want to replace it? Enter Yes or No", dish)
        dishRepository.addDish(dish)
        return AdminResponse(200, "Dish was successfully added\n\n", dish)
    }

    override fun updateDish(dish : Dish) : AdminResponse {
        dishRepository.addDish(dish)
        return AdminResponse(200, "Dish was successfully updated\n\n", dish)

    }

    override fun dropDish(dishName : String) : AdminResponse {
        if(!dishRepository.containsDish(dishName))
            return AdminResponse(200, hintDishIsAbsent, null)
        dishRepository.dropDish(dishName)
        return AdminResponse(200, "Dish was successfully dropped\n\n", null)
    }

    override fun setAmountOfDish(dishName : String, amount : Int) : AdminResponse {
        if(!dishRepository.containsDish(dishName))
            return AdminResponse(200, hintDishIsAbsent, null)
        dishRepository.setAmountForDish(dishName, amount)
        return AdminResponse(200, "Amount of $dishName dish was successfully changed\n\n", null)
    }

    override fun setPriceOfDish(dishName : String, price : Double) : AdminResponse {
        if(!dishRepository.containsDish(dishName))
            return AdminResponse(200, hintDishIsAbsent, null)
        dishRepository.setPriceForDish(dishName, price)
        return AdminResponse(200, "Price of $dishName dish was successfully changed\n\n", null)
    }

    override fun setDifficultyOfDish(dishName : String, difficulty : Int) : AdminResponse {
        if(!dishRepository.containsDish(dishName))
            return AdminResponse(200, hintDishIsAbsent, null)
        dishRepository.setDifficultyForDish(dishName, difficulty)
        return AdminResponse(200, "Difficulty of $dishName dish was successfully changed\n\n", null)
    }
}