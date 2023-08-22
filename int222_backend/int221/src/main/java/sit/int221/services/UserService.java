package sit.int221.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sit.int221.entities.User;
import sit.int221.repositories.UserRepositttory;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepositttory userRepositttory;

    public List<User> getAllUser(){
        Sort sortRoleAndUsername = Sort.by(Sort.Direction.ASC, "role", "username");
        return userRepositttory.findAll(sortRoleAndUsername);
    }
}
