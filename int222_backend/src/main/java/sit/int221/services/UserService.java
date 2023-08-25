package sit.int221.services;

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
        String trimmedName = newUser.getName().trim();
        String trimmedUsername = newUser.getUsername().trim();
        String trimmedEmail = newUser.getEmail().trim();

        User existName = userRepository.findByName(newUser.getName());
        User existUserName = userRepository.findByUsername(newUser.getUsername());
        User existEmail = userRepository.findByEmail(newUser.getEmail());
        if (existName != null || existUserName != null || existEmail != null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"The required must be unique");
        } else {
            newUser.setUsername(trimmedUsername);
            newUser.setName(trimmedName);
            newUser.setEmail(trimmedEmail);
            return userRepository.saveAndFlush(newUser);
        }
    }

    public User updateUser(Integer userId,User userDetail){
        String trimmedName = userDetail.getName().trim();
        String trimmedUsername = userDetail.getUsername().trim();
        String trimmedEmail = userDetail.getEmail().trim();

        User existUser = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        existUser.setName(trimmedName);
        existUser.setUsername(trimmedUsername);
        existUser.setEmail(trimmedEmail);
        existUser.setRole(userDetail.getRole());
        return userRepository.saveAndFlush(existUser);
    }

    public void deleteUser(Integer id) {
        User existUser = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        userRepository.delete(existUser);
    }
}
