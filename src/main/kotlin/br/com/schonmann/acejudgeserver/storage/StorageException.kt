package br.com.schonmann.acejudgeserver.storage

open class StorageException : RuntimeException {

    constructor(message: String) : super(message) {}

    constructor(message: String, cause: Throwable) : super(message, cause) {}
}