package com.bookish.bar.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "authorities")
public class Authority {

    @Id
    private String authority;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @ManyToMany(mappedBy = "authorities")
    private Set<User> users = new HashSet<>();

    public Authority(String authority) {
        this.authority = authority;
    }

}
