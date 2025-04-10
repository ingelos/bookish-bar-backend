package com.bookish.bar.dtos.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateUserDetailsDto {


    private String username;
    @Email(message = "Invalid email")
    private String email;
    @NotBlank(message= "Current password is required")
    private String currentPassword;

}
