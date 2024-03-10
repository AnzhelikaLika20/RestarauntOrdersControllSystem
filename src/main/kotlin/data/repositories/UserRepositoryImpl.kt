package data.repositories

import data.Interfaces.UserRepository
import data.models.AdminAccount
import data.models.Order
import data.models.UserAccount
import data.models.VisitorAccount
import kotlinx.serialization.encodeToString
import java.io.File
import java.io.FileNotFoundException
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic


class UserRepositoryImpl(private val pathToSerializedStorage: String) : UserRepository {
    private val json = Json{prettyPrint = true}
    private fun storeInfo(listOrders: List<UserAccount>) {
        val file = File(pathToSerializedStorage)
        val serializedInfo = json.encodeToString(listOrders)
        file.writeText(serializedInfo)
    }

    fun loadInfo() : String {
        val file = File(pathToSerializedStorage)
        try {
            return file.readText()
        } catch (_: FileNotFoundException) {
            file.createNewFile()
            return ""
        }
    }

    override fun createAccount(user : UserAccount) {
        val infoFromFile = loadInfo()
        val json = Json {
            prettyPrint = true
            serializersModule = SerializersModule {
                polymorphic(UserAccount::class) {
                    subclass(AdminAccount::class, AdminAccount.serializer())
                    subclass(VisitorAccount::class, VisitorAccount.serializer())
                }
            }
        }
        val listOfUsers = if (infoFromFile.isBlank()) mutableListOf() else
            json.decodeFromString<List<UserAccount>>(infoFromFile).toMutableList()
        user.isActive = true
        listOfUsers.addLast(user)
        storeInfo(listOfUsers)
    }

    override fun enterAccount(login: String) {
        val listOfUsers = getAllUsers().toMutableList()
        listOfUsers.removeIf { x -> x.login == login }
        val user = getAccountByLogin(login)
        user.isActive = true
        listOfUsers.add(user)
        storeInfo(listOfUsers)
    }

    override fun containsLogin(login: String): Boolean {
        val listOfUsers = getAllUsers()
        return listOfUsers.any{x -> x.login == login}
    }

    override fun getAccountByLogin(login: String): UserAccount {
        val listOfUsers = getAllUsers()
        return listOfUsers.single { x -> x.login == login }
    }
    private fun getAllUsers() : List<UserAccount> {
        val infoFromFile = loadInfo()
        val listOfUsers =
            if (infoFromFile.isBlank()) listOf() else Json.decodeFromString<List<UserAccount>>(
                infoFromFile
            )
        return listOfUsers
    }
}