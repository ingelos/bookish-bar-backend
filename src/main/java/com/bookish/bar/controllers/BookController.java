package com.bookish.bar.controllers;

import com.bookish.bar.client.OpenLibraryClient;
import com.bookish.bar.dtos.dtos.BookDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/books")
public class BookController {

    private final OpenLibraryClient openLibraryClient;

    public BookController(OpenLibraryClient openLibraryClient) {
        this.openLibraryClient = openLibraryClient;
    }

    @GetMapping("/{openLibraryId}")
    public ResponseEntity<BookDto> getBook(@PathVariable String openLibraryId) {
        BookDto book = openLibraryClient.fetchBook(openLibraryId);
        return ResponseEntity.ok(book);
    }

}
