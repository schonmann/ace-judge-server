package br.com.schonmann.acejudgeserver.storage

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class StorageProperties {

    /**
     * Folder location for storing files
     */
    @Value("\${ace.storage.submissions-path}")
    var location = "/home/xuma/upload-dir"

}