package com.bookish.bar.repositories;

import com.bookish.bar.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, String> {
}
