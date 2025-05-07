package store.management.store_system.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "staff_account")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;

    private String password;

    private String department;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Role roles;

    private final String timeOfCreation = String.valueOf(LocalDateTime.now());
}
