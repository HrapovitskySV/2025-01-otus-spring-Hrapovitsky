package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.otus.hw.repositories.CustomUserRepository;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final CustomUserRepository customUserRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        var oCustomUser = customUserRepository.findByUsername(userName);
        if (oCustomUser.isEmpty()) {
            throw new UsernameNotFoundException("Unknown user: " + userName);
        }
        return oCustomUser.get();
    }
}

