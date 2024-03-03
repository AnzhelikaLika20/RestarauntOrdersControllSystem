package services

import data.models.Dish
import data.repositories.DishRepositoryImpl
import services.interfaces.MenuService
import services.models.DishResponse

class MenuServiceImpl(private val dishRepository: DishRepositoryImpl) : MenuService {
    private val hintDishIsAbsent : String = "There is no dish with such name\n\n"
    override fun addDish(dish : Dish) : DishResponse {
        if (dishRepository.containsDish(dish.name))
            return DishResponse(401,
                "Dish with name ${dish.name} already exists. Do you want to replace it? Enter Yes or No", dish)
        dishRepository.addDish(dish)
        return DishResponse(200, "Dish was successfully added\n\n", dish)
    }

    override fun updateDish(dish : Dish) : DishResponse {
        dishRepository.addDish(dish)
        return DishResponse(200, "Dish was successfully updated\n\n", dish)

    }

    override fun dropDish(dishName : String) : DishResponse {
        if(!dishRepository.containsDish(dishName))
            return DishResponse(200, hintDishIsAbsent, null)
        dishRepository.dropDish(dishName)
        return DishResponse(200, "Dish was successfully dropped\n\n", null)
    }

    override fun setAmountOfDish(dishName : String, amount : Int) : DishResponse {
        if(!dishRepository.containsDish(dishName))
            return DishResponse(200, hintDishIsAbsent, null)
        dishRepository.setAmountForDish(dishName, amount)
        return DishResponse(200, "Amount of $dishName dish was successfully changed\n\n", null)
    }

    override fun setPriceOfDish(dishName : String, price : Double) : DishResponse {
        if(!dishRepository.containsDish(dishName))
            return DishResponse(200, hintDishIsAbsent, null)
        dishRepository.setPriceForDish(dishName, price)
        return DishResponse(200, "Price of $dishName dish was successfully changed\n\n", null)
    }

    override fun setDifficultyOfDish(dishName : String, difficulty : Int) : DishResponse {
        if(!dishRepository.containsDish(dishName))
            return DishResponse(200, hintDishIsAbsent, null)
        dishRepository.setDifficultyForDish(dishName, difficulty)
        return DishResponse(200, "Difficulty of $dishName dish was successfully changed\n\n", null)
    }
}