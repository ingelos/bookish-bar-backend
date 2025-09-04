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
import com.bookish.bar.repositories.BookListItemRepository;
import com.bookish.bar.services.BookListService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/bookLists")
public class BookListController {

    private final BookListService bookListService;
    private final BookListItemRepository bookListItemRepository;

    public record UserBookCounts(int all, int read, int wantToRead) {}

    public BookListController(BookListService bookListService, BookListItemRepository bookListItemRepository) {
        this.bookListService = bookListService;
        this.bookListItemRepository = bookListItemRepository;
    }


    @GetMapping("/{type}")
    public ResponseEntity<BookListDto> getUserBooks(
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


    @GetMapping("/counts")
    public UserBookCounts getBookCounts(@AuthenticationPrincipal User user) {
        int read = bookListItemRepository.countByUserAndBookList_Type(user, BookListType.READ);
        int want = bookListItemRepository.countByUserAndBookList_Type(user, BookListType.WANT_TO_READ);
        int all = read + want;
        return new UserBookCounts(all, read, want);
    }


}
