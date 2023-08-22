package sit.int221.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sit.int221.dtos.AnnouncementDTO;
import sit.int221.dtos.CreateAndUpdateAnnouncementDTO;
import sit.int221.dtos.PageDTO;
import sit.int221.entities.Announcement;
import sit.int221.entities.Category;
import sit.int221.exceptions.AnnouncementNotFoundException;
import sit.int221.repositories.AnnouncementRepository;
import sit.int221.utils.AnnouncementDisplay;
import sit.int221.utils.ListMapper;

import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class AnnouncementService {
    @Autowired
    private AnnouncementRepository announcementRepository;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ModelMapper modelMapper;

    public List<Announcement> getAllAnnouncement(String mode) {
        if (Objects.equals(mode, "active")) {
            List<Announcement> activeAnnouncements = announcementRepository.getActiveAnnouncements(AnnouncementDisplay.Y, ZonedDateTime.now());
            activeAnnouncements.sort(Comparator.comparing(Announcement::getId).reversed());
            return activeAnnouncements;
        } else if (Objects.equals(mode, "close")){
            List<Announcement> closeAnnouncements = announcementRepository.findAllByCloseDateIsNotNullAndAnnouncementDisplayEqualsAndCloseDateBefore(AnnouncementDisplay.Y, ZonedDateTime.now());
            closeAnnouncements.sort(Comparator.comparing(Announcement::getId).reversed());
            return closeAnnouncements;
        } else {
            Sort sort = Sort.by(Sort.Direction.DESC, "id");
            return announcementRepository.findAll(sort);
        }
    }

    public Announcement getAnnouncement(Integer announcementId, Boolean viewCount) {
        if (viewCount != null && viewCount) {
            Announcement announcement = announcementRepository.findById(announcementId).orElseThrow(() -> new AnnouncementNotFoundException(announcementId));
            announcement.setViewCount(announcement.getViewCount() + 1);
            return announcementRepository.saveAndFlush(announcement);
        }

        return announcementRepository.findById(announcementId).orElseThrow(() -> new AnnouncementNotFoundException(announcementId));
    }

    public Announcement createAnnouncement(CreateAndUpdateAnnouncementDTO announcement) {
        Category category = categoryService.getCategory(announcement.getCategoryId());
        Announcement newAnnouncement = modelMapper.map(announcement, Announcement.class);
        newAnnouncement.setCategory(category);
        return announcementRepository.saveAndFlush(newAnnouncement);
    }

    public Announcement updateAnnouncement(Integer announcementId, CreateAndUpdateAnnouncementDTO announcement) {
        Announcement oldAnnouncement = announcementRepository.findById(announcementId).orElseThrow(() -> new AnnouncementNotFoundException(announcementId));
        Category category = categoryService.getCategory(announcement.getCategoryId());
        oldAnnouncement.setAnnouncementTitle(announcement.getAnnouncementTitle());
        oldAnnouncement.setAnnouncementDescription(announcement.getAnnouncementDescription());
        oldAnnouncement.setCategory(category);
        oldAnnouncement.setPublishDate(announcement.getPublishDate());
        oldAnnouncement.setCloseDate(announcement.getCloseDate());
        oldAnnouncement.setAnnouncementDisplay(announcement.getAnnouncementDisplay());
        return announcementRepository.saveAndFlush(oldAnnouncement);
    }

    public void deleteAnnouncement(Integer announcementId) {
        Announcement announcement = announcementRepository.findById(announcementId).orElseThrow(() -> new AnnouncementNotFoundException(announcementId));
        announcementRepository.delete(announcement);
    }

    public Page<Announcement> getAnnouncementsByPage(String mode, Integer page, Integer size, Integer categoryId) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);
        ZonedDateTime currentTime = ZonedDateTime.now();

        if (categoryId == 0) {
            if (Objects.equals(mode, "active")) {
                return announcementRepository.getActiveAnnouncementsPage(AnnouncementDisplay.Y, currentTime, pageable);
            } else if (Objects.equals(mode, "close")) {
                return announcementRepository.findAllByCloseDateIsNotNullAndAnnouncementDisplayEqualsAndCloseDateBefore(AnnouncementDisplay.Y, currentTime, pageable);
            } else {
                return announcementRepository.findAll(pageable);
            }
        } else {
            if (Objects.equals(mode, "active")) {
                return announcementRepository.getActiveAnnouncementsPageWithCategoryId(AnnouncementDisplay.Y, currentTime, categoryId, pageable);
            } else if (Objects.equals(mode, "close")) {
                return announcementRepository.findAllByCloseDateIsNotNullAndAnnouncementDisplayEqualsAndCloseDateBeforeAndCategory_Id(AnnouncementDisplay.Y, currentTime, categoryId, pageable);
            } else {
                return announcementRepository.findAllByCategory_Id(categoryId, pageable);
            }
        }
    }
}
