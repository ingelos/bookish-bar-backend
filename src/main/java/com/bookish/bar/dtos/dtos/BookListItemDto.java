package com.bookish.bar.dtos.dtos;

import com.bookish.bar.enums.BookListType;
import com.bookish.bar.models.Book;
import com.bookish.bar.models.BookListItem;
import lombok.Data;

import java.util.List;

@Data
public class BookListItemDto {
   private String openLibraryId;
   private String title;
   private List<String> authors;
   private String coverUrl;

   private Integer rating;
   private BookListType listType;


   public static BookListItemDto fromEntity(BookListItem item) {
      Book book = item.getBook();
      BookListItemDto dto = new BookListItemDto();
      dto.setOpenLibraryId(book.getOpenLibraryId());
      dto.setTitle(book.getTitle());
      dto.setAuthors(book.getAuthors());
      dto.setCoverUrl(book.getCoverUrl());
      dto.setRating(item.getRating());
      dto.setListType(item.getBookList().getType());
      return dto;
   }

   public static BookDto toBookDto(BookListItem item) {
      Book book = item.getBook();
      BookDto dto = new BookDto();
      dto.setOpenLibraryId(book.getOpenLibraryId());
      dto.setTitle(book.getTitle());
      dto.setAuthors(book.getAuthors());
      dto.setPublishedYear(book.getPublishedYear());
      dto.setCoverUrl(book.getCoverUrl());
      return dto;
   }

}
