package sit.int221.controllers;

import jakarta.persistence.EntityManager;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sit.int221.dtos.UserDTO;
import sit.int221.entities.User;
import sit.int221.services.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = {"http://localhost:5173", "http://intproj22.sit.kmutt.ac.th"})
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public List<User> getAllUser(){
        return userService.getAllUser();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Integer id){
        return userService.getUser(id);
    }

    @PostMapping("")
    public UserDTO createUser(@RequestBody User newUser){
        userService.createUser(newUser);
        return modelMapper.map(newUser,UserDTO.class);
    }
}
