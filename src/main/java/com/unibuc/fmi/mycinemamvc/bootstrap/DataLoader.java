package com.unibuc.fmi.mycinemamvc.bootstrap;

import com.unibuc.fmi.mycinemamvc.domain.Authority;
import com.unibuc.fmi.mycinemamvc.domain.User;
import com.unibuc.fmi.mycinemamvc.repositories.AuthorityRepository;
import com.unibuc.fmi.mycinemamvc.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Profile("mysql")
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private void loadUserData() {
        if(userRepository.count() == 0) {
            Authority adminRole = authorityRepository.save(Authority.builder().role("ROLE_ADMIN").build());
            Authority guestRole = authorityRepository.save(Authority.builder().role("ROLE_GUEST").build());

            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("12345"))
                    .authorities(Set.of(adminRole))
                    .build();

            User guest = User.builder()
                    .username("guest")
                    .password(passwordEncoder.encode("12345"))
                    .authorities(Set.of(guestRole))
                    .build();

            userRepository.save(admin);
            userRepository.save(guest);
        }
    }

    @Override
    public void run(String... args) {
        loadUserData();
    }
}
