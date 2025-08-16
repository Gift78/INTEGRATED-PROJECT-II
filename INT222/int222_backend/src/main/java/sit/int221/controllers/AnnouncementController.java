package sit.int221.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import sit.int221.dtos.*;
import sit.int221.entities.Announcement;
import sit.int221.services.AnnouncementService;
import sit.int221.services.SubscriptionService;
import sit.int221.utils.ListMapper;

import java.util.List;

@RestController
@RequestMapping("/api/announcements")
@CrossOrigin(origins = {"http://localhost:5173", "https://intproj22.sit.kmutt.ac.th", "http://127.0.0.1:5173"})
@RequiredArgsConstructor
public class AnnouncementController {
    private final AnnouncementService announcementService;
    private final SubscriptionService subscriptionService;
    private final ModelMapper modelMapper;
    private final ListMapper listMapper;

    @GetMapping("")
    public List<AnnouncementDTO> getAllAnnouncement(@RequestParam(required = false) String mode,
                                                    @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        return listMapper.mapList(announcementService.getAllAnnouncement(mode, authorizationHeader), AnnouncementDTO.class, modelMapper);
    }

    @GetMapping("/{id}")
    public AnnouncementDTO getAnnouncement(@PathVariable Integer id,
                                           @RequestParam(required = false) Boolean count,
                                           @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        Announcement announcement = announcementService.getAnnouncement(id, count, authorizationHeader);
        return modelMapper.map(announcement, AnnouncementDTO.class);
    }

    @PostMapping("")
    public CreateAndUpdateAnnouncementResponseDTO createAnnouncement(@Valid @RequestBody CreateAndUpdateAnnouncementDTO newAnnouncement,
                                                                     @RequestHeader(value = "Authorization") String authorizationHeader,
                                                                     BindingResult bindingResult) throws MethodArgumentNotValidException {
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException((MethodParameter) null, bindingResult);
        }

        Announcement announcement = announcementService.createAnnouncement(newAnnouncement, authorizationHeader);
        subscriptionService.sendAnnouncementEmail(announcement.getCategory().getId(), announcement);

        return modelMapper.map(announcement, CreateAndUpdateAnnouncementResponseDTO.class);
    }

    @PutMapping("/{id}")
    public CreateAndUpdateAnnouncementResponseDTO updateAnnouncement(@PathVariable Integer id,
                                                                     @Valid @RequestBody CreateAndUpdateAnnouncementDTO newAnnouncement,
                                                                     @RequestHeader(value = "Authorization") String authorizationHeader,
                                                                     BindingResult bindingResult) throws MethodArgumentNotValidException {
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException((MethodParameter) null, bindingResult);
        }

        Announcement announcement = announcementService.updateAnnouncement(id, newAnnouncement, authorizationHeader);
        subscriptionService.sendAnnouncementEmail(announcement.getCategory().getId(), announcement);
        return modelMapper.map(announcement, CreateAndUpdateAnnouncementResponseDTO.class);
    }

    @DeleteMapping("/{id}")
    public void deleteAnnouncement(@PathVariable Integer id, @RequestHeader(value = "Authorization") String authorizationHeader) {
        announcementService.deleteAnnouncement(id, authorizationHeader);
    }

    @GetMapping("/pages")
    public PageDTO<AnnouncementPageDTO> getAnnouncementsByPage(@RequestParam(defaultValue = "admin") String mode,
                                                               @RequestParam(defaultValue = "0") Integer page,
                                                               @RequestParam(defaultValue = "5") Integer size,
                                                               @RequestParam(defaultValue = "0") Integer category) {
        return listMapper.toPageDTO(announcementService.getAnnouncementsByPage(mode, page, size, category), AnnouncementPageDTO.class, modelMapper);
    }
}
