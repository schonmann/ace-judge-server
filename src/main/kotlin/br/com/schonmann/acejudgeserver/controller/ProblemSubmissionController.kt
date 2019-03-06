package br.com.schonmann.acejudgeserver.controller

import br.com.schonmann.acejudgeserver.dto.ProblemSubmissionDTO
import br.com.schonmann.acejudgeserver.dto.RankDTO
import br.com.schonmann.acejudgeserver.service.ProblemSubmissionService
import br.com.schonmann.acejudgeserver.service.RankService
import org.jetbrains.annotations.NotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/problem-submission")
class ProblemSubmissionController(@Autowired private val problemSubmissionService: ProblemSubmissionService) : BaseController {

    @GetMapping("/mine", params = ["page", "size"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @PreAuthorize("hasAuthority('VIEW')")
    @Transactional
    fun getMySubmissions(@NotNull pageable: Pageable): Page<ProblemSubmissionDTO> {
        return problemSubmissionService.getMySubmissions(getRequestUser().username, pageable).map { x -> ProblemSubmissionDTO(x) }
    }
}