package br.com.schonmann.acejudgeserver.service

import br.com.schonmann.acejudgeserver.model.User
import br.com.schonmann.acejudgeserver.repository.UserRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.stereotype.Service

@Service
class UserService(@Autowired private val userRepository: UserRepository) {

    private val logger: Logger = LoggerFactory.getLogger(this.javaClass.name);

    fun findByUsername(username: String): User? {
        return userRepository.findByUsername(username)
    }

    fun createUser(username: String, password: String, name: String) {

    }
}