package br.com.schonmann.acejudgeserver.model

import javax.persistence.*

@Entity
class ProblemCategory(
        @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
        var id: Long = 0,
        
        var name: String,

        @Lob
        var image: String
)