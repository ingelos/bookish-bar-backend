package com.bookish.bar.controllers;

import com.bookish.bar.dtos.inputDtos.AuthorInputDto;
import com.bookish.bar.dtos.outputDtos.AuthorOutputDto;
import com.bookish.bar.services.AuthorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("/id")
    public ResponseEntity<AuthorOutputDto> getAuthorDetails(@PathVariable("id") String id) {
        AuthorOutputDto authorDetails = authorService.getAuthorById(id);
        return ResponseEntity.ok(authorDetails);
    }

    @PostMapping("/fetch/{id}")
    public ResponseEntity<Void> fetchAndSaveAuthor(@PathVariable String id) {
        authorService.fetchAndSaveAuthor(id);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


}
