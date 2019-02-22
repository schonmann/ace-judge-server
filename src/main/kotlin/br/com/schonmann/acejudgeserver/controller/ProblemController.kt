package br.com.schonmann.acejudgeserver.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/problem")
class ProblemController {

    @GetMapping(value = ["/list"])
    fun list() {

    }

    @GetMapping(value = ["/getById"])
    fun getById() {

    }
}