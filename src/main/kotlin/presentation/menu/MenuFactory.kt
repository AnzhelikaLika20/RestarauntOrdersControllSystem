package presentation.menu

import di.DI
import presentation.interfaces.AuthMenu
import presentation.interfaces.AdminMenu
import presentation.interfaces.VisitorMenu

class MenuFactory {
    fun getAuthMenu(): AuthMenu {
        return AuthMenuImpl(DI.authService)
    }

    fun getDishMenu() : AdminMenu {
        return AdminMenuImpl(DI.menuService, DI.statisticsService, DI.orderService)
    }

    fun getVisitorMenu() : VisitorMenu {
        return VisitorMenuImpl(DI.orderService, DI.menuService, DI.reviewService)
    }
}