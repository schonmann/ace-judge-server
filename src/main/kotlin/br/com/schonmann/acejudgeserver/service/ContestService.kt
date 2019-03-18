package br.com.schonmann.acejudgeserver.service

import br.com.schonmann.acejudgeserver.dto.ContestSaveDTO
import br.com.schonmann.acejudgeserver.dto.SelectDTO
import br.com.schonmann.acejudgeserver.exception.ForbiddenException
import br.com.schonmann.acejudgeserver.model.Contest
import br.com.schonmann.acejudgeserver.model.Problem
import br.com.schonmann.acejudgeserver.model.User
import br.com.schonmann.acejudgeserver.repository.ContestRepository
import br.com.schonmann.acejudgeserver.repository.ProblemRepository
import br.com.schonmann.acejudgeserver.repository.UserRepository
import br.com.schonmann.acejudgeserver.util.sumTimeString
import com.querydsl.core.types.Predicate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.PermissionDeniedDataAccessException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*

@Service
class ContestService(@Autowired private val contestRepository: ContestRepository,
                     private val userRepository: UserRepository, private val problemRepository: ProblemRepository) {

    fun getByFilter(predicate: Predicate, pageable: Pageable): Page<Contest> {
        return contestRepository.findAll(predicate, pageable)
    }

    fun getByFilter(pageable: Pageable): Page<Contest> {
        return contestRepository.findAll(pageable)
    }

    fun getByNameContaining(pageable: Pageable, name : String) : Page<Contest> {
        return contestRepository.findByNameContaining(pageable, name)
    }

    fun getById(id: Long): Contest {
        return contestRepository.getOne(id)
    }

    fun save(username: String, c: ContestSaveDTO) {

        val user : User = userRepository.getOneByUsername(username)

        val problems : List<Problem> = problemRepository.findByIdIn(c.problemsIds)

        val contest = Contest(c.id ?: 0 , c.name, c.password, c.description,
                c.startDate.sumTimeString(c.startTime), c.endDate.sumTimeString(c.endTime), ArrayList(), problems, user)

        contestRepository.findByIdOrNull(c.id ?: 0)?.let { dbContest ->
            // Keep original references from database.
            contest.admin = dbContest.admin
            contest.participants = dbContest.participants
        }

        contestRepository.save(contest)
    }

    @Throws(PermissionDeniedDataAccessException::class)
    fun join(username: String, contestId: Long, password: String) {
        val contest = contestRepository.getOne(contestId)

        if (contest.password != password) {
            throw ForbiddenException("wrong password")
        }

        val user = userRepository.getOneByUsername(username)
        contest.participants = contest.participants.plus(user)

        contestRepository.save(contest)
    }
}