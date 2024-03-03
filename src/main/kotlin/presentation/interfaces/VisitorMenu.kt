package presentation.interfaces

interface VisitorMenu {
    fun getCurrentOrderStatus(id : Int)
    fun createOrder()
    fun addDishIntoExistingOrder(id : Int)
    fun payOrder(id: Int)
}