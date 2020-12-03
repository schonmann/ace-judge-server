package br.com.schonmann.acejudgeserver.model

import br.com.schonmann.acejudgeserver.enums.LanguageEnum
import br.com.schonmann.acejudgeserver.enums.ProblemSimulationStatusEnum
import br.com.schonmann.acejudgeserver.enums.ProblemSubmissionAnalysisStatus
import br.com.schonmann.acejudgeserver.enums.ProblemSubmissionCorrectnessStatusEnum
import java.util.*
import javax.persistence.*

@Entity
class ProblemSubmission(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id : Long = 0,

        @ManyToOne(optional = false)
        val problem : Problem,

        @ManyToOne(optional = false)
        val user : User,

        @Enumerated(value = EnumType.STRING)
        @Column(nullable = false)
        var correctnessStatus : ProblemSubmissionCorrectnessStatusEnum,

        @Enumerated(value = EnumType.STRING)
        @Column(nullable = false)
        var analysisStatus : ProblemSubmissionAnalysisStatus,

        @ManyToOne(optional = true)
        val parentContest : Contest?,

        @Enumerated(value = EnumType.STRING)
        @Column(nullable = false)
        val language : LanguageEnum,

        var submitDate: Date,

        var runtime: Double?

)