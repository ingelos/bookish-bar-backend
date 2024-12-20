package com.bookish.bar.services;

import com.bookish.bar.models.Authority;
import com.bookish.bar.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class CustomUserDetailService {

    private final UserService userService;
    public CustomUserDetailService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userService.getUser(username);

        String password = user.getPassword();
        Set<Authority> authorities = user.getAuthorities();
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        authorities.forEach((authority) -> grantedAuthorities.add(new SimpleGrantedAuthority(authority.getAuthority())));

        return new org.springframework.security.core.userdetails.User(username, password, grantedAuthorities);
    }

}
