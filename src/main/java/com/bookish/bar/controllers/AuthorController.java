package com.bookish.bar.controllers;

import com.bookish.bar.client.OpenLibraryClient;
import com.bookish.bar.dtos.dtos.AuthorDto;
import com.bookish.bar.repositories.AuthorRepository;
import com.bookish.bar.services.AuthorService;
import org.springframework.security.access.AuthorizationServiceException;
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
    public AuthorDto getAuthor(@PathVariable String authorId)  {
        return authorService.getOrCreateAuthor(authorId);
    }



}
