package br.com.schonmann.acejudgeserver.init

import br.com.schonmann.acejudgeserver.enums.PrivilegeEnum
import br.com.schonmann.acejudgeserver.enums.ProblemCategoryEnum
import br.com.schonmann.acejudgeserver.enums.RoleEnum
import br.com.schonmann.acejudgeserver.mock.MockUserService
import br.com.schonmann.acejudgeserver.model.Privilege
import br.com.schonmann.acejudgeserver.model.ProblemCategory
import br.com.schonmann.acejudgeserver.model.Role
import br.com.schonmann.acejudgeserver.service.PrivilegeService
import br.com.schonmann.acejudgeserver.service.ProblemCategoryService
import br.com.schonmann.acejudgeserver.service.RoleService
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
        private val mockUserService: MockUserService,
        private val problemCategoryService: ProblemCategoryService
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

    @Transactional
    fun createProblemCategoryIfNotFound(category : ProblemCategoryEnum, image : String) : ProblemCategory {
        problemCategoryService.findByCategory(category)?.let {
            return it
        }
        return problemCategoryService.createCategory(category, image)
    }

    override fun onApplicationEvent(event: ContextRefreshedEvent) {

        if (alreadySetup) {
            return
        }

        createRoleIfNotFound(role = RoleEnum.ROLE_ADMIN, privileges = listOf(PrivilegeEnum.ALL))
        createRoleIfNotFound(role = RoleEnum.ROLE_USER, privileges = listOf(PrivilegeEnum.VIEW, PrivilegeEnum.PROBLEM_SUBMIT, PrivilegeEnum.PROBLEM_VIEW))

        createProblemCategoryIfNotFound(category = ProblemCategoryEnum.AD_HOC, image = "https://picsum.photos/200")
        createProblemCategoryIfNotFound(category = ProblemCategoryEnum.COMPUTATIONAL_GEOMETRY, image = "https://picsum.photos/200")
        createProblemCategoryIfNotFound(category = ProblemCategoryEnum.DYNAMIC_PROGRAMMING, image = "https://picsum.photos/200")
        createProblemCategoryIfNotFound(category = ProblemCategoryEnum.NUMBER_THEORY, image = "https://picsum.photos/200")

        mockUserService.mock()

        alreadySetup = true
    }
}