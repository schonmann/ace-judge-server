package br.com.schonmann.acejudgeserver.model

import javax.persistence.*

@Entity
class User(
    @Id @GeneratedValue(strategy = GenerationType.AUTO) var id: Long,
    var username: String,
    var name: String,
    var address: String
)