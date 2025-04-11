package com.bookish.bar.dtos.inputDtos;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProfileInputDto {

    @Size(max  = 64)
    private String firstname;
    @Size(max  = 64)
    private String lastname;
    @Size(max  = 500)
    private String about;

}
