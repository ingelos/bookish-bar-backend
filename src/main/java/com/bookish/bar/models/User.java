package com.bookish.bar.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String username;
    @Column
    private String email;
    @Column
    private String password;
    @Column
    private String firstname;
    @Column
    private String lastname;
    @Column
    private String bio;


    @Setter(AccessLevel.NONE)
    @OneToMany(mappedBy = "user")
    private List<BookList> bookLists;

    @Setter(AccessLevel.NONE)
    @ManyToMany
    private Set<Authority> authorities = new HashSet<>();


}
