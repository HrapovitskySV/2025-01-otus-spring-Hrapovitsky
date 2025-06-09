package ru.otus.hw.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.otus.hw.repositories.CustomUserRepository;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private CustomUserRepository customUserRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        var oCustomUser= customUserRepository.findByUsername(userName);
        if (oCustomUser.isEmpty()) {
            throw new UsernameNotFoundException("Unknown user: "+userName);
        }
        return oCustomUser.get();
    }
}

