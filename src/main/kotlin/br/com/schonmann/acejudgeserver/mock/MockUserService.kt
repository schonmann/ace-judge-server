package br.com.schonmann.acejudgeserver.mock

import br.com.schonmann.acejudgeserver.enum.RoleEnum
import br.com.schonmann.acejudgeserver.service.RoleService
import br.com.schonmann.acejudgeserver.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MockUserService(@Autowired private val userService: UserService, private val roleService: RoleService) : MockService {
    override fun mock() {

        with(userService) {
            createUser(name = "ALUNO MOCKY", username = "mocky", password = "mocky", address = "Av. das Alfandegas", pictureUrl = "", roles = listOf(RoleEnum.ROLE_USER))
            createUser(name = "ALUNO FOO", username = "foo", password = "foo", address = "Av. das Alfandegas", pictureUrl = "", roles = listOf(RoleEnum.ROLE_USER))
            createUser(name = "ALUNO BAR", username = "bar", password = "bar", address = "Av. das Alfandegas", pictureUrl = "", roles = listOf(RoleEnum.ROLE_USER))
            createUser(name = "PROFESSOR", username = "professor", password = "professor", address = "Av. das Alfandegas", pictureUrl = "", roles = listOf(RoleEnum.ROLE_USER, RoleEnum.ROLE_ADMIN))
        }
    }
}