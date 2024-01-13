package hypolia.fr.user

class UserService(private val userRepository: UserRepository) {
    fun getDefaultUser(): User = userRepository.findUser("nathael") ?: error("Can't find nathael user")
}
