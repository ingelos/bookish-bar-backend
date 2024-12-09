package com.bookish.bar.dtos.mappers;

import com.bookish.bar.dtos.inputDtos.AuthorInputDto;
import com.bookish.bar.dtos.outputDtos.AuthorOutputDto;
import com.bookish.bar.models.Author;




public class AuthorMapper {

    public static Author toModel (AuthorInputDto inputDto) {
        Author author = new Author();
        author.setId(inputDto.getId());
        author.setName(inputDto.getName());
        author.setBio(inputDto.getBio());
        author.setBirthDate(inputDto.getBirthDate());
        author.setPhotos(inputDto.getPhotos());
        return author;
    }

    public static AuthorOutputDto toDto(Author author) {
        AuthorOutputDto dto = new AuthorOutputDto();
        dto.setId(author.getId());
        dto.setName(author.getName());
        dto.setBio(author.getBio());
        dto.setBirthDate(author.getBirthDate());

        if (author.getPhotos() != null && !author.getPhotos().isEmpty()) {
            dto.setPhotoUrl("https://covers.openlibrary.org/b/id/" + author.getPhotos().getFirst() + "-L.jpg");
        } else {
            dto.setPhotoUrl("");
        }

        return dto;
    }


}
