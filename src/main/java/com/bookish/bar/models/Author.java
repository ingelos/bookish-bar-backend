package com.bookish.bar.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "authors")
public class Author {

    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(length = 2000)
    private String bio;

    private String birthDate;
    private String deathDate;

    @ManyToMany(mappedBy = "authors")
    private Set<Book> books = new HashSet<>();

}
