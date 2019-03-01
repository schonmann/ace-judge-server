package br.com.schonmann.acejudgeserver.model

import br.com.schonmann.acejudgeserver.enum.ProblemVisibilityEnum
import javax.persistence.*


@Entity
class Problem(
        @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
        var id: Long = 0,

        var name: String,

        var description: String,

        var visibility: ProblemVisibilityEnum,

        @ManyToOne
        var category: ProblemCategory
)