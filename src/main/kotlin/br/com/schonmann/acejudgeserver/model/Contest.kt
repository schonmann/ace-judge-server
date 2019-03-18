package br.com.schonmann.acejudgeserver.model

import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import java.util.*
import javax.persistence.*
import kotlin.collections.ArrayList


@Entity
class Contest(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0,

        var name: String,

        var password: String,

        @Lob
        @Column(nullable = false, length = 16777215)
        var description: String,

        var startDate: Date,

        var endDate: Date,

        @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(
                name = "contests_users",
                joinColumns = [JoinColumn(name = "contest_id", referencedColumnName = "id")],
                inverseJoinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")])
        @Fetch(value = FetchMode.SUBSELECT)
        var participants: List<User> = ArrayList(),

        @ManyToOne
        var admin: User
)