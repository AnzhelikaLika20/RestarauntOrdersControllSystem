package data.Interfaces

import data.models.UserAccount

interface UserRepository {
    fun storeInfo(info: String)
    fun loadInfo() : String
    fun createAccount(user : UserAccount)
    fun enterAccount(login : String)
    fun containsLogin(login : String) : Boolean
    fun getAccountByLogin(login : String) : UserAccount?
}