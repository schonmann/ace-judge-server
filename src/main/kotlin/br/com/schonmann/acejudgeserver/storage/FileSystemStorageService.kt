package br.com.schonmann.acejudgeserver.storage

import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Path
import java.io.IOException
import org.springframework.util.FileSystemUtils
import java.net.MalformedURLException
import org.springframework.core.io.UrlResource
import java.nio.file.StandardCopyOption
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.util.StringUtils
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Stream


@Service
class FileSystemStorageService constructor(@Autowired private val storageProperties: StorageProperties) : StorageService {

    private val rootLocation: Path = Paths.get(storageProperties.location)

    @Throws(StorageException::class)
    override fun store(file: MultipartFile, renameTo : String?, ignoreExtension : Boolean) {
        val extension = if (ignoreExtension) "" else ".${StringUtils.getFilenameExtension(file.originalFilename!!)}"
        val filename = if (renameTo != null) "$renameTo$extension" else StringUtils.cleanPath(file.originalFilename!!)
        try {
            if (file.isEmpty) {
                throw StorageException("Failed to store empty file $filename")
            }
            if (filename.contains("..")) {
                // This is a security check
                throw StorageException(
                        "Cannot store file with relative path outside current directory $filename")
            }
            file.inputStream.use { inputStream ->
                Files.createDirectories(this.rootLocation.resolve(filename).parent)
                Files.copy(inputStream, this.rootLocation.resolve(filename),
                        StandardCopyOption.REPLACE_EXISTING)
            }
        } catch (e: IOException) {
            throw StorageException("Failed to store file $filename", e)
        }

    }

    override fun loadAll(): Stream<Path> {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter({ path -> !path.equals(this.rootLocation) })
                    .map(({ this.rootLocation.relativize(it) }))
        } catch (e: IOException) {
            throw StorageException("Failed to read stored files", e)
        }

    }

    override fun load(filename: String): Path {
        return rootLocation.resolve(filename)
    }

    override fun loadAsResource(filename: String): Resource {
        try {
            val file = load(filename)
            val resource = UrlResource(file.toUri())
            return if (resource.exists() || resource.isReadable) {
                resource
            } else {
                throw StorageFileNotFoundException(
                        "Could not read file: $filename")

            }
        } catch (e: MalformedURLException) {
            throw StorageFileNotFoundException("Could not read file: $filename", e)
        }

    }

    override fun deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile())
    }

    override fun init() {
        try {
            Files.createDirectories(rootLocation)
        } catch (e: IOException) {
            throw StorageException("Could not initialize storage", e)
        }
    }
}