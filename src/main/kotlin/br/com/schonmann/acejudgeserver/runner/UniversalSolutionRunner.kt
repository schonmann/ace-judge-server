package br.com.schonmann.acejudgeserver.runner

import br.com.schonmann.acejudgeserver.cmd.runCommand
import br.com.schonmann.acejudgeserver.model.CompilerResult
import br.com.schonmann.acejudgeserver.model.SolutionRunnerResult
import org.springframework.stereotype.Component
import java.lang.Exception
import java.nio.file.Path
import kotlin.system.measureTimeMillis

@Component
class UniversalSolutionRunner : SolutionRunner {
    override fun run(executable : Path, input: Path) : SolutionRunnerResult {

        val cmd = "./${executable.toFile().name}"
        val output = cmd.runCommand(workingDir = executable.parent.toFile(), inputFile = input.toFile()) ?: ""

        return SolutionRunnerResult(output, 10, 1024)
    }
}