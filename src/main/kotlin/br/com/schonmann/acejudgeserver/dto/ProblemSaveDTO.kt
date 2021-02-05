package br.com.schonmann.acejudgeserver.dto

import br.com.schonmann.acejudgeserver.enums.*
import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.web.multipart.MultipartFile

data class ProblemSaveDTO(
        val id: Long?,
        val name: String,
        val complexities: String,
        val bigoNotation: String,
        val score: Long,
        val problemDescription: String,
        val constraintDescription: String,
        val exampleInput: String,
        val exampleOutput: String,
        val category: ProblemCategoryEnum,
        val difficulty: ProblemDifficultyEnum,
        val visibility: ProblemVisibilityEnum,
        val judgeAnswerKeyProgramLanguage: LanguageEnum?,
        val inputGeneratorLanguage: LanguageEnum?,
        @JsonIgnore
        var judgeInputFile: MultipartFile?,
        @JsonIgnore
        var judgeAnswerKeyProgramFile: MultipartFile?,
        @JsonIgnore
        var inputGenerator: MultipartFile?
)