package com.bookish.bar.dtos.dtos;

import com.bookish.bar.models.Book;
import com.bookish.bar.models.BookListItem;
import jakarta.persistence.ElementCollection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDto{
    private String openLibraryId;
    private String title;
    private List<String> authors;
    private Integer publishedYear;
    private String coverUrl;

    private String description;
    private String firstSentence;

    public static BookDto fromEntity(BookListItem item) {
        BookDto dto = new BookDto();
        dto.openLibraryId = item.getBook().getOpenLibraryId();
        dto.title = item.getBook().getTitle();
        dto.authors = item.getBook().getAuthors();
        dto.publishedYear = item.getBook().getPublishedYear();
        dto.coverUrl = item.getBook().getCoverUrl();
        dto.description = item.getBook().getDescription();
        dto.firstSentence = item.getBook().getFirstSentence();
        return dto;
    }



}
