package br.com.schonmann.acejudgeserver.dto

import br.com.schonmann.acejudgeserver.model.Role

data class RoleDTO(
    val name: String,
    val privileges: List<String>
) {
    constructor(r : Role) : this(name=r.role.name, privileges=r.privileges.map { p -> p.privilege.name })
}