package br.com.schonmann.acejudgeserver

import br.com.schonmann.acejudgeserver.mock.MockService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AceJudgeServerApplication

fun main(args: Array<String>) {
	runApplication<AceJudgeServerApplication>(*args)
}