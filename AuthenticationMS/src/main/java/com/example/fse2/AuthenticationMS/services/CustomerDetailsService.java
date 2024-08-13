package com.example.fse2.AuthenticationMS.services;

import com.example.fse2.AuthenticationMS.models.AppUser;
import com.example.fse2.AuthenticationMS.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomerDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser appUser = userRepository.findByEmail(email);

        if (appUser != null){

            UserDetails userDetails = User.withUsername(appUser.getEmail())
                    .password(appUser.getPassword())
                    .authorities(new SimpleGrantedAuthority("ROLE_"+appUser.getRole()))
                    .build();
            return userDetails;
        }
        return null;
    }
}
