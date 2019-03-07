package br.com.schonmann.acejudgeserver.model

import br.com.schonmann.acejudgeserver.enums.ProblemCategoryEnum
import javax.persistence.*

@Entity
class ProblemCategory(
        @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
        var id: Long = 0,

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        var category: ProblemCategoryEnum,

        @Lob
        var image: String
)