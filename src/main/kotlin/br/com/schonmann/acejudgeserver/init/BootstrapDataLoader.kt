package br.com.schonmann.acejudgeserver.init

import br.com.schonmann.acejudgeserver.enum.PrivilegeEnum
import br.com.schonmann.acejudgeserver.enum.RoleEnum
import br.com.schonmann.acejudgeserver.mock.MockUserService
import br.com.schonmann.acejudgeserver.model.Privilege
import br.com.schonmann.acejudgeserver.model.Role
import br.com.schonmann.acejudgeserver.service.PrivilegeService
import br.com.schonmann.acejudgeserver.service.RoleService
import br.com.schonmann.acejudgeserver.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.stream.Collectors

@Component
class BootstrapDataLoader(
        @Autowired
        private val roleService: RoleService,
        private val privilegeService: PrivilegeService,
        private val mockUserService: MockUserService
) : ApplicationListener<ContextRefreshedEvent> {

    private var alreadySetup: Boolean = false

    @Transactional
    fun createPrivilegeIfNotFound(privilege: PrivilegeEnum): Privilege {
        privilegeService.findByPrivilege(privilege)?.let {
            return it
        }
        return privilegeService.createPrivilege(privilege)
    }

    @Transactional
    fun createRoleIfNotFound(role: RoleEnum, privileges: Collection<PrivilegeEnum>): Role {
        roleService.findByRole(role)?.let {
            return it
        }
        val privilegesFromDB = privileges.stream().map { x -> createPrivilegeIfNotFound(x) }.collect(Collectors.toList())
        return roleService.createRole(role, privilegesFromDB)
    }

    override fun onApplicationEvent(event: ContextRefreshedEvent) {

        if (alreadySetup) {
            return
        }

        createRoleIfNotFound(role = RoleEnum.ROLE_ADMIN, privileges = listOf(PrivilegeEnum.ALL))
        createRoleIfNotFound(role = RoleEnum.ROLE_USER, privileges = listOf(PrivilegeEnum.VIEW, PrivilegeEnum.PROBLEM_SUBMIT, PrivilegeEnum.PROBLEM_VIEW))

        mockUserService.mock()

        alreadySetup = true
    }
}