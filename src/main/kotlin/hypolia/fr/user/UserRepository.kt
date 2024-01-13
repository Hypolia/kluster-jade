package hypolia.fr.user

data class User(val name: String)

interface UserRepository {
    fun findUser(name: String): User?
    fun addUsers(users: List<User>)
}


class UserRepositoryImpl : UserRepository {
    private val _users = arrayListOf<User>(User("nathael"))

    override fun findUser(name: String): User? {
        return _users.firstOrNull { it.name == name }
    }

    override fun addUsers(users : List<User>) {
        _users.addAll(users)
    }
}
