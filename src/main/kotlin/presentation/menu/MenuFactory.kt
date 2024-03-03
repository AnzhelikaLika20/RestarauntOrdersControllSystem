package presentation.menu

import di.DI
import presentation.interfaces.AuthMenu
import presentation.interfaces.DishMenu
import presentation.interfaces.VisitorMenu

class MenuFactory {
    fun getAuthMenu(): AuthMenu {
        return AuthMenuImpl(DI.authService)
    }

    fun getDishMenu() : DishMenu {
        return DishMenuImpl(DI.menuService)
    }

    fun getVisitorMenu() : VisitorMenu {
        return VisitorMenuImpl(DI.orderService)
    }
}