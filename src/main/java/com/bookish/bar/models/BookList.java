package com.bookish.bar.models;

import com.bookish.bar.enums.BookListType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "booklists")
public class BookList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String listName;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    @Enumerated(EnumType.STRING)
    private BookListType type;

    @OneToMany(mappedBy = "bookList", cascade = CascadeType.ALL)
    private List<BookListItem> books = new ArrayList<>();

}
