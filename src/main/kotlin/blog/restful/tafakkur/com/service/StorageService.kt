package blog.restful.tafakkur.com.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class StorageService(
    @Value("\${file.upload-dir}")
    private val uploadDir: String
) {
    init {
        val path = Paths.get(uploadDir)
        if (!Files.exists(path)) {
            Files.createDirectories(path)
        }
    }

    fun storeFile(file: MultipartFile, subfolder: String, replace: Boolean = false): String {
        val oriName = file.originalFilename ?: throw RuntimeException("Invalid file")
        val fileName: String = if (replace) {
            oriName
        } else {
            val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))
            "${timestamp}_${oriName}"
        }

        // Gabungkan subfolder dengan direktori utama
        val subfolderPath = Paths.get(uploadDir, subfolder).toAbsolutePath().normalize()

        // Buat subfolder jika belum ada
        if (!Files.exists(subfolderPath)) {
            Files.createDirectories(subfolderPath)
        }

        val targetLocation = subfolderPath.resolve(fileName)
        try {

            Files.deleteIfExists(targetLocation)
            Files.copy(file.inputStream, targetLocation)

        } catch (ex: IOException) {
            throw RuntimeException("Could not store file, try again!", ex)
        }
        return fileName
    }
}