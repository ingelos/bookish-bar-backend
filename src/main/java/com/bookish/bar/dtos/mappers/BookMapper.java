package com.bookish.bar.dtos.mappers;
import com.bookish.bar.dtos.dtos.BookDto;
import com.bookish.bar.models.Author;
import com.bookish.bar.models.Book;
import com.bookish.bar.services.AuthorService;
import org.hibernate.sql.ast.tree.expression.Collation;

import java.util.Set;
import java.util.stream.Collectors;

public class BookMapper {

    public static Book toEntity(BookDto dto, AuthorService authorService) {
        Book book = new Book();
        book.setOpenLibraryId(dto.getOpenLibraryId());
        book.setTitle(dto.getTitle());

        book.setPublishedYear(dto.getPublishedYear());
        book.setCoverUrl(dto.getCoverUrl());

        Set<Author> authors = dto.getAuthors().stream()
                        .map(a -> authorService.getOrCreateAuthorEntity(a))
                                .collect(Collectors.toSet());
        book.setAuthors(authors);

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
