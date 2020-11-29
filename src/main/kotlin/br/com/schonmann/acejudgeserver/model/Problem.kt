package br.com.schonmann.acejudgeserver.model

import br.com.schonmann.acejudgeserver.enums.LanguageEnum
import br.com.schonmann.acejudgeserver.enums.ProblemSimulationStatusEnum
import br.com.schonmann.acejudgeserver.enums.ProblemDifficultyEnum
import br.com.schonmann.acejudgeserver.enums.ProblemVisibilityEnum
import com.querydsl.core.annotations.QueryEntity
import javax.persistence.*

@Entity
@QueryEntity
class Problem(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0,

        @Column(nullable = false)
        var name: String,

        @Column(nullable = false)
        var complexities: String,

        @Enumerated(value = EnumType.STRING)
        @Column(nullable = false)
        var simulationStatus : ProblemSimulationStatusEnum,

        @Column(nullable = false)
        var bigoNotation: String,

        @Column(nullable = false)
        var score: Long,

        @Column(nullable = false, length = 16777215)
        @Lob
        var problemDescription: String, // hypertext!

        @Column(nullable = false, length = 16777215)
        @Lob
        var constraintDescription: String, // hypertext!

        @Column(nullable = false, length = 16777215)
        var exampleInput: String,

        @Column(nullable = false, length = 16777215)
        var exampleOutput: String,

        @ManyToMany(mappedBy = "problems")
        var contests : List<Contest> = ArrayList(),

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        var visibility: ProblemVisibilityEnum,

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        var judgeAnswerKeyProgramLanguage: LanguageEnum,

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        var inputGeneratorLanguage: LanguageEnum,

        @ManyToOne(optional = false)
        var category: ProblemCategory,

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        var difficulty : ProblemDifficultyEnum
)