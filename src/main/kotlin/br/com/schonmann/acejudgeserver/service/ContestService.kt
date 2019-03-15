package br.com.schonmann.acejudgeserver.service

import br.com.schonmann.acejudgeserver.dto.ContestSaveDTO
import br.com.schonmann.acejudgeserver.model.Contest
import br.com.schonmann.acejudgeserver.repository.ContestRepository
import br.com.schonmann.acejudgeserver.repository.UserRepository
import com.querydsl.core.types.Predicate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class ContestService(@Autowired private val contestRepository: ContestRepository,
                     private val userRepository: UserRepository) {

    fun getByFilter(predicate: Predicate, pageable: Pageable): Page<Contest> {
        return contestRepository.findAll(predicate, pageable)
    }

    fun getByFilter(pageable: Pageable): Page<Contest> {
        return contestRepository.findAll(pageable)
    }

    fun getById(id: Long): Contest {
        return contestRepository.getOne(id)
    }

    fun save(username: String, c: ContestSaveDTO) {
        val user = userRepository.getOneByUsername(username)
        val contest = Contest(0, c.name, c.password, c.description, c.startDate, c.endDate, ArrayList(), user)
        contestRepository.save(contest)
    }
}