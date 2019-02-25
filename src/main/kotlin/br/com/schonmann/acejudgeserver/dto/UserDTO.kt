package br.com.schonmann.acejudgeserver.dto

import br.com.schonmann.acejudgeserver.model.User
import java.util.stream.Collectors


data class UserDTO(
        val name: String,
        val username: String,
        val password: String,
        val pictureUrl: String?,
        val roles: List<String>
) {
    constructor(user: User) : this(
            user.name, user.username, user.password, user.pictureUrl,
            user.roles.stream().map { x -> x.role.name }.collect(Collectors.toList())
    )
}