package com.bookish.bar.dtos.inputDtos;

import lombok.Data;

import java.util.List;

@Data
public class AuthorInputDto {

    private String id;
    private String name;
    private String bio;
    private String birthDate;
    private List<Integer> photos;

}
