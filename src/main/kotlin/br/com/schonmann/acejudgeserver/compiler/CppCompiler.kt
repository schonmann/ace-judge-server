package br.com.schonmann.acejudgeserver.compiler

import br.com.schonmann.acejudgeserver.cmd.runCommand
import br.com.schonmann.acejudgeserver.enums.LanguageEnum
import br.com.schonmann.acejudgeserver.model.CompilerResult
import br.com.schonmann.acejudgeserver.model.SolutionMeta
import org.springframework.stereotype.Component
import java.nio.file.Path

@Component
class CppCompiler : Compiler {
    override fun compile(path: Path, vararg args: String): CompilerResult {

        val cmd = "g++ ${path.toFile().name} -lm -lcrypt -O2 -pipe"

        cmd.runCommand(path.parent.toFile())

        return CompilerResult(executable = path.parent.resolve("a.out"), metadata = SolutionMeta(language = LanguageEnum.CPP))
    }
}