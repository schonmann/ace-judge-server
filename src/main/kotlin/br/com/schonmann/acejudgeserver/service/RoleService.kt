package br.com.schonmann.acejudgeserver.service

import br.com.schonmann.acejudgeserver.enum.RoleEnum
import br.com.schonmann.acejudgeserver.model.Privilege
import br.com.schonmann.acejudgeserver.model.Role
import br.com.schonmann.acejudgeserver.repository.RoleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class RoleService(@Autowired private val roleRepository: RoleRepository) {

    fun createRole(role: RoleEnum, privileges: Collection<Privilege>) : Role{
        return roleRepository.save(Role(role = role, privileges = privileges))
    }

    fun findByRole(role: RoleEnum) : Role? {
        return roleRepository.findByRole(role)
    }
}