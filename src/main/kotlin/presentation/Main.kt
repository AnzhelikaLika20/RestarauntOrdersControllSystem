package presentation

import data.models.AdminAccount
import data.models.UserAccount
import data.models.VisitorAccount
import presentation.menu.MenuFactory
import services.models.ResponseCode

fun main() {
    val menuFactory = MenuFactory()
    val dishMenu = menuFactory.getDishMenu()
    val authMenu = menuFactory.getAuthMenu()

    val response = authMenu.dealWithUser()
    if(response.status == ResponseCode.Exiting)
        return
    if(response.account is AdminAccount)
        dishMenu.dealWithUser()
}