package br.com.schonmann.acejudgeserver.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.lang.RuntimeException

@ResponseStatus(HttpStatus.FORBIDDEN)
class ForbiddenException(msg : String) : RuntimeException(msg) {
}