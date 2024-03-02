package presentation.menu

import di.DI
import presentation.interfaces.AuthMenu
import presentation.interfaces.DishMenu

class MenuFactory {
    fun getAuthMenu(): AuthMenu {
        return AuthMenuImpl(DI.authService)
    }

    fun getDishMenu() : DishMenu {
        return DishMenuImpl(DI.menuService)
    }
}