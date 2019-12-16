package ru.itis.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.itis.chat.security.role.Role;
import ru.itis.chat.security.state.State;
import ru.itis.chat.models.User;
import ru.itis.chat.repositories.UsersRepository;

//@Configuration
public class CommandLineRunnerImpl implements CommandLineRunner {

    @Autowired
    UsersRepository usersRepository;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... strings) throws Exception {

        String hashPassword = passwordEncoder.encode("ggwp");
        User user = User.builder()
                .firstName("Alisher")
                .lastName("Alisherov")
                .hashPassword(hashPassword)
                .login("ali")
                .role(Role.USER)
                .state(State.ACTIVE)
                .build();

        User user1 = User.builder()
                .firstName("Dias")
                .lastName("Diasov")
                .hashPassword( passwordEncoder.encode("ggwp"))
                .login("dias")
                .role(Role.USER)
                .state(State.ACTIVE)
                .build();

        usersRepository.save(user);
        usersRepository.save(user1);
    }
}
