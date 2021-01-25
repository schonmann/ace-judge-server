package br.com.schonmann.acejudgeserver.controller

import br.com.schonmann.acejudgeserver.dto.ProblemStatisticsDTO
import br.com.schonmann.acejudgeserver.dto.ProblemSubmissionDTO
import br.com.schonmann.acejudgeserver.dto.SubmitSolutionDTO
import br.com.schonmann.acejudgeserver.service.ProblemSubmissionService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.jetbrains.annotations.NotNull
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.PostMapping
import java.nio.charset.Charset


@RestController
@RequestMapping("/api/problem-submission")
class ProblemSubmissionController(@Autowired private val problemSubmissionService: ProblemSubmissionService, private val objectMapper: ObjectMapper) : BaseController {

    val logger = LoggerFactory.getLogger(this::class.simpleName)

    @GetMapping("/mine", params = ["page", "size"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @PreAuthorize("hasAuthority('VIEW')")
    @Transactional
    fun getMySubmissions(@NotNull pageable: Pageable): Page<ProblemSubmissionDTO> {
        return problemSubmissionService.getMySubmissions(getRequestUser().username, pageable).map { x -> ProblemSubmissionDTO(x) }
    }

    @GetMapping("/getById", params = ["id"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @PreAuthorize("hasAuthority('VIEW')")
    @Transactional
    fun getById(@RequestParam("id") id : Long): ProblemSubmissionDTO {
        val submission = problemSubmissionService.getById(id)
        return ProblemSubmissionDTO(submission)
    }

    @PostMapping("/submit", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @PreAuthorize("hasAuthority('VIEW')")
    fun submitProblem(@RequestParam("file") solutionFile: MultipartFile, @RequestParam params : String) {

        val dto : SubmitSolutionDTO = objectMapper.readValue(params)

        dto.solutionFile = solutionFile

        problemSubmissionService.submitSolution(getRequestUser().username, dto)
    }

    @GetMapping("/statistics", produces = [MediaType.APPLICATION_JSON_VALUE])
    @PreAuthorize("hasAuthority('VIEW')")
    @Transactional
    fun getSubmissionStatistics(): ProblemStatisticsDTO {
        return problemSubmissionService.getSubmissionStatistics(getRequestUser().username)
    }
}