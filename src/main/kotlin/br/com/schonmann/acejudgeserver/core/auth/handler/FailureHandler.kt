package br.com.schonmann.acejudgeserver.core.auth.handler

import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler
import org.springframework.stereotype.Component

@Component
class FailureHandler : SimpleUrlAuthenticationFailureHandler() {

}