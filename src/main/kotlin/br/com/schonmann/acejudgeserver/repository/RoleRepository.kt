package br.com.schonmann.acejudgeserver.repository

import br.com.schonmann.acejudgeserver.enums.RoleEnum
import br.com.schonmann.acejudgeserver.model.Role
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RoleRepository : JpaRepository<Role, Long> {
    fun findByRole(role : RoleEnum) : Role?
}