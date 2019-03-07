package br.com.schonmann.acejudgeserver.service

import br.com.schonmann.acejudgeserver.enums.PrivilegeEnum
import br.com.schonmann.acejudgeserver.model.Privilege
import br.com.schonmann.acejudgeserver.repository.PrivilegeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class PrivilegeService(@Autowired private val privilegeRepository: PrivilegeRepository) {

    fun findByPrivilege(privilege: PrivilegeEnum) : Privilege? {
        return privilegeRepository.findByPrivilege(privilege)
    }

    fun createPrivilege(privilege : PrivilegeEnum) : Privilege{
        return privilegeRepository.save(Privilege(privilege = privilege))
    }
}