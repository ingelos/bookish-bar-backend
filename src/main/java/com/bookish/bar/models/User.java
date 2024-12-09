package com.bookish.bar.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @GeneratedValue(strategy = GenerationType.UUID)
    private Long id;

    @Column(unique =true, nullable = false)
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


    public User(String username, String email, String password, String firstname,  String lastname, String bio, Set<Authority> authorities) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.bio = bio;
        this.authorities = authorities;

    }
}
