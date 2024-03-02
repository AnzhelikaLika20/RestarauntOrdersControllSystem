package presentation

import data.models.AdminAccount
import data.models.UserAccount
import data.models.VisitorAccount
import presentation.menu.MenuFactory

fun main() {
    val menuFactory = MenuFactory()
    val dishMenu = menuFactory.getDishMenu()
    val authMenu = menuFactory.getAuthMenu()

    val response = authMenu.dealWithUser()
    if(response.status == 400)
        return
    if(response.account is AdminAccount)
        dishMenu.dealWithUser()
}