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
        String trimmedName = newUser.getName().replaceAll("\\s+","");
        String trimmedUserName = newUser.getUsername().replaceAll("\\s+","");
        String trimmedEmail = newUser.getEmail().replaceAll("\\s+","");
        if (trimmedName.isEmpty() || trimmedUserName.isEmpty() || trimmedEmail.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"The required cannot be empty");
        }

        User existName = userRepository.findByName(newUser.getName());
        User existUserName = userRepository.findByUsername(newUser.getUsername());
        User existEmail = userRepository.findByEmail(newUser.getEmail());
        if (existName != null || existUserName != null || existEmail != null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"The required must be unique");
        }else {
            newUser.setUsername(trimmedUserName);
            newUser.setName(trimmedName);
            newUser.setEmail(trimmedEmail);
            return userRepository.saveAndFlush(newUser);
        }
    }
}
