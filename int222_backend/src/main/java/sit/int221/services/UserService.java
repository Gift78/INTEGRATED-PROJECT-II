package sit.int221.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.entities.User;
import sit.int221.exceptions.UserNotFoundException;
import sit.int221.repositories.UserRepository;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUser(){
        Sort sortRoleAndUsername = Sort.by(Sort.Direction.ASC, "role", "username");
        return userRepository.findAll(sortRoleAndUsername);
    }

    public User getUser(Integer userId){
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }

    public User createUser(User newUser){
        User existName = userRepository.findByName(newUser.getName());
        User existUserName = userRepository.findByUsername(newUser.getUsername());
        User existEmail = userRepository.findByEmail(newUser.getEmail());
        if (existName != null || existUserName != null || existEmail != null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"The required must be unique");
        }else {
            newUser.setUsername(newUser.getUsername());
            newUser.setName(newUser.getName());
            newUser.setEmail(newUser.getEmail());
            return userRepository.saveAndFlush(newUser);
        }
    }

    public User updateUser(Integer userId,User userDetail){
        User existUser = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        existUser.setName(userDetail.getName());
        existUser.setUsername(userDetail.getUsername());
        existUser.setEmail(userDetail.getEmail());
        existUser.setRole(userDetail.getRole());
        return userRepository.saveAndFlush(existUser);
    }
}
