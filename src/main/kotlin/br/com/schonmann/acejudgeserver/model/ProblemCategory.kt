package br.com.schonmann.acejudgeserver.model

import br.com.schonmann.acejudgeserver.enums.ProblemCategoryEnum
import javax.persistence.*

@Entity
class ProblemCategory(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0,

        @Enumerated(EnumType.STRING)
        @Column(nullable = false, unique = true, length = 25)
        var category: ProblemCategoryEnum,

        @Lob
        var image: String
)