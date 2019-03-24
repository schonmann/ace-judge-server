package br.com.schonmann.acejudgeserver.storage

import org.springframework.core.io.Resource
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Path
import java.util.stream.Stream


interface StorageService {

    fun init()

    @Throws(StorageException::class)
    fun store(file: MultipartFile, renameTo: String? = null, ignoreExtension : Boolean = false)

    fun loadAll(): Stream<Path>

    fun load(filename: String): Path

    fun loadAsResource(filename: String): Resource

    fun deleteAll()

}