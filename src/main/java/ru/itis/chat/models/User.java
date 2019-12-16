package ru.itis.chat.models;

import lombok.*;
import ru.itis.chat.security.role.Role;
import ru.itis.chat.security.state.State;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;

    private String login;
    private String hashPassword;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    @Enumerated(value = EnumType.STRING)
    private State state;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Token> tokens;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Message> messages;
}
