package br.com.schonmann.acejudgeserver.controller

import br.com.schonmann.acejudgeserver.dto.UserDTO
import br.com.schonmann.acejudgeserver.model.User
import br.com.schonmann.acejudgeserver.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user")
class UserController(@Autowired private val userService: UserService) : BaseController {

    @GetMapping("/data")
    fun getUserData() : ResponseEntity<UserDTO> {

        val user = userService.findByUsername(this.getRequestUser().username)

        if (user != null) {
            return ResponseEntity.ok(UserDTO(user))
        }

        return ResponseEntity.notFound().build()
    }
}