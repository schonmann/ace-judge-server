package br.com.schonmann.acejudgeserver.judge

import br.com.schonmann.acejudgeserver.compiler.UniversalCompiler
import br.com.schonmann.acejudgeserver.enums.LanguageEnum
import br.com.schonmann.acejudgeserver.enums.ProblemSubmissionStatusEnum
import br.com.schonmann.acejudgeserver.model.CompilerResult
import br.com.schonmann.acejudgeserver.model.SolutionRunnerResult
import br.com.schonmann.acejudgeserver.runner.UniversalSolutionRunner
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.nio.file.Path

@Service
class CorrectnessJudge(@Autowired private val universalCompiler: UniversalCompiler, private val universalSolutionRunner: UniversalSolutionRunner) : Judge {

    override fun verdict(solution: Path, language : LanguageEnum, judgeInput : Path, judgeOutput : Path): ProblemSubmissionStatusEnum {

        val compilerResult : CompilerResult = universalCompiler.compile(solution, language)

        val solutionRunResult : SolutionRunnerResult = universalSolutionRunner.run(compilerResult.executable, judgeInput)
        val expectedOutput : String = judgeOutput.toFile().readText(Charsets.UTF_8)

        println("Output programa: ")
        println(solutionRunResult.output)
        println("Output esperado:")
        println(expectedOutput)


        if(solutionRunResult.output.contentEquals(expectedOutput)) {
            return ProblemSubmissionStatusEnum.CORRECT_ANSWER
        }

        return ProblemSubmissionStatusEnum.WRONG_ANSWER
    }
}