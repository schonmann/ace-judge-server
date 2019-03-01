package br.com.schonmann.acejudgeserver.dto

import br.com.schonmann.acejudgeserver.model.User
import java.util.stream.Collector
import java.util.stream.Collectors

data class UserDTO(
        val name: String,
        val username: String,
        val password: String,
        val pictureUrl: String?,
        val roles: List<RoleDTO>
) {
    constructor(user: User) : this(
            user.name, user.username, user.password, user.pictureUrl,
            user.roles.stream().map { x -> RoleDTO(x) }.collect(Collectors.toList())
    )
}