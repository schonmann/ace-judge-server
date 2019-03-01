package br.com.schonmann.acejudgeserver.model

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.stream.Collectors
import javax.persistence.*

/**
 * The User class.
 */

@Entity
class User(
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long = 0,

        private val username: String,
        private val password: String,
        val name: String,
        val address: String,
        val pictureUrl: String = "",

        @Column(name = "enabled", nullable = false)
        val enabled: Boolean = true,

        @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(
            name = "users_roles",
            joinColumns = [JoinColumn(
                    name = "user_id", referencedColumnName = "id")],
            inverseJoinColumns = [JoinColumn(
                    name = "role_id", referencedColumnName = "id")])
        val roles: Collection<Role> = ArrayList(),

        @Transient
        private val authorities: MutableCollection<out GrantedAuthority>? = ArrayList()

) : UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return roles.flatMap{ r ->
            r.privileges.map { p -> SimpleGrantedAuthority(p.privilege.name) }
        }.toMutableList()
    }

    override fun isEnabled(): Boolean = enabled
    override fun isCredentialsNonExpired(): Boolean = enabled
    override fun isAccountNonExpired(): Boolean = enabled
    override fun isAccountNonLocked(): Boolean = enabled
    override fun getUsername(): String = username
    override fun getPassword(): String = password
}