package com.bookish.bar.dtos.dtos;

import com.bookish.bar.enums.BookListType;
import com.bookish.bar.models.BookList;
import lombok.Data;

import java.util.List;

@Data
public class BookListDto {
    private Long id;
    private String listName;
    private BookListType type;
    private List<BookDto> books;

    public static BookListDto fromEntity(BookList bookList) {
        BookListDto dto = new BookListDto();
        dto.id = bookList.getId();
        dto.listName = bookList.getListName();
        dto.type = bookList.getType();
        dto.books = bookList.getBooks().stream()
                .map(BookDto::fromEntity)
                .toList();
        return dto;
    }


}
