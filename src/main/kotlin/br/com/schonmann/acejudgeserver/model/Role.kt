package br.com.schonmann.acejudgeserver.model

import br.com.schonmann.acejudgeserver.enums.RoleEnum
import javax.persistence.*

@Entity
class Role(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0,

        @Enumerated(value = EnumType.STRING)
        @Column(nullable = false, unique = true, length = 25)
        var role: RoleEnum,

        @ManyToMany(mappedBy = "roles")
        var users : Collection<User> = ArrayList(),

        @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(
                name = "roles_privileges",
                joinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")],
                inverseJoinColumns = [JoinColumn(name = "privilege_id", referencedColumnName = "id")]
        )
        val privileges: Collection<Privilege>
)