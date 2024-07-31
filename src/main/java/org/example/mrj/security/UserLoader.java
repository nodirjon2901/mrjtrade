package org.example.mrj.security;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor

@Component
public class UserLoader implements CommandLineRunner
{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception
    {
        if (!userRepository.existsByUsername("nasiniemsin"))
        {
            User user = new User();
            user.setUsername("nasiniemsin");
            user.setPassword(passwordEncoder.encode("parolDlyaMujikov"));
            userRepository.save(user);
        }
    }
}
