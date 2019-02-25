package br.com.schonmann.acejudgeserver.mock

import br.com.schonmann.acejudgeserver.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct

@Service
class MockService(@Autowired val userService: UserService) {
    fun mock() {
        with(userService) {
            createUser(username = "user", password = "user", name = "Aluno Mock")
            createUser(username = "admin", password = "admin", name = "Professor Mock")
        }
    }
}