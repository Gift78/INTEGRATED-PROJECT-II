package sit.int221.dtos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sit.int221.utils.UserRole;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Integer id;
    private String username;
    private String name;
    private String email;
    @Enumerated(EnumType.STRING)
    private UserRole role;
}