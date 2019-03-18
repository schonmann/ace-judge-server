package br.com.schonmann.acejudgeserver.controller

import br.com.schonmann.acejudgeserver.dto.*
import br.com.schonmann.acejudgeserver.exception.ForbiddenException
import br.com.schonmann.acejudgeserver.model.Contest
import br.com.schonmann.acejudgeserver.service.ContestService
import com.querydsl.core.types.Predicate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.querydsl.binding.QuerydslPredicate
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/contest")
class ContestController(@Autowired private val contestService: ContestService) : BaseController {

    @GetMapping("/query", produces = [MediaType.APPLICATION_JSON_VALUE])
    @PreAuthorize("hasAuthority('VIEW')")
    fun getByFilter(pageable: Pageable, @QuerydslPredicate(root = Contest::class) predicate: Predicate?): Page<ContestListItemDTO> {
        val username = getRequestUser().username
        if (predicate != null) {
            return contestService.getByFilter(predicate, pageable).map { x -> ContestListItemDTO(x, username) }
        }
        return contestService.getByFilter(pageable).map { x -> ContestListItemDTO(x, username) }
    }

    @PostMapping("/save", produces = [MediaType.APPLICATION_JSON_VALUE])
    @PreAuthorize("hasAuthority('CONTEST_CRUD')")
    fun save(@RequestBody dto: ContestSaveDTO) {
        contestService.save(getRequestUser().username, dto)
    }

    @GetMapping("/getById", produces = [MediaType.APPLICATION_JSON_VALUE])
    @PreAuthorize("hasAuthority('VIEW')")
    fun getById(@RequestParam("id") id: Long): ResponseEntity<ContestDTO> {
        val contest = contestService.getById(id)
        return ResponseEntity.ok(ContestDTO(contest))
    }

    @GetMapping("/getByIdIfAuthorized", produces = [MediaType.APPLICATION_JSON_VALUE])
    @PreAuthorize("hasAuthority('VIEW')")
    fun getByIdIfAuthorized(@RequestParam("id") id: Long): ResponseEntity<ContestDTO> {
        val contest = contestService.getById(id)
        if (!contest.participants.any { x -> x.username == getRequestUser().username }) {
            throw ForbiddenException("user without contest permission")
        }
        return ResponseEntity.ok(ContestDTO(contest))
    }

    @PostMapping("/join", produces = [MediaType.APPLICATION_JSON_VALUE])
    @PreAuthorize("hasAuthority('VIEW')")
    fun join(@RequestBody dto: JoinContestDTO) {
        val username = getRequestUser().username
        contestService.join(username, dto.contestId, dto.password)
    }
}