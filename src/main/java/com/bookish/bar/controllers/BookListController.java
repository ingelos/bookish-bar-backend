package com.bookish.bar.controllers;

import com.bookish.bar.client.OpenLibraryClient;
import com.bookish.bar.dtos.dtos.*;
import com.bookish.bar.dtos.inputDtos.ProfileInputDto;
import com.bookish.bar.dtos.outputDtos.ProfileOutputDto;
import com.bookish.bar.enums.BookListType;
import com.bookish.bar.models.Book;
import com.bookish.bar.models.BookList;
import com.bookish.bar.models.BookListItem;
import com.bookish.bar.models.User;
import com.bookish.bar.services.BookListService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/lists")
public class BookListController {

    private final BookListService bookListService;
    private final OpenLibraryClient openLibraryClient;

    public BookListController(BookListService bookListService, OpenLibraryClient openLibraryClient) {
        this.bookListService = bookListService;
        this.openLibraryClient = openLibraryClient;
    }




    @GetMapping("/search")
    public ResponseEntity<List<BookDto>> searchBooks(@RequestParam String q) {
        List<BookDto> results = openLibraryClient.searchBooks(q);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/{type}")
    public ResponseEntity<BookListDto> getUserList(
            @AuthenticationPrincipal User user,
            @PathVariable BookListType type) {

        BookListDto dto = bookListService.getUserBookList(user, type);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{type}/items")
    public ResponseEntity<List<BookListItemDto>> getUserBookListItems(
            @AuthenticationPrincipal User user,
            @PathVariable BookListType type) {
        List<BookListItemDto> items = bookListService.getUserBookListItems(user, type);
        return ResponseEntity.ok(items);
    }

    @PostMapping("/{type}/books")
    public ResponseEntity<Void> addBookToList(
            @AuthenticationPrincipal User user,
            @PathVariable BookListType type,
            @RequestBody AddBookRequest request) {

        bookListService.addBookToList(user, request.getOpenLibraryId(), type, request.getRating());
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/{type}/{openLibraryId}")
    public ResponseEntity<Void> removeBookFromList(
            @AuthenticationPrincipal User user,
            @PathVariable BookListType type,
            @PathVariable String openLibraryId) {

        bookListService.removeBookFromList(user, type, openLibraryId);
        return ResponseEntity.noContent().build();
    }


    @PutMapping("/read/{openLibraryId}/rating")
    public ResponseEntity<BookListItemDto> updateRating(
            @AuthenticationPrincipal User user,
            @PathVariable BookListType type,
            @PathVariable String openLibraryId,
            @RequestBody UpdateRatingRequest request
    ) {
        BookListItem updatedItem = bookListService.updateRating(user, type, openLibraryId, request.rating());
        return ResponseEntity.ok(BookListItemDto.fromEntity(updatedItem));
    }




}
