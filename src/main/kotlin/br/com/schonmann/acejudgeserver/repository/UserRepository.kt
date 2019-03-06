package br.com.schonmann.acejudgeserver.repository

import br.com.schonmann.acejudgeserver.model.User
import org.springframework.data.domain.Page
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {

    fun findByUsername(username: String) : User?
}