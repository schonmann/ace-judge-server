package br.com.schonmann.acejudgeserver.dto

import java.util.*

data class CeleryMessageDTO (
        var lang : String = "py",
        var task : String,
        var id : String = UUID.randomUUID().toString(),
        var root_id : String = UUID.randomUUID().toString(),
        var parent_id : String = UUID.randomUUID().toString(),
        var group : String = UUID.randomUUID().toString(),
        var args : List<String> = ArrayList()
)