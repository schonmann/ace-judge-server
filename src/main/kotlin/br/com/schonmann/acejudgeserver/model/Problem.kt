package br.com.schonmann.acejudgeserver.model

import br.com.schonmann.acejudgeserver.enums.ProblemDifficultyEnum
import br.com.schonmann.acejudgeserver.enums.ProblemVisibilityEnum
import com.querydsl.core.annotations.QueryEntity
import javax.persistence.*

@Entity
@QueryEntity
class Problem(
        @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
        var id: Long = 0,

        @Column(nullable = false)
        var name: String,

        @Column(nullable = false)
        var description: String,

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        var visibility: ProblemVisibilityEnum,

        @ManyToOne(optional = false)
        var category: ProblemCategory,

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        var difficulty : ProblemDifficultyEnum
)