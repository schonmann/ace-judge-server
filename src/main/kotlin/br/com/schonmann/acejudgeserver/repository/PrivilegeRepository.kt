package br.com.schonmann.acejudgeserver.repository

import br.com.schonmann.acejudgeserver.enums.PrivilegeEnum
import br.com.schonmann.acejudgeserver.model.Privilege
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PrivilegeRepository : JpaRepository<Privilege, Long> {
    fun findByPrivilege(privilege: PrivilegeEnum) : Privilege?
}