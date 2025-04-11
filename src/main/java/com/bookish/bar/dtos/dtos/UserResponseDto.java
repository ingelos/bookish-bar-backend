package com.bookish.bar.dtos.dtos;

import com.bookish.bar.models.Authority;
import com.bookish.bar.models.User;
import lombok.Data;

import java.util.List;

@Data
public class UserResponseDto {

    private Long id;
    private String username;
    private String email;
    private String token;

    private List<String> authorities;

    public UserResponseDto(User user, String token) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.token = token;
        this.authorities = user.getAuthorities().stream()
                .map(Authority::getAuthority)
                .toList();
    }
}
