package data.Interfaces

import data.models.UserAccount

interface UserRepository {
    fun createAccount(user : UserAccount)
    fun enterAccount(login : String)
    fun containsLogin(login : String) : Boolean
    fun getAccountByLogin(login : String) : UserAccount?
}