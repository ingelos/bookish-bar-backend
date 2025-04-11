package com.bookish.bar.dtos.outputDtos;

import com.bookish.bar.models.Authority;
import com.bookish.bar.models.BookList;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Data
public class UserOutputDto {

    private Long id;
    private String username;
    private String email;

    private List<String> authorities;

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = new ArrayList<>();
        authorities.forEach(authority -> this.authorities.add(authority.getAuthority()));
    }

}
