package com.bookish.bar.dtos.dtos;

import com.bookish.bar.enums.BookListType;
import com.bookish.bar.models.Book;
import com.bookish.bar.models.BookListItem;
import lombok.Data;

@Data
public class BookListItemDto {
   private String openLibraryId;
   private String title;
   private String author;
   private String coverUrl;

   private Integer userRating;
   private BookListType listType;

   public static BookListItemDto fromEntity(BookListItem item) {
      Book book = item.getBook();

      BookListItemDto dto = new BookListItemDto();
      dto.setOpenLibraryId(book.getOpenLibraryId());
      dto.setTitle(book.getTitle());
      dto.setAuthor(book.getAuthor());
      dto.setCoverUrl(book.getCoverUrl());

      dto.setUserRating(item.getRating());
      dto.setListType(item.getBookList().getType());

      return dto;
   }


}
