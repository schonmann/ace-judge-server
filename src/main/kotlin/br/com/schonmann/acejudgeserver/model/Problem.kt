package br.com.schonmann.acejudgeserver.model

import org.springframework.data.annotation.Id
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.ManyToOne


@Entity
class Problem(
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE) var id: Long,
    var name: String,
    var description: String,
    @ManyToOne
    var category: ProblemCategory
)