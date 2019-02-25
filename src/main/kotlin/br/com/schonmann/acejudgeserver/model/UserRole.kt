package br.com.schonmann.acejudgeserver.model

import br.com.schonmann.acejudgeserver.enum.RoleEnum
import javax.persistence.*

@Entity
data class UserRole(
        @Id @GeneratedValue(strategy = GenerationType.AUTO) var id: Long,
        @ManyToOne var user : User,
        @Enumerated(value = EnumType.STRING) @Column(nullable = false) var role : RoleEnum
)