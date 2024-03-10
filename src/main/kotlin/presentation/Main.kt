package presentation

import data.models.AdminAccount
import data.models.VisitorAccount
import presentation.menu.MenuFactory
import services.models.ResponseCode

fun main() {
    val menuFactory = MenuFactory()
    val dishMenu = menuFactory.getDishMenu()
    val authMenu = menuFactory.getAuthMenu()
    val visitorMenu = menuFactory.getVisitorMenu()

    while (true) {
        val response = authMenu.dealWithUser()
        if (response.status == ResponseCode.Exiting)
            return
        if (response.account is AdminAccount)
            dishMenu.dealWithUser()
        else
            visitorMenu.dealWithUser(response.account as VisitorAccount)
    }
}