package br.com.schonmann.acejudgeserver.model

import javax.persistence.*

@Entity
class ProblemCategory(
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE) var id: Long,
    var name: String,
    @Lob var image: String
)