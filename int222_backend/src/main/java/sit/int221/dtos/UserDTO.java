package sit.int221.dtos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sit.int221.utils.UserRole;

import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Integer id;
    @Size(max = 45)
    @NotNull
    private String username;
    @Size(max = 100)
    @NotNull
    private String name;
    @Size(max = 150)
    @NotNull
    private String email;
    @Enumerated(EnumType.STRING)
    private UserRole role;
    private ZonedDateTime createdOn;
    private ZonedDateTime updatedOn;
}