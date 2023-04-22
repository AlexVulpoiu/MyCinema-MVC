package com.unibuc.fmi.mycinemamvc.services;

import com.unibuc.fmi.mycinemamvc.domain.Authority;
import com.unibuc.fmi.mycinemamvc.domain.User;
import com.unibuc.fmi.mycinemamvc.dto.RegisterDto;
import com.unibuc.fmi.mycinemamvc.exceptions.BadRequestException;
import com.unibuc.fmi.mycinemamvc.exceptions.UniqueConstraintException;
import com.unibuc.fmi.mycinemamvc.repositories.AuthorityRepository;
import com.unibuc.fmi.mycinemamvc.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void register(RegisterDto registerDto) {
        Optional<User> optionalUser = userRepository.findByUsername(registerDto.getUsername());
        if(optionalUser.isPresent()) {
            throw new UniqueConstraintException("This username is already used!");
        }

        Optional<Authority> optionalGuestRole = authorityRepository.findByRole("ROLE_GUEST");
        if(optionalGuestRole.isEmpty()) {
            throw new BadRequestException("The guest role doesn't exist!");
        }

        User user = User.builder()
                .username(registerDto.getUsername())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .authorities(Set.of(optionalGuestRole.get()))
                .build();
        userRepository.save(user);
    }
}
