package br.com.schonmann.acejudgeserver.runner

import br.com.schonmann.acejudgeserver.model.CompilerResult
import br.com.schonmann.acejudgeserver.model.SolutionRunnerResult
import java.nio.file.Path

interface SolutionRunner {
    fun run(executable : Path, input : Path) : SolutionRunnerResult
}