package data.Interfaces

import data.models.UserAccount

interface UserRepository {
    fun storeInfo(info: String)
    fun loadInfo() : String
    fun signUpUser(user : UserAccount)
    fun signInUser(login : String)
    fun containsLogin(login : String) : Boolean
    fun getAccountByLogin(login : String) : UserAccount?
}