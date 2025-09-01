package com.bookish.bar.services;

import com.bookish.bar.client.OpenLibraryClient;
import com.bookish.bar.dtos.dtos.BookDto;
import com.bookish.bar.dtos.dtos.BookListDto;
import com.bookish.bar.dtos.dtos.BookListItemDto;
import com.bookish.bar.dtos.mappers.BookMapper;
import com.bookish.bar.enums.BookListType;
import com.bookish.bar.models.Book;
import com.bookish.bar.models.BookList;
import com.bookish.bar.models.BookListItem;
import com.bookish.bar.models.User;
import com.bookish.bar.repositories.BookListItemRepository;
import com.bookish.bar.repositories.BookListRepository;
import com.bookish.bar.repositories.BookRepository;
import com.bookish.bar.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookListService {

    private final UserRepository userRepository;
    private final BookListItemRepository bookListItemRepository;
    private final BookRepository bookRepository;
    private final BookListRepository bookListRepository;
    private final OpenLibraryClient openLibraryClient;

    public BookListService(UserRepository userRepository, BookListItemRepository bookListItemRepository, BookRepository bookRepository, BookListRepository bookListRepository, OpenLibraryClient openLibraryClient) {
        this.userRepository = userRepository;
        this.bookListItemRepository = bookListItemRepository;
        this.bookRepository = bookRepository;
        this.bookListRepository = bookListRepository;
        this.openLibraryClient = openLibraryClient;
    }

    public List<BookListItem> getAllBooks(User user) {
        return bookListItemRepository.findAllByUser(user);
    }


    public BookListService(UserRepository userRepository, BookRepository bookRepository, BookListRepository bookListRepository, BookListItemRepository bookListItemRepository, OpenLibraryClient openLibraryClient) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.bookListRepository = bookListRepository;
        this.bookListItemRepository = bookListItemRepository;
        this.openLibraryClient = openLibraryClient;
    }

    @Transactional
    public void addBookToList(User user, String openLibraryId, BookListType type, Integer rating) {

        Book book = bookRepository.findById(openLibraryId)
                .orElseGet(() -> {
                    BookDto fetchedBookDto = openLibraryClient.fetchBook(openLibraryId);
                    Book fetchedBook = BookMapper.toEntity(fetchedBookDto);
                    return bookRepository.save(fetchedBook);
                });

        BookList bookList = bookListRepository.findByUserAndType(user, type)
                .orElseThrow(() -> new IllegalStateException("Book list not found for user"));

        if (bookListItemRepository.existsByBookListAndBook(bookList, book)) {
            throw new IllegalStateException("Book already in list");
        }

        BookListItem item = new BookListItem();
        item.setBook(book);
        item.setBookList(bookList);

        if (type == BookListType.READ && rating != null) {
            item.setRating(rating);
        }

        bookListItemRepository.save(item);

    }

    @Transactional
    public BookListItem updateRating(User user, BookListType type, String openLibraryId, Integer newRating) {
        BookListItem item = bookListItemRepository
                .findByUserAndBookAndListType(user, openLibraryId, type)
                .orElseThrow(() -> new IllegalStateException("Book not on READ list"));

        if (type != BookListType.READ) {
            throw new IllegalStateException("Ratings are only allowed for READ books");
        }

        item.setRating(newRating);
        return bookListItemRepository.save(item);
    }

    @Transactional
    public BookListDto getUserBookList(User user, BookListType type) {
        BookList list = bookListRepository.findByUserAndType(user, type)
                .orElseThrow(() -> new IllegalStateException("List not found"));

        return BookListDto.fromEntity(list);
    }

    public void removeBookFromList(User user, BookListType type, String openLibraryId) {
        BookList list = bookListRepository.findByUserAndType(user, type)
                .orElseThrow(() -> new IllegalStateException("List not found"));

        Book book = bookRepository.findById(openLibraryId)
                .orElseThrow(() -> new IllegalStateException("Book not found"));

        BookListItem item = bookListItemRepository.findByBookListAndBook(list, book)
                .orElseThrow(() -> new IllegalStateException("Book not in list"));

        bookListItemRepository.delete(item);
    }

    @Transactional
    public List<BookListItemDto> getUserBookListItems(User user, BookListType type) {
        BookList list = bookListRepository.findByUserAndType(user, type)
                .orElseThrow(() -> new IllegalStateException("List not found"));
        return list.getBooks().stream().map(BookListItemDto::fromEntity)
                .toList();
    }
}
