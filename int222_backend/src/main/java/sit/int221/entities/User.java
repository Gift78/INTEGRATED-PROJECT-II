package sit.int221.entities;

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
import sit.int221.validators.ValidUnique;

import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ValidUnique
    @NotNull
    @NotBlank(message = "must not empty")
    @Size(max = 45, message = "This field size must between 1 to 45")
    private String username;
    @ValidUnique
    @NotNull
    @NotBlank(message = "must not empty")
    @Size(max = 100, message = "This field size must between 1 to 45")
    private String name;
    @ValidUnique
    @Email(message = "must be a well-formed email address", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    @NotNull
    @NotBlank(message = "must not empty")
    @Size(max = 150, message = "This field size must between 1 to 45")
    private String email;
    @Enumerated(EnumType.STRING)
    private UserRole role;
    @Column(name = "createdOn", insertable = false, updatable = false)
    private ZonedDateTime createdOn;
    @Column(name = "updatedOn", insertable = false, updatable = false)
    private ZonedDateTime updatedOn;
}
