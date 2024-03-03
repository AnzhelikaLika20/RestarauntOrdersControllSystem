package services

import data.models.Dish
import data.repositories.DishRepositoryImpl
import services.interfaces.MenuService
import services.models.DishResponse
import services.models.ResponseCode

class MenuServiceImpl(private val dishRepository: DishRepositoryImpl) : MenuService {
    private val hintDishIsAbsent : String = "There is no dish with such name\n\n"
    override fun addDish(dish : Dish) : DishResponse {
        if (dishRepository.containsDish(dish.name))
            return DishResponse(ResponseCode.NeedConfirmation,
                "Dish with name ${dish.name} already exists. Do you want to replace it? Enter Yes or No", dish)
        dishRepository.addDish(dish)
        return DishResponse(ResponseCode.Success, "Dish was successfully added\n\n", dish)
    }

    override fun updateDish(dish : Dish) : DishResponse {
        dishRepository.addDish(dish)
        return DishResponse(ResponseCode.Success, "Dish was successfully updated\n\n", dish)

    }

    override fun dropDish(dishName : String) : DishResponse {
        if(!dishRepository.containsDish(dishName))
            return DishResponse(ResponseCode.Success, hintDishIsAbsent, null)
        dishRepository.dropDish(dishName)
        return DishResponse(ResponseCode.Success, "Dish was successfully dropped\n\n", null)
    }

    override fun setAmountOfDish(dishName : String, amount : Int) : DishResponse {
        if(!dishRepository.containsDish(dishName))
            return DishResponse(ResponseCode.Success, hintDishIsAbsent, null)
        dishRepository.setAmountForDish(dishName, amount)
        return DishResponse(ResponseCode.Success, "Amount of $dishName dish was successfully changed\n\n", null)
    }

    override fun setPriceOfDish(dishName : String, price : Double) : DishResponse {
        if(!dishRepository.containsDish(dishName))
            return DishResponse(ResponseCode.Success, hintDishIsAbsent, null)
        dishRepository.setPriceForDish(dishName, price)
        return DishResponse(ResponseCode.Success, "Price of $dishName dish was successfully changed\n\n", null)
    }

    override fun setDifficultyOfDish(dishName : String, difficulty : Int) : DishResponse {
        if(!dishRepository.containsDish(dishName))
            return DishResponse(ResponseCode.Success, hintDishIsAbsent, null)
        dishRepository.setDifficultyForDish(dishName, difficulty)
        return DishResponse(ResponseCode.Success, "Difficulty of $dishName dish was successfully changed\n\n", null)
    }
}