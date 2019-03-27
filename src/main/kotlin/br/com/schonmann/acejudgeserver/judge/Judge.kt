package br.com.schonmann.acejudgeserver.judge

import br.com.schonmann.acejudgeserver.enums.LanguageEnum
import br.com.schonmann.acejudgeserver.enums.ProblemSubmissionStatusEnum
import java.nio.file.Path

interface Judge {
    fun verdict(solution: Path, language: LanguageEnum, judgeInput: Path, judgeOutput: Path): ProblemSubmissionStatusEnum
}