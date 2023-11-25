package sit.int221.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.int221.entities.File;

public interface FileRepository extends JpaRepository<File, Integer> {
    File findByAnnouncementIdAndOriginalFileName(int id, String originalFileName);
}
