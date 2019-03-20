package br.com.schonmann.acejudgeserver.dto

data class SignupDTO (
    val username : String,
    val password : String,
    val name : String,
    val address : String,
    val privilegeKey : String
)