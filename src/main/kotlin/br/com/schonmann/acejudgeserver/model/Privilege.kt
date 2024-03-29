package br.com.schonmann.acejudgeserver.model

import br.com.schonmann.acejudgeserver.enums.PrivilegeEnum
import javax.persistence.*

@Entity
class Privilege(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0,

        @Enumerated(value = EnumType.STRING) @Column(nullable = false)
        var privilege: PrivilegeEnum,

        @ManyToMany(mappedBy = "privileges")
        var roles : Collection<Role> = ArrayList()
) {}