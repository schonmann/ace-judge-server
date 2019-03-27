package br.com.schonmann.acejudgeserver.compiler

import br.com.schonmann.acejudgeserver.enums.LanguageEnum
import br.com.schonmann.acejudgeserver.model.CompilerResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.nio.file.Path

@Component
class UniversalCompiler(@Autowired private val cppCompiler: CppCompiler) {
    fun compile(path: Path, language: LanguageEnum, vararg args: String): CompilerResult {
        return when(language) {
            LanguageEnum.CPP -> cppCompiler.compile(path, *args)
            else -> cppCompiler.compile(path, *args)
        }
    }
}