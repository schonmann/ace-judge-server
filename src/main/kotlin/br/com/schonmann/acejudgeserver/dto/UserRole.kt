package br.com.schonmann.acejudgeserver.dto

import br.com.schonmann.acejudgeserver.model.UserRole

data class UserRoleDTO(
    val role : String
) {
    constructor(userRole : UserRole) : this(userRole.role.name) {}
}