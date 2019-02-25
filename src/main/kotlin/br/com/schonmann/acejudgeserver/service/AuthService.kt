package br.com.schonmann.acejudgeserver.service

import br.com.schonmann.acejudgeserver.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(@Autowired private val userService: UserService) : UserDetailsService {

    @Throws(UsernameNotFoundException::class)
    @Transactional
    override fun loadUserByUsername(username: String?): UserDetails {
        if (username == null) {
            throw UsernameNotFoundException("username is null")
        }
        val user : User? = userService.findByUsername(username)
        return user ?: throw UsernameNotFoundException("user not found in database")
    }
}