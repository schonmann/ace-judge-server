package br.com.schonmann.acejudgeserver.controller

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails

interface BaseController {
    fun getRequestUser() : UserDetails {
        return SecurityContextHolder.getContext().authentication.principal as UserDetails
    }
}