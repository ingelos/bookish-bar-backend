package com.bookish.bar.services;
import com.bookish.bar.dtos.dtos.BookListDto;
import com.bookish.bar.dtos.dtos.BookListItemDto;
import com.bookish.bar.enums.BookListType;
import com.bookish.bar.models.Book;
import com.bookish.bar.models.BookList;
import com.bookish.bar.models.BookListItem;
import com.bookish.bar.models.User;
import com.bookish.bar.repositories.BookListItemRepository;
import com.bookish.bar.repositories.BookListRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookListService {

    private final BookListItemRepository bookListItemRepository;
    private final BookListRepository bookListRepository;
    private final BookService bookService;

    public BookListService(BookListItemRepository bookListItemRepository, BookListRepository bookListRepository, BookService bookService) {
        this.bookListItemRepository = bookListItemRepository;
        this.bookListRepository = bookListRepository;
        this.bookService = bookService;
    }

    @Transactional
    public List<BookListItem> getAllBooksByUser(User user) {
        return bookListItemRepository.findAllByUser(user);
    }

    @Transactional
    public void addBookToList(User user, String openLibraryId, BookListType type, Integer rating) {
        Book book = bookService.getOrCreateBook(openLibraryId);

        BookList list = bookListRepository.findByUserAndType(user, type)
                .orElseThrow(() -> new IllegalStateException("Book list not found for user"));

        if (bookListItemRepository.existsByBookListAndBook(list, book)) {
            throw new IllegalStateException("Book already in list");
        }

        BookListItem item = new BookListItem();
        item.setBook(book);
        item.setBookList(list);

        if (type == BookListType.READ && rating != null) {
            item.setRating(rating);
        }

        bookListItemRepository.save(item);

    }

    @Transactional
    public void removeBookFromList(User user, BookListType type, String openLibraryId) {
        BookList list = bookListRepository.findByUserAndType(user, type)
                .orElseThrow(() -> new IllegalStateException("List not found"));

        BookListItem item = bookListItemRepository.findByBookListAndBook_OpenLibraryId(list, openLibraryId)
                .orElseThrow(() -> new IllegalStateException("Book not in list"));

        bookListItemRepository.delete(item);
    }


    @Transactional
    public BookListItem updateRating(User user, BookListType type, String openLibraryId, Integer newRating) {
        BookListItem item = bookListItemRepository
                .findByUserAndBookAndListType(user, openLibraryId, BookListType.READ)
                .orElseThrow(() -> new IllegalStateException("Book not on READ list"));

        item.setRating(newRating);
        return bookListItemRepository.save(item);
    }


    // Get user book list (want-to-read, read, or virtual all)
    @Transactional
    public BookListDto getUserBookList(User user, BookListType type) {
        List<BookListItem> items;

        if (type == BookListType.ALL) {
            items = bookListItemRepository.findAllByUser(user);
        } else {
            BookList list = bookListRepository.findByUserAndType(user, type)
                    .orElseThrow(() -> new IllegalStateException("List not found"));
            items = list.getBooks();
        }
        List<BookListItemDto> books = items.stream()
                .map(BookListItemDto::fromEntity)
                .toList();

        BookListDto dto = new BookListDto();
        dto.setListName(type == BookListType.ALL ? "All" : type.name());
        dto.setType(type);
        dto.setBooks(books);
        return dto;
    }


    @Transactional
    public List<BookListItemDto> getUserBookListItems(User user, BookListType type) {
        if (type == BookListType.ALL) {
            return getAllUserBooks(user);
        }

        BookList list = bookListRepository.findByUserAndType(user, type)
                .orElseThrow(() -> new IllegalStateException("List not found"));

        return list.getBooks().stream().map(BookListItemDto::fromEntity)
                .toList();
    }

    @Transactional
    public List<BookListItemDto> getAllUserBooks(User user) {
        List<BookList> lists = bookListRepository.findByUser(user);
        return lists.stream()
                .flatMap(list -> list.getBooks().stream())
                .map(BookListItemDto::fromEntity)
                .toList();
    }
}


