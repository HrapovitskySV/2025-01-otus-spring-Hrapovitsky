package ru.otus.hw.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.otus.hw.services.CustomUserDetailsService;


//@Component
public class CustomAuthenticationProvider {//implements AuthenticationProvider {
    @Autowired
    private CustomUserDetailsService userDetailsService;

    private PasswordEncoder passwordEncoder;

    CustomAuthenticationProvider(){
        this.passwordEncoder = new BCryptPasswordEncoder(12);
    }

    //@Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        final String name = authentication.getName();
        final String password = authentication.getCredentials().toString();
        UserDetails userDetails = userDetailsService.loadUserByUsername(name);
        var hashPassword = passwordEncoder.encode(password);
        if (userDetails.getPassword().equals(hashPassword)) {
            throw new BadCredentialsException("Bad Credentials");
        }

        return new UsernamePasswordAuthenticationToken(name, password, userDetails.getAuthorities());
    }

    //@Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}