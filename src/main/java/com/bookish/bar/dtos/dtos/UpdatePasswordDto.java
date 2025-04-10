package com.bookish.bar.dtos.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdatePasswordDto {

    @NotBlank(message= "Current password is required")
    private String currentPassword;
    @Size(min=8, message = "Password needs to be at least 8 characters long")
    private String newPassword;
    @NotBlank( message = "Confirm password is required")
    private String confirmPassword;

}
