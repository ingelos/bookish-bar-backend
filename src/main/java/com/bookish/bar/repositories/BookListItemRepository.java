package com.bookish.bar.repositories;

import com.bookish.bar.enums.BookListType;
import com.bookish.bar.models.Book;
import com.bookish.bar.models.BookList;
import com.bookish.bar.models.BookListItem;
import com.bookish.bar.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookListItemRepository extends JpaRepository<BookListItem, Long> {
    boolean existsByBookListAndBook(BookList list, Book book);
    List<BookListItem> findAllByUser(User user);
    Optional<BookListItem> findByUserAndBookAndListType(User user, String openLibraryId, BookListType type);
    Optional<BookListItem> findByBookListAndBook_OpenLibraryId(BookList list, String openLibraryId);
    int countByUserAndBookList_Type(User user, BookListType bookListType);
}
