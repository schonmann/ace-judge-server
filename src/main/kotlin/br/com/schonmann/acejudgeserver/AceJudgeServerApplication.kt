package br.com.schonmann.acejudgeserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AceJudgeServerApplication

fun main(args: Array<String>) {
	runApplication<AceJudgeServerApplication>(*args)
}