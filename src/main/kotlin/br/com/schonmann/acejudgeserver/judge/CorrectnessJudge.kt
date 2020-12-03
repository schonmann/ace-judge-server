package br.com.schonmann.acejudgeserver.judge

import br.com.schonmann.acejudgeserver.compiler.UniversalCompiler
import br.com.schonmann.acejudgeserver.dto.VerdictDTO
import br.com.schonmann.acejudgeserver.enums.LanguageEnum
import br.com.schonmann.acejudgeserver.enums.ProblemSubmissionCorrectnessStatusEnum
import br.com.schonmann.acejudgeserver.model.CompilerResult
import br.com.schonmann.acejudgeserver.model.SolutionRunnerResult
import br.com.schonmann.acejudgeserver.runner.UniversalSolutionRunner
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.nio.file.Path
import kotlin.system.measureTimeMillis

@Service
class CorrectnessJudge(@Autowired private val universalCompiler: UniversalCompiler, private val universalSolutionRunner: UniversalSolutionRunner) : Judge {

    override fun verdict(solution: Path, language : LanguageEnum, judgeInput : Path, judgeOutput : Path): VerdictDTO {

        val compilerResult : CompilerResult = universalCompiler.compile(solution, language)

        lateinit var solutionRunResult : SolutionRunnerResult

        val runtime = measureTimeMillis {
            solutionRunResult = universalSolutionRunner.run(compilerResult.executable, judgeInput)
        } / 1000.0

        val expectedOutput : String = judgeOutput.toFile().readText(Charsets.UTF_8)

        if(solutionRunResult.output.contentEquals(expectedOutput)) {
            return VerdictDTO(ProblemSubmissionCorrectnessStatusEnum.CORRECT_ANSWER, runtime)
        }

        return VerdictDTO(ProblemSubmissionCorrectnessStatusEnum.WRONG_ANSWER, runtime)
    }
}