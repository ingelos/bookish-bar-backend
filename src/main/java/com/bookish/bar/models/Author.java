package com.bookish.bar.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "authors")
public class Author {

    @Id
    private String id;

    @Column
    private String name;

    @Column
    private String bio;

    @Column
    private String birthDate;

    @ElementCollection
    private List<Integer> photos;


    @Column
    @ManyToMany(mappedBy = "authors")
    private List<Book> books;

}
