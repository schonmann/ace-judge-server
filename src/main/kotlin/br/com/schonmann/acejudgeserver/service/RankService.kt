package br.com.schonmann.acejudgeserver.service

import br.com.schonmann.acejudgeserver.dto.RankDTO
import br.com.schonmann.acejudgeserver.repository.RankRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class RankService(@Autowired private val rankRepository: RankRepository) {

    fun getGeneralRank(pageable: Pageable): Page<RankDTO> {
        return rankRepository.getGeneralRank(pageable)
    }

    fun getRankByContest(pageable: Pageable): Page<RankDTO> {
        return Page.empty()
    }
}