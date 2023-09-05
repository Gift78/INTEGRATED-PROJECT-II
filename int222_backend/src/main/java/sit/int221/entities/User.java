package sit.int221.entities;

import jakarta.persistence.*;
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
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;
    private String password;
    private String name;
    private String email;
    @Enumerated(EnumType.STRING)
    private UserRole role;
    @Column(name = "createdOn", insertable = false, updatable = false)
    private ZonedDateTime createdOn;
    @Column(name = "updatedOn", insertable = false, updatable = false)
    private ZonedDateTime updatedOn;
}
