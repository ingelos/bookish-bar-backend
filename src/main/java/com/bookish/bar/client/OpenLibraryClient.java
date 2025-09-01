package com.bookish.bar.client;

import com.bookish.bar.dtos.dtos.BookDto;
import com.bookish.bar.models.Book;
import org.apache.coyote.Response;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class OpenLibraryClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String BASE_URL = "https://openlibrary.org";

    public BookDto fetchBook(String openLibraryId) {
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

        BookDto dto = new BookDto();
        dto.setOpenLibraryId(openLibraryId);
        dto.setTitle((String) data.getOrDefault("title", "Unknown Title"));

        List<String> authorNames = new ArrayList<>();
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> authors = (List<Map<String, Object>>) data.get("authors");
        if (authors != null && !authors.isEmpty()) {
            for (Map<String, Object> authorMap : authors) {
                @SuppressWarnings("unchecked")
//                Map<String, Object> authorObj = (Map<String, Object>) authorMap.get("author");
//                if (authorObj != null) {
                    String authorKey = (String) ((Map<String, Object>) authorMap.get("author")).get("key");
                    String authorUrl = BASE_URL + authorKey + ".json";

                    ResponseEntity<Map<String, Object>> authorResponse = restTemplate.exchange(
                            authorUrl,
                            HttpMethod.GET,
                            null,
                            new ParameterizedTypeReference<Map<String, Object>>() {}
                    );

                    Map<String, Object> authorData = authorResponse.getBody();
                    if (authorData != null) {
                       authorNames.add((String) authorData.getOrDefault("name", "Unknown Author"));
                    }
                }
            }
        dto.setAuthors(authorNames);

        @SuppressWarnings("unchecked")
        Map<String, Object> created = (Map<String, Object>) data.get("created");
        if (created != null && created.get("value") instanceof String createdDate) {
            dto.setPublishedYear(Integer.parseInt(createdDate.substring(0, 4)));
        }

        @SuppressWarnings("unchecked")
        List<Integer> covers = (List<Integer>) data.get("covers");
        if (covers != null && !covers.isEmpty()) {
            dto.setCoverUrl("https://covers.openlibrary.org/b/id/" + covers.getFirst() + "-M.jpg");
        } else {
            dto.setCoverUrl("https://covers.openlibrary.org/b/olid/" + openLibraryId + "-M.jped");
        }

        return dto;
    }



    public List<BookDto> searchBooks(String query) {
        String url = BASE_URL + "/search.json?q=" + UriUtils.encode(query, StandardCharsets.UTF_8);
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        Map<String, Object> body = response.getBody();
        if (body == null || !body.containsKey("docs")) {
            return List.of();
        }

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> docs = (List<Map<String, Object>>) body.get("docs");

        return docs.stream()
                .map(this::fromDocToBookDto)
                .toList();
    }

    private BookDto fromDocToBookDto(Map<String, Object> doc) {
        String openLibraryId = (doc.get("key") != null) ? doc.get("key").toString().replace("/works/", "") : null;
        String title = (String) doc.getOrDefault("title", "Unknown title");

        @SuppressWarnings("unchecked")
        List<String> authors = (List<String>) doc.getOrDefault("author_name", List.of());

        Integer publishedYear = null;
        if (doc.containsKey("first_publish_year")) {
            publishedYear = (Integer) doc.get("first_publish_year");
        }

        // covers
        String coverUrl = null;
        if (doc.containsKey("cover_i")) {
            coverUrl = "https://covers.openlibrary.org/b/id/" + doc.get("cover_id") + "-M.jped";
        }

        return new BookDto(openLibraryId, title, authors, publishedYear, coverUrl);

    }



}
