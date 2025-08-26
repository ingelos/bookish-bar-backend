package com.bookish.bar.client;

import com.bookish.bar.models.Book;
import org.apache.coyote.Response;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
public class OpenLibraryClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String BASE_URL = "https://openlibrary.org";

    public Book fetchBook(String openLibraryId) {
        String url = BASE_URL +  "/works/" + openLibraryId + ".json";

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String, Object>>() {}
        );

        Map<String, Object> data = response.getBody();
        if (data == null) {
            throw new RuntimeException("No data from OpenLibrary for id: " + openLibraryId);
        }

        Book book = new Book();
        book.setOpenLibraryId(openLibraryId);

        book.setTitle((String) data.getOrDefault("title", "Unknown Title"));

        List<Map<String, Object>> authors = (List<Map<String, Object>>) data.get("authors");
        if (authors != null && !authors.isEmpty()) {
            Map<String, Object> authorMap = authors.getFirst();
            String authorKey = (String) ((Map<String, Object>) authorMap.get("author")).get("key");

            String authorUrl = BASE_URL + authorKey + ".json";
            ResponseEntity<Map<String, Object>> authorResponse = restTemplate.exchange(
                    authorUrl,
                    HttpMethod.GET,
                    null, new ParameterizedTypeReference<Map<String, Object>>() {}
            );

            Map<String, Object> authorData = authorResponse.getBody();
            if (authorData != null) {
                book.setAuthor((String) authorData.getOrDefault("name", "Unknown Author"));
            }
        }

        List<Integer> covers = (List<Integer>) data.get("covers");
        if (covers != null && !covers.isEmpty()) {
            Integer coverId = covers.getFirst();
            book.setCoverUrl("https://covers.openlibrary.org/b/id/" + covers.getFirst() + "-M.jpg");
        } else {
            book.setCoverUrl("https://covers.openlibrary.org/b/olid/" + openLibraryId + "-M.jped");
        }

        return book;
    }


}
