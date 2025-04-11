package com.bookish.bar.dtos.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangeUsernameDto {

    @NotBlank(message = "Username is required")
    private String username;
    @NotBlank(message = "Password is required")
    private String password;

}
