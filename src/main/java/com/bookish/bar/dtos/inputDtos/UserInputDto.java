package com.bookish.bar.dtos.inputDtos;

import com.bookish.bar.dtos.dtos.AuthorityDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;


@Data
public class UserInputDto {

    @NotBlank(message = "Username is required")
    private String username;
    @Email(message = "Invalid email")
    private String email;
    @Size(min=8, message = "Password needs to be at least 8 characters long")
    private String password;
    @Size(min=1, max=64)
    private String firstname;
    @Size(min=2, max=64)
    private String lastname;
    private String bio;

    private Set<AuthorityDto> authorities = new HashSet<>();


}
