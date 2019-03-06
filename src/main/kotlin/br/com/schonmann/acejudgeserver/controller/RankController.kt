package br.com.schonmann.acejudgeserver.controller

import br.com.schonmann.acejudgeserver.dto.RankDTO
import br.com.schonmann.acejudgeserver.service.RankService
import org.jetbrains.annotations.NotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/rank")
class RankController(@Autowired private val rankService: RankService) : BaseController {

    @GetMapping("/general", params = ["page", "size"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @PreAuthorize("hasAuthority('VIEW')")
    fun getGeneralRank(@NotNull pageable: Pageable): Page<RankDTO> {
        return rankService.getGeneralRank(pageable)
    }
}