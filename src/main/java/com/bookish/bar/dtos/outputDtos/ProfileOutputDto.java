package com.bookish.bar.dtos.outputDtos;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProfileOutputDto {

    private Long id;
    private String firstname;
    private String lastname;
    private String about;
//    private String profilePictureUrl;

    private String username;


}
