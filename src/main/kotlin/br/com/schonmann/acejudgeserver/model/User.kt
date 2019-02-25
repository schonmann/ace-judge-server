package br.com.schonmann.acejudgeserver.model

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.stream.Collectors
import javax.persistence.*
import kotlin.jvm.Transient

/**
 * The User class.
 */

@Entity
class User(
    @Id @GeneratedValue(strategy = GenerationType.AUTO) val id: Long,
    private val username: String,
    private val password: String,
    val name: String,
    val address: String,
    val pictureUrl: String,
    @Column(name = "enabled", nullable = false) val enabled: Boolean = true,
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER) val roles: List<UserRole>,
    @Transient private val authorities: MutableCollection<out GrantedAuthority>?

) : UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return roles.stream().map { role ->
            SimpleGrantedAuthority(role.toString())
        }.collect(Collectors.toList())
    }

    override fun isEnabled(): Boolean = enabled
    override fun isCredentialsNonExpired(): Boolean = enabled
    override fun isAccountNonExpired(): Boolean = enabled
    override fun isAccountNonLocked(): Boolean = enabled
    override fun getUsername(): String = username
    override fun getPassword(): String = password
}