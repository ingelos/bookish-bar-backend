package com.bookish.bar.controllers;

import com.bookish.bar.dtos.dtos.AddBookRequest;
import com.bookish.bar.dtos.dtos.BookListDto;
import com.bookish.bar.dtos.dtos.BookListItemDto;
import com.bookish.bar.dtos.inputDtos.ProfileInputDto;
import com.bookish.bar.dtos.outputDtos.ProfileOutputDto;
import com.bookish.bar.enums.BookListType;
import com.bookish.bar.models.BookList;
import com.bookish.bar.models.User;
import com.bookish.bar.services.BookListService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/lists")
public class BookListController {

    private final BookListService bookListService;

    public BookListController(BookListService bookListService) {
        this.bookListService = bookListService;
    }




    @PostMapping("/{type}/books")
    public ResponseEntity<Void> addBookToList(
            @AuthenticationPrincipal User user,
            @PathVariable BookListType type,
            @RequestBody AddBookRequest request) {

        bookListService.addBookToList(user, request.getOpenLibraryId(), type, request.getRating());
        return ResponseEntity.ok().build();

    }

    @GetMapping("/{type}")
    public ResponseEntity<BookListDto> getUserList(
            @AuthenticationPrincipal User user,
            @PathVariable BookListType type) {

        BookListDto dto = bookListService.getUserList(user, type);
        return ResponseEntity.ok(dto);
    }


    @DeleteMapping("/{type}/books/{itemId}")
    public ResponseEntity<Void> removeBookFromList(
            @PathVariable Long userId,
            @PathVariable BookListType type,
            @PathVariable Long itemId) {

        bookListService.removeBookFromList(userId, type, itemId);
        return ResponseEntity.noContent().build();
    }



}
