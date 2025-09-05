package com.bookish.bar.services;

import com.bookish.bar.client.OpenLibraryClient;
import com.bookish.bar.dtos.dtos.BookDto;
import com.bookish.bar.dtos.mappers.BookMapper;
import com.bookish.bar.models.Author;
import com.bookish.bar.models.Book;
import com.bookish.bar.repositories.BookRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorService authorService;
    private final OpenLibraryClient openLibraryClient;

    public BookService(BookRepository bookRepository, AuthorService authorService, OpenLibraryClient openLibraryClient) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;
        this.openLibraryClient = openLibraryClient;
    }

    @Transactional
    public Book getOrCreateBook(String openLibraryId) {
        return bookRepository.findById(openLibraryId).orElseGet(() -> {
                    BookDto dto = openLibraryClient.fetchBook(openLibraryId);
                    Book entity = BookMapper.toEntity(dto);

            if (dto.getAuthors() != null) {
                Set<Author> authors = dto.getAuthors().stream()
                        .map(authorDto -> authorService.getOrCreateAuthor(authorDto.getId()))
                        .collect(Collectors.toSet());

                entity.setAuthors(authors);
            }

            return bookRepository.save(entity);
                });
    }

    public BookDto getBookDetails(String openLibraryId) {
        return openLibraryClient.fetchBook(openLibraryId);
    }

    public List<BookDto> searchBooks(String query, int page, int size) {
        return openLibraryClient.searchBooks(query, page, size);
    }
}
