package com.bookish.bar.dtos.dtos;

import com.bookish.bar.dtos.mappers.BookMapper;
import com.bookish.bar.models.Author;
import com.bookish.bar.models.Book;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorDto {
    private String id;
    private String name;

    // optional for fetching author details
    private String bio;
    private String birthDate;
    private String deathDate;

    private List<BookDto> books;

    public AuthorDto(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public static AuthorDto fromEntity(Author author) {
        AuthorDto dto = new AuthorDto();
        dto.setId(author.getId());
        dto.setName(author.getName());
        dto.setBio(author.getBio());
        dto.setBirthDate(author.getBirthDate());
        dto.setDeathDate(author.getDeathDate());

        if (author.getBooks() != null) {
            dto.setBooks(
                    author.getBooks().stream().map(BookMapper::toDto).toList()
            );
        }

        return dto;
    }

}
