package sit.int221.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import sit.int221.dtos.*;
import sit.int221.entities.Announcement;
import sit.int221.services.AnnouncementService;
import sit.int221.utils.ListMapper;

import java.util.List;

@RestController
@RequestMapping("/api/announcements")
@CrossOrigin(origins = {"http://localhost:5173", "http://intproj22.sit.kmutt.ac.th"})
public class AnnouncementController {
    @Autowired
    private AnnouncementService announcementService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ListMapper listMapper;

    @GetMapping("")
    public List<Announcement> getAllAnnouncement(@RequestParam(required = false) String mode) {
        return announcementService.getAllAnnouncement(mode);
    }

    @GetMapping("/{id}")
    public AnnouncementDTO getAnnouncement(@PathVariable Integer id, @RequestParam(required = false) Boolean count) {
        Announcement announcement = announcementService.getAnnouncement(id, count);
        return modelMapper.map(announcement, AnnouncementDTO.class);
    }

    @PostMapping("")
    public CreateAndUpdateAnnouncementResponseDTO createAnnouncement(@Valid @RequestBody CreateAndUpdateAnnouncementDTO newAnnouncement, BindingResult bindingResult) throws MethodArgumentNotValidException {
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException((MethodParameter) null, bindingResult);
        }

        Announcement announcement = announcementService.createAnnouncement(newAnnouncement);
        return modelMapper.map(announcement, CreateAndUpdateAnnouncementResponseDTO.class);
    }

    @PutMapping("/{id}")
    public CreateAndUpdateAnnouncementResponseDTO updateAnnouncement(@PathVariable Integer id,@Valid @RequestBody CreateAndUpdateAnnouncementDTO newAnnouncement, BindingResult bindingResult) throws MethodArgumentNotValidException {
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException((MethodParameter) null, bindingResult);
        }

        Announcement announcement = announcementService.updateAnnouncement(id, newAnnouncement);
        return modelMapper.map(announcement, CreateAndUpdateAnnouncementResponseDTO.class);
    }

    @DeleteMapping("/{id}")
    public void deleteAnnouncement(@PathVariable Integer id) {
        announcementService.deleteAnnouncement(id);
    }

    @GetMapping("/pages")
    public PageDTO<AnnouncementPageDTO> getAnnouncementsByPage(@RequestParam(defaultValue = "admin") String mode,
                                                               @RequestParam(defaultValue = "0") Integer page,
                                                               @RequestParam(defaultValue = "5") Integer size,
                                                               @RequestParam(defaultValue = "0") Integer category) {
        return listMapper.toPageDTO(announcementService.getAnnouncementsByPage(mode, page, size, category), AnnouncementPageDTO.class, modelMapper);
    }
}
