package br.com.schonmann.acejudgeserver.controller

import br.com.schonmann.acejudgeserver.dto.SignupDTO
import br.com.schonmann.acejudgeserver.enums.RoleEnum
import br.com.schonmann.acejudgeserver.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/")
class AuthController(@Autowired private val userService: UserService) : BaseController {

    @PostMapping("/signup", produces = [MediaType.APPLICATION_JSON_VALUE], consumes = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    fun signup(dto : SignupDTO) {
        var roles = listOf(RoleEnum.ROLE_USER)
        if (dto.privilegeKey == "superace") {//TODO: undo this when you can :)
            roles = roles.plus(RoleEnum.ROLE_ADMIN)
        }
        userService.createUser(dto.username, dto.password, dto.name, dto.address, "", roles)
    }
}