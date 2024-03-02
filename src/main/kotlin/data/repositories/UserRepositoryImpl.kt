package data.repositories

import data.Interfaces.UserRepository
import data.models.AdminAccount
import data.models.UserAccount
import data.models.VisitorAccount
import kotlinx.serialization.encodeToString
import java.io.File
import java.io.FileNotFoundException
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic


class UserRepositoryImpl(private val pathToSerializedStorage: String) : UserRepository {
    override fun storeInfo(info: String) {
        val file = File(pathToSerializedStorage)
        file.writeText(info)
    }

    override fun loadInfo() : String {
        val file = File(pathToSerializedStorage)
        try {
            return file.readText()
        } catch (ex: FileNotFoundException) {
            file.createNewFile()
            return ""
        }
    }

    override fun signUpUser(user : UserAccount) {
        val infoFromFile = loadInfo()
        val json = Json {
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
        val serializedInfo = Json.encodeToString(listOfUsers.toList())
        storeInfo(serializedInfo)
    }

    override fun signInUser(login: String) {
        val infoFromFile = loadInfo()
        val listOfUsers: MutableList<UserAccount> =
            if (infoFromFile.isBlank()) mutableListOf() else Json.decodeFromString<List<UserAccount>>(
                infoFromFile
            ).toMutableList()
        listOfUsers.removeIf { x -> x.login == login }
        val user = getAccountByLogin(login)
        user.isActive = true
        listOfUsers.add(user)
        val serializedInfo = Json.encodeToString(listOfUsers)
        storeInfo(serializedInfo)
    }

    override fun containsLogin(login: String): Boolean {
        val infoFromFile = loadInfo()
        val listOfUsers: MutableList<UserAccount> =
            if (infoFromFile.isBlank()) mutableListOf() else Json.decodeFromString<List<UserAccount>>(
                infoFromFile
            ).toMutableList()
        return listOfUsers.any{x -> x.login == login}
    }

    override fun getAccountByLogin(login: String): UserAccount {
        val infoFromFile = loadInfo()
        val listOfUsers: MutableList<UserAccount> =
            if (infoFromFile.isBlank()) mutableListOf() else Json.decodeFromString<List<UserAccount>>(
                infoFromFile
            ).toMutableList()
        return listOfUsers.single { x -> x.login == login }
    }
}