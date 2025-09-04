package com.bookish.bar.services;

import com.bookish.bar.client.OpenLibraryClient;
import com.bookish.bar.dtos.dtos.AuthorDto;
import com.bookish.bar.models.Author;
import com.bookish.bar.repositories.AuthorRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final OpenLibraryClient openLibraryClient;

    public AuthorService(AuthorRepository authorRepository, OpenLibraryClient openLibraryClient) {
        this.authorRepository = authorRepository;
        this.openLibraryClient = openLibraryClient;
    }

    @Transactional
    public AuthorDto getOrCreateAuthor(String authorId) {
        Author author = authorRepository.findById(authorId).orElseGet(() -> {
            AuthorDto dto = openLibraryClient.fetchAuthor(authorId);
            Author entity = new Author();
            entity.setId(dto.getId());
            entity.setName(dto.getName());
            entity.setBio(dto.getBio());
            entity.setBirthDate(dto.getBirthDate());
            entity.setDeathDate(dto.getDeathDate());
            return authorRepository.save(entity);
        });
        return AuthorDto.fromEntity(author);

    }




}
