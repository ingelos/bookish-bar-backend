package com.bookish.bar.dtos.dtos;

import com.bookish.bar.models.Book;
import com.bookish.bar.models.BookListItem;
import jakarta.persistence.ElementCollection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDto{
    private String openLibraryId;
    private String title;
    private Integer publishedYear;
    private String coverUrl;

    private List<AuthorDto> authors = new ArrayList<>();

    // nullable, only used for fetching book details
    private String description;
    private String firstSentence;

    public static BookDto fromEntity(Book book) {
        BookDto dto = new BookDto();
        dto.setOpenLibraryId(book.getOpenLibraryId());
        dto.setTitle(book.getTitle());
        dto.setPublishedYear(book.getPublishedYear());
        dto.setCoverUrl(book.getCoverUrl());
        dto.setDescription(book.getDescription());
        dto.setFirstSentence(book.getFirstSentence());

        dto.setAuthors(
                book.getAuthors().stream()
                        .map(AuthorDto::fromEntity)
                        .toList()
        );

        return dto;
    }


}
