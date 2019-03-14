package br.com.schonmann.acejudgeserver.service

import br.com.schonmann.acejudgeserver.enums.RoleEnum
import br.com.schonmann.acejudgeserver.model.User
import br.com.schonmann.acejudgeserver.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.stream.Collectors

@Service
class UserService(@Autowired private val userRepository: UserRepository, private val roleService: RoleService) {

    private val defaultRole = RoleEnum.ROLE_USER

    fun findByUsername(username: String): User? {
        return userRepository.findByUsername(username)
    }

    @Transactional
    fun createUser(username: String, password: String, name: String, address: String, pictureUrl: String, roles: Collection<RoleEnum>) {
        if(userRepository.existsByUsername(username)) {
            return
        }
        if (!roles.contains(defaultRole)) {
            roles.plus(defaultRole)
        }
        val rolesDB = roles.stream().map { x -> roleService.findByRole(x)!! }.collect(Collectors.toList())
        userRepository.save(User(username = username, password = password, name = name, address = address, pictureUrl = pictureUrl, roles = rolesDB))
    }
}