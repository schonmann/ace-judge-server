package br.com.schonmann.acejudgeserver.model

import javax.persistence.*


@Entity
class Problem(
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE) var id: Long,
    var name: String,
    var description: String,
    @ManyToOne var category: ProblemCategory
)