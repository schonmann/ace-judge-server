package br.com.schonmann.acejudgeserver.model

import java.nio.file.Path

class CompilerResult(
        var executable: Path,
        var metadata: SolutionMeta
)