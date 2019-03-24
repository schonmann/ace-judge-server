package br.com.schonmann.acejudgeserver.repository

import br.com.schonmann.acejudgeserver.dto.RankDTO
import br.com.schonmann.acejudgeserver.model.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface RankRepository : JpaRepository<User, Long> {

    @Transactional
    @Query("select new br.com.schonmann.acejudgeserver.dto.RankDTO(0L, u.name, count(distinct p)) " +
            "from User u left join u.submissions s left join s.problem p where s.status = br.com.schonmann.acejudgeserver.enums.ProblemSubmissionStatusEnum.CORRECT_ANSWER and p.visibility = br.com.schonmann.acejudgeserver.enums.ProblemVisibilityEnum.PUBLIC group by s.problem, u order by count(distinct p) desc")
    fun getGeneralRank(pageable: Pageable): Page<RankDTO>

}