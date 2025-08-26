package com.bookish.bar.dtos.dtos;

import com.bookish.bar.models.Book;
import com.bookish.bar.models.BookListItem;
import lombok.Data;

@Data
public class BookDto{
    private String openLibraryId;
    private String title;
    private String author;
    private String coverUrl;

    public static BookDto fromEntity(BookListItem item) {
        BookDto dto = new BookDto();
        dto.openLibraryId = item.getBook().getOpenLibraryId();
        dto.title = item.getBook().getTitle();
        dto.author = item.getBook().getAuthor();
        dto.coverUrl = item.getBook().getCoverUrl();
        return dto;
    }



}
