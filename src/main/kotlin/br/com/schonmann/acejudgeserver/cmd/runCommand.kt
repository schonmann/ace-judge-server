package br.com.schonmann.acejudgeserver.cmd

import br.com.schonmann.acejudgeserver.exception.ExecutionException
import br.com.schonmann.acejudgeserver.exception.TimeLimitException
import java.io.File
import java.util.concurrent.TimeUnit

fun String.runCommand(workingDir: File, time: Long = 3, timeUnit: TimeUnit = TimeUnit.SECONDS, inputFile : File? = null): String? {
    val parts = this.split("\\s".toRegex())
    val builder = ProcessBuilder(*parts.toTypedArray())
            .directory(workingDir)
            .redirectErrorStream(true)
    val proc = if (inputFile != null) builder.redirectInput(inputFile).start() else builder.start()
    val executed = proc.waitFor(time, timeUnit)
    if (!executed) {
        proc.destroyForcibly()
        throw TimeLimitException("process timed out, code = ${proc.exitValue()}")
    }
    val output = proc.inputStream.bufferedReader().readText()
    if (proc.exitValue() != 0) {
        proc.destroyForcibly()
        throw ExecutionException("error executing program, code = ${proc.exitValue()}")
    }
    proc.destroyForcibly()
    return output
}