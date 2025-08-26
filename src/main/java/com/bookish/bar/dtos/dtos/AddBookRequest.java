package com.bookish.bar.dtos.dtos;

import lombok.Data;

@Data
public class AddBookRequest {

    private String openLibraryId;
    private Integer rating;

}
