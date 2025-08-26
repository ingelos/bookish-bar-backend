package com.bookish.bar.dtos.outputDtos;

import lombok.Data;

@Data
public class ProfileOutputDto {

    private Long id;
    private String username;
    private String firstname;
    private String lastname;
    private String about;
    private String profilePhotoUrl;


}
