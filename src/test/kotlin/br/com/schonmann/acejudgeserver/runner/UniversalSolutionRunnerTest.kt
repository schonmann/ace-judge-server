package br.com.schonmann.acejudgeserver.runner

import br.com.schonmann.acejudgeserver.cmd.runCommand
import org.junit.Test

import org.junit.Assert.*
import java.io.File
import java.nio.file.Path

class UniversalSolutionRunnerTest {

    @Test
    fun run() {
        val p = File("/home/xuma/upload-dir/submissions/67/a.out")
        println("g++ ./a.out".runCommand(workingDir = p.parentFile))
    }
}