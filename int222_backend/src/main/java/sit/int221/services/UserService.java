package sit.int221.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;
import sit.int221.dtos.CreateUserDTO;
import sit.int221.dtos.UpdateUserDTO;
import sit.int221.entities.User;
import sit.int221.exceptions.UserNotFoundException;
import sit.int221.repositories.UserRepository;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;

    public List<User> getAllUser(){
        Sort sortRoleAndUsername = Sort.by(Sort.Direction.ASC, "role", "username");
        return userRepository.findAll(sortRoleAndUsername);
    }

    public User getUser(Integer userId){
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }

    public User createUser(CreateUserDTO user){
        String trimmedName = user.getName().trim();
        String trimmedUsername = user.getUsername().trim();
        String trimmedEmail = user.getEmail().trim();
        String trimmedPassword = user.getPassword().trim();

        Argon2PasswordEncoder argon2PasswordEncoder = new Argon2PasswordEncoder(16, 32, 1, 4096, 3);
        String hashedPassword = argon2PasswordEncoder.encode(trimmedPassword);

        User newUser = modelMapper.map(user, User.class);
        newUser.setUsername(trimmedUsername);
        newUser.setName(trimmedName);
        newUser.setEmail(trimmedEmail);
        newUser.setPassword(hashedPassword);
        return userRepository.saveAndFlush(newUser);
    }

    public User updateUser(Integer userId, UpdateUserDTO userDetail){
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
