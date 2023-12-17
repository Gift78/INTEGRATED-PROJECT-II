package sit.int221.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sit.int221.services.FileService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

@RestController
@RequestMapping("/api/file")
@CrossOrigin(origins= {"http://localhost:5173", "https://intproj22.sit.kmutt.ac.th", "http://127.0.0.1:5173"})
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @GetMapping("/{fileName:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String fileName) {
        Resource file = fileService.loadFile(fileName);
        String mimeType;
        String originalFileName = fileService.getOriginalFileName(fileName);
        try {
            mimeType = Files.probeContentType(Paths.get(file.getURI()));
            if (mimeType == null) {
                mimeType = "application/octet-stream";
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not determine file type.", e);
        }
        MediaType mediaType = MediaType.parseMediaType(mimeType);

        HttpHeaders headers = new HttpHeaders();
        if (mimeType.equals("application/pdf") || mimeType.contains("image")) {
            // Display PDF, PNG, JPEG, and GIF files in the browser
            headers.setContentDisposition(ContentDisposition.builder("inline").filename(originalFileName).build());
        } else {
            // Download all other file types
            headers.setContentDisposition(ContentDisposition.builder("attachment").filename(originalFileName).build());
        }

        return ResponseEntity.ok().headers(headers).contentType(mediaType).body(file);
    }

    @PostMapping("")
    public ResponseEntity<?> createAnnouncementWithFiles(@RequestPart("announcementId") Integer announcementId, @RequestPart("files") MultipartFile[] files) {
        fileService.storeFiles(files, announcementId);
        return ResponseEntity.ok(Map.of("message", "You successfully uploaded " + files.length + " files and created a new announcement!"));
    }

    @PutMapping("")
    public ResponseEntity<?> updateAnnouncementWithFiles(@RequestPart("announcementId") Integer announcementId, @RequestPart("files") MultipartFile[] files) {
        fileService.storeFiles(files, announcementId);
        return ResponseEntity.ok(Map.of("message", "You successfully uploaded " + files.length + " files and updated the announcement!"));
    }

    @DeleteMapping("/{uniqueFilename:.+}")
    public ResponseEntity<?> deleteFile(@PathVariable String uniqueFilename) {
        fileService.deleteFile(uniqueFilename);
        return ResponseEntity.ok(Map.of("message", "You successfully deleted the file!"));
    }
}