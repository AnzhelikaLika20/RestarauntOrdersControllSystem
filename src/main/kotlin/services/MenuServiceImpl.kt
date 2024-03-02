package services

import data.models.Dish
import data.repositories.DishRepositoryImpl
import services.interfaces.MenuService
import services.models.AdminResponse
import services.models.AuthResponse

class MenuServiceImpl(private val dishRepository: DishRepositoryImpl) : MenuService {
    override fun addDish(dish : Dish) : AdminResponse {
        if (dishRepository.containsDish(dish.name)) {
            return AdminResponse(401,
                "Dish with name ${dish.name} is already exists. Do you want to replace it?", dish)
        }
        dishRepository.addDish(dish)
        return AdminResponse(200, "Dish was successfully added\n\n", dish)
    }

    override fun updateDish(dish : Dish) : AdminResponse {
        dishRepository.addDish(dish)
        return AdminResponse(200, "Dish was successfully updated\n\n", dish)

    }

    override fun dropDish() : AdminResponse {
        TODO("Not yet implemented")
    }

    override fun setAmountOfDish() : AdminResponse {
        TODO("Not yet implemented")
    }

    override fun setPriceOfDish() : AdminResponse {
        TODO("Not yet implemented")
    }

    override fun setDifficultyOfDish() : AdminResponse {
        TODO("Not yet implemented")
    }
}