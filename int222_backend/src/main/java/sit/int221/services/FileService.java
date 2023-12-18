package sit.int221.services;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sit.int221.entities.Announcement;
import sit.int221.entities.File;
import sit.int221.exceptions.AnnouncementNotFoundException;
import sit.int221.repositories.AnnouncementRepository;
import sit.int221.repositories.FileRepository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileService {
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private AnnouncementRepository announcementRepository;
    @Value("${file.upload-dir}")
    private String uploadDir;

    @PostConstruct
    public void init() {
        Path fileStorageLocation = Path.of(uploadDir).toAbsolutePath().normalize();

        try {
            Files.createDirectories(fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public void storeFiles(MultipartFile[] files, Integer announcementId) {
        for (MultipartFile file : files) {
            String originalFileName = file.getOriginalFilename();
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            String uniqueFileName = UUID.randomUUID() + fileExtension;

            try {
                if (uniqueFileName.contains("..")) {
                    throw new RuntimeException("Sorry! Filename contains invalid path sequence " + uniqueFileName);
                }
                Path targetLocation = Path.of(uploadDir).resolve(uniqueFileName);
                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

                File fileEntity = new File();
                Announcement announcement = announcementRepository.findById(announcementId).orElseThrow(() -> new AnnouncementNotFoundException(announcementId));
                fileEntity.setOriginalFileName(originalFileName);
                fileEntity.setUniqueFileName(uniqueFileName);
                fileEntity.setAnnouncement(announcement);
                fileRepository.save(fileEntity);
            } catch (Exception ex) {
                throw new RuntimeException("Could not store file " + uniqueFileName + ". Please try again!", ex);
            }
        }
    }

    public void deleteFile(String fileName) {
        try {
            Path filePath = Path.of(uploadDir).resolve(fileName).normalize();
            Files.delete(filePath);
        } catch (Exception ex) {
            throw new RuntimeException("File not found " + fileName, ex);
        }
    }

    public Resource loadFile(String originalFileName, Integer announcementId) {
        File file = fileRepository.findByAnnouncementIdAndOriginalFileName(announcementId, originalFileName);
        String uniqueFileName = file.getUniqueFileName();

        try {
            Path filePath = Path.of(uploadDir).resolve(uniqueFileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new RuntimeException("File not found " + uniqueFileName);
            }
        } catch (Exception ex) {
            throw new RuntimeException("File not found " + uniqueFileName, ex);
        }
    }
}