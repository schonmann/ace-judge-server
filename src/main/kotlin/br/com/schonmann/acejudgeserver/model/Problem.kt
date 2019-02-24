package br.com.schonmann.acejudgeserver.model

import br.com.schonmann.acejudgeserver.model.enum.ProblemVisibilityEnum
import javax.persistence.*


@Entity
class Problem(
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE) var id: Long,
    var name: String,
    var description: String,
    var visibility: ProblemVisibilityEnum,
    @ManyToOne var category: ProblemCategory
)