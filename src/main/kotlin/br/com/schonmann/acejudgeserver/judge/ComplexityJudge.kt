package br.com.schonmann.acejudgeserver.judge

import br.com.schonmann.acejudgeserver.enums.ProblemSubmissionCorrectnessStatusEnum
import org.springframework.stereotype.Service
import java.nio.file.Path

@Service
class ComplexityJudge {
    fun verdict(solution: Path, input : Path, output : Path): ProblemSubmissionCorrectnessStatusEnum {
        return ProblemSubmissionCorrectnessStatusEnum.CORRECT_ANSWER
    }
}