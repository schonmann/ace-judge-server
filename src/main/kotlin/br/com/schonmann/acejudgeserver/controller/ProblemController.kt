package br.com.schonmann.acejudgeserver.controller

import br.com.schonmann.acejudgeserver.dto.ProblemDTO
import br.com.schonmann.acejudgeserver.dto.ProblemSaveDTO
import br.com.schonmann.acejudgeserver.dto.SelectDTO
import br.com.schonmann.acejudgeserver.model.Problem
import br.com.schonmann.acejudgeserver.service.ProblemService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.querydsl.core.types.Predicate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.querydsl.binding.QuerydslPredicate
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/problem")
class ProblemController(@Autowired private val problemService : ProblemService, private val objectMapper: ObjectMapper) : BaseController {

    fun problemDtoWithSolvedFlag(problem : Problem) : ProblemDTO {
        val dto = ProblemDTO(problem)
        dto.solved = problemService.isProblemSolved(problem, getRequestUser().username)
        return dto
    }

    @GetMapping("/query", produces = [MediaType.APPLICATION_JSON_VALUE])
    @PreAuthorize("hasAuthority('VIEW')")
    fun getByFilter(pageable: Pageable, @QuerydslPredicate(root = Problem::class) predicate : Predicate?): Page<ProblemDTO> {
        if (predicate != null) {
            return problemService.getByFilter(predicate, pageable).map { p -> problemDtoWithSolvedFlag(p) }
        }
        return problemService.getByFilter(pageable).map { p -> problemDtoWithSolvedFlag(p) }
    }

    @GetMapping("/queryByContest", produces = [MediaType.APPLICATION_JSON_VALUE])
    @PreAuthorize("hasAuthority('VIEW')")
    fun getByContest(pageable: Pageable, @RequestParam contestId: Long): Page<ProblemDTO> {
        return problemService.getByContestsContaining(pageable, contestId).map { p -> problemDtoWithSolvedFlag(p) }
    }

    @PostMapping("/save", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @PreAuthorize("hasAuthority('PROBLEM_CRUD')")
    fun save(@RequestParam judgeInput: MultipartFile?, @RequestParam judgeOutput: MultipartFile?, @RequestParam inputGeneratorFile: MultipartFile?, @RequestParam params : String) {

        val dto : ProblemSaveDTO = objectMapper.readValue(params)

        dto.judgeInputFile = judgeInput
        dto.judgeOutputFile = judgeOutput
        dto.inputGeneratorFile = inputGeneratorFile

        problemService.save(dto)
    }

    @GetMapping("/getById", produces = [MediaType.APPLICATION_JSON_VALUE])
    @PreAuthorize("hasAuthority('VIEW')")
    fun getById(@RequestParam("id") id : Long): ResponseEntity<ProblemDTO> {
        val problem = problemService.getById(id)
        return ResponseEntity.ok(ProblemDTO(problem))
    }

    @GetMapping("/queryByName", produces = [MediaType.APPLICATION_JSON_VALUE])
    @PreAuthorize("hasAuthority('VIEW')")
    fun queryByName(pageable: Pageable, @RequestParam name : String): Page<SelectDTO> {
        return problemService.getByNameContaining(pageable, name).map { x -> SelectDTO(x.id, x.name) }
    }
}