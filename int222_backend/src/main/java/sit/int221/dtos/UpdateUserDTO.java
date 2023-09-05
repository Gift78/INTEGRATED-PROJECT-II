package sit.int221.dtos;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sit.int221.utils.UserRole;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDTO {
    @NotNull
    @NotBlank(message = "must not empty")
    @Size(max = 45, message = "This field size must between 1 to 45")
    private String username;

    @NotNull @NotBlank(message = "must not empty")
    @Size(max = 100, message = "This field size must between 1 to 45")
    private String name;

    @NotNull @NotBlank(message = "must not empty")
    @Size(max = 150, message = "This field size must between 1 to 45")
    @Email(message = "must be a well-formed email address", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    private String email;

    @Enumerated(EnumType.STRING)
    private UserRole role;
}
