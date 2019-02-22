package br.com.schonmann.acejudgeserver.controller

import br.com.schonmann.acejudgeserver.model.User
import br.com.schonmann.acejudgeserver.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user")
class UserController(@Autowired private val userService: UserService) {

    @GetMapping("/data")
    fun getUserData(@RequestParam("username") username : String) : User {
        return userService.findByUsername(username)
    }
}