package br.com.schonmann.acejudgeserver.controller

import br.com.schonmann.acejudgeserver.dto.UserDTO
import br.com.schonmann.acejudgeserver.enum.RoleEnum
import br.com.schonmann.acejudgeserver.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.security.core.context.SecurityContextHolder



@RestController
@RequestMapping("/api/user")
class UserController(@Autowired private val userService: UserService) : BaseController {

    @GetMapping("/data")
    @PreAuthorize("hasAuthority('VIEW')")
    fun getUserData(): ResponseEntity<UserDTO> {

        val authentication = SecurityContextHolder.getContext().authentication

        for (x in authentication.authorities) {
            println(x.authority)
        }

        userService.findByUsername(this.getRequestUser().username)?.let {
            return ResponseEntity.ok(UserDTO(it))
        }

        return ResponseEntity.notFound().build()
    }
}