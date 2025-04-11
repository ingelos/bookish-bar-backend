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

    private Set<AuthorityDto> authorities = new HashSet<>();


}
