package br.com.schonmann.acejudgeserver.service

import br.com.schonmann.acejudgeserver.model.User
import br.com.schonmann.acejudgeserver.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService(@Autowired private val userRepository: UserRepository) {

    fun findByUsername(username : String): User {
        return userRepository.findByUsername(username)
    }
}