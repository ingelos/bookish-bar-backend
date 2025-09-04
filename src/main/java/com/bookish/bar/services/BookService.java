package com.bookish.bar.services;

import com.bookish.bar.client.OpenLibraryClient;
import com.bookish.bar.dtos.dtos.BookDto;
import com.bookish.bar.dtos.mappers.BookMapper;
import com.bookish.bar.models.Book;
import com.bookish.bar.repositories.BookRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final OpenLibraryClient openLibraryClient;

    public BookService(BookRepository bookRepository, OpenLibraryClient openLibraryClient) {
        this.bookRepository = bookRepository;
        this.openLibraryClient = openLibraryClient;
    }

    @Transactional
    public Book getOrCreateBook(String openLibraryId) {
        return bookRepository.findById(openLibraryId)
                .orElseGet(() -> {
                    BookDto dto = openLibraryClient.fetchBookDetails(openLibraryId);
                    Book entity = BookMapper.toEntity(dto);
                    return bookRepository.save(entity);
                });
    }

    public BookDto fetchBookFromApi(String openLibraryId) {
        return openLibraryClient.fetchBookDetails(openLibraryId);
    }


    public List<BookDto> searchBooks(String query, int page, int size) {
        return openLibraryClient.searchBooks(query, page, size);
    }
}
