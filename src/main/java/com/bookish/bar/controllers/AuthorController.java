package com.bookish.bar.controllers;

import com.bookish.bar.dtos.dtos.AuthorDto;
import com.bookish.bar.services.AuthorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("/{authorId}")
    public ResponseEntity<AuthorDto> getAuthor(@PathVariable String authorId)  {
        return ResponseEntity.ok(authorService.getAuthor(authorId));
    }



}
