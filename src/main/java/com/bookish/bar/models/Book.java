package com.bookish.bar.models;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "books")
public class Book {
    @Id
    private String openLibraryId;

    private String title;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> authors = new ArrayList<>();
    private Integer publishedYear;
    private String coverUrl;

}
