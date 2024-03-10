package presentation.interfaces

import data.models.VisitorAccount
import services.models.Response

interface VisitorMenu {
    fun displayMenuOptions()
    fun dealWithUser(account: VisitorAccount): Response
}