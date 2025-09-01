package com.bookish.bar.dtos.mappers;
import com.bookish.bar.dtos.dtos.BookDto;
import com.bookish.bar.models.Book;

public class BookMapper {

    public static Book toEntity(BookDto dto) {
        Book book = new Book();
        book.setOpenLibraryId(dto.getOpenLibraryId());
        book.setTitle(dto.getTitle());
        book.setAuthors(dto.getAuthors());
        book.setPublishedYear(dto.getPublishedYear());
        book.setCoverUrl(dto.getCoverUrl());
        return book;
    }

    public static BookDto toDto(Book book) {
        BookDto dto = new BookDto();
        dto.setOpenLibraryId(book.getOpenLibraryId());
        dto.setTitle(book.getTitle());
        dto.setAuthors(book.getAuthors());
        dto.setPublishedYear(book.getPublishedYear());
        dto.setCoverUrl(book.getCoverUrl());
        return dto;
    }
}
