package com.bookish.bar.services;

import com.bookish.bar.client.OpenLibraryClient;
import com.bookish.bar.dtos.dtos.AuthorDto;
import com.bookish.bar.dtos.dtos.BookDto;
import com.bookish.bar.models.Author;
import com.bookish.bar.repositories.AuthorRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final OpenLibraryClient openLibraryClient;

    public AuthorService(AuthorRepository authorRepository, OpenLibraryClient openLibraryClient) {
        this.authorRepository = authorRepository;
        this.openLibraryClient = openLibraryClient;
    }

    @Transactional
    public Author getOrCreateAuthor(String authorId) {
        return authorRepository.findById(authorId).orElseGet(() -> {
            AuthorDto dto = openLibraryClient.fetchAuthorDetails(authorId);
            Author entity = new Author();
            entity.setId(dto.getId());
            entity.setName(dto.getName());
            entity.setBio(dto.getBio());
            entity.setBirthDate(dto.getBirthDate());
            entity.setDeathDate(dto.getDeathDate());
            return authorRepository.save(entity);
        });
    }

    @Transactional
    public AuthorDto getAuthorDetails(String authorId) {
        Author author = getOrCreateAuthor(authorId);
        return AuthorDto.fromEntity(author);
    }

    @Transactional
    public List<BookDto> getAuthorWorks(String authorId) {
        return openLibraryClient.fetchAuthorWorks(authorId);
    }


}
