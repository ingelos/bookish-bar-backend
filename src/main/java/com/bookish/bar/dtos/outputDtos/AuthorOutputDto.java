package com.bookish.bar.dtos.outputDtos;

import lombok.Data;


@Data
public class AuthorOutputDto {

    private String id;
    private String name;
    private String bio;
    private String birthDate;
    private String photoUrl;

}
