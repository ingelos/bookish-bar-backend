package com.bookish.bar.dtos.inputDtos;

import jakarta.validation.constraints.NotNull;

public class ProfilePictureInputDto {

    @NotNull
    private String profilePictureUrl;

}
