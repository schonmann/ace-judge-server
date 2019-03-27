package br.com.schonmann.acejudgeserver.dto

import br.com.schonmann.acejudgeserver.enums.LanguageEnum
import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.web.multipart.MultipartFile

data class SubmitSolutionDTO(
        val problemId: Long,
        val timestamp: Long,
        val contestId: Long?,
        val language: LanguageEnum,
        @JsonIgnore
        var solutionFile: MultipartFile?
)