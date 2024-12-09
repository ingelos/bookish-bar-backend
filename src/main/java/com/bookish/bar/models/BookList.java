package com.bookish.bar.models;

import com.bookish.bar.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



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

    @Column
    @ManyToOne
    private User user;

    @Column
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @Column
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column
    private Integer rating;

}
