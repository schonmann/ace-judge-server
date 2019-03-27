package br.com.schonmann.acejudgeserver.compiler

import br.com.schonmann.acejudgeserver.enums.LanguageEnum
import br.com.schonmann.acejudgeserver.model.CompilerResult
import java.nio.file.Path

interface Compiler {
    fun compile(path : Path, vararg args : String) : CompilerResult
}