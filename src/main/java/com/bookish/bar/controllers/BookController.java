package com.bookish.bar.controllers;

import com.bookish.bar.client.OpenLibraryClient;
import com.bookish.bar.dtos.dtos.BookDto;
import com.bookish.bar.dtos.mappers.BookMapper;
import com.bookish.bar.models.Book;
import com.bookish.bar.services.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/{openLibraryId}")
    public ResponseEntity<BookDto> getBook(@PathVariable String openLibraryId) {
        BookDto dto = bookService.fetchBookFromApi(openLibraryId);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/db/{openLibraryId}")
    public ResponseEntity<BookDto> getBookFromDb(@PathVariable String openLibraryId) {
        Book book = bookService.getOrCreateBook(openLibraryId);
        return ResponseEntity.ok(BookMapper.toDto(book));
    }


}
