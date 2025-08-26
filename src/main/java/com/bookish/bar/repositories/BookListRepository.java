package com.bookish.bar.repositories;

import com.bookish.bar.enums.BookListType;
import com.bookish.bar.models.BookList;
import com.bookish.bar.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookListRepository extends JpaRepository<BookList, Long> {
//    List<BookList> findByUserId(Long userId);
    Optional<BookList> findByUserAndType(User user, BookListType type);

}
