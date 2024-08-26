package selapang.restful.tafakkur.com.controller

import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

@RestController
@RequestMapping("/files")
class FileDownloadController {

    private val fileStorageLocation: Path = Paths.get("uploads").toAbsolutePath().normalize()

    @GetMapping("/images/**")
    fun downloadFile(request: HttpServletRequest): ResponseEntity<Resource> {
        // Dapatkan path relatif dari request URI
        val requestURI = request.requestURI
        val fullPath = requestURI.substring(requestURI.indexOf("/images/") + "/images/".length)

        // Bentuk path lengkap file berdasarkan direktori root penyimpanan
        val filePath = fileStorageLocation.resolve(fullPath).normalize()

        val resource: Resource = UrlResource(filePath.toUri())

        if (resource.exists() && resource.isReadable) {
            val contentType = Files.probeContentType(filePath) ?: "application/octet-stream"
            return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.filename + "\"")
                .body(resource)
        } else {
            return ResponseEntity.notFound().build()
        }
    }
}