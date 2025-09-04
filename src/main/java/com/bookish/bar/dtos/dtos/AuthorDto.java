package com.bookish.bar.dtos.dtos;

import com.bookish.bar.models.Author;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorDto {
    private String id;
    private String name;
    private String bio;
    private String birthDate;
    private String deathDate;

    public static AuthorDto fromEntity(Author author) {
        AuthorDto dto = new AuthorDto();
        dto.setId(author.getId());
        dto.setName(author.getName());
        dto.setBio(author.getBio());
        dto.setBirthDate(author.getBirthDate());
        dto.setDeathDate(author.getDeathDate());
        return dto;
    }

}
