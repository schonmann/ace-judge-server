package br.com.schonmann.acejudgeserver.judge

import br.com.schonmann.acejudgeserver.enums.ProblemSubmissionStatusEnum
import org.springframework.stereotype.Service
import java.nio.file.Path

@Service
class ComplexityJudge {
    fun verdict(solution: Path, input : Path, output : Path): ProblemSubmissionStatusEnum {
        return ProblemSubmissionStatusEnum.CORRECT_ANSWER
    }
}