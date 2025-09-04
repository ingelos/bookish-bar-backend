package com.bookish.bar.client;

import com.bookish.bar.dtos.dtos.AuthorDto;
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


    // Lightweight search

    public List<BookDto> searchBooks(String query, int page, int size) {
        String url = BASE_URL + "/search.json?q=" + UriUtils.encode(query, StandardCharsets.UTF_8)
                + "&page=" + page + "&limit=" + size;

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {}
        );

        Map<String, Object> body = response.getBody();
        if (body == null || !body.containsKey("docs")) return List.of();

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> docs = (List<Map<String, Object>>) body.get("docs");

        return docs.stream()
                .map(this::fromDocToBookDto)
                .toList();
    }


    // Full details book fetch

    public BookDto fetchBookDetails(String openLibraryId) {
        String url = BASE_URL +  "/works/" + openLibraryId + ".json";

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                url, HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, Object>>() {}
        );

        Map<String, Object> data = response.getBody();
        if (data == null) throw new RuntimeException("No data from OpenLibrary for id: " + openLibraryId);

        BookDto dto = new BookDto();
        dto.setOpenLibraryId(openLibraryId);
        dto.setTitle((String) data.getOrDefault("title", "Unknown Title"));

        // Authors
        List<String> authorNames = new ArrayList<>();
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> authors = (List<Map<String, Object>>) data.get("authors");
        if (authors != null && !authors.isEmpty()) {
            for (Map<String, Object> authorMap : authors) {
                @SuppressWarnings("unchecked")
                    String authorKey = (String) ((Map<String, Object>) authorMap.get("author")).get("key");
                    String authorUrl = BASE_URL + authorKey + ".json";

                    ResponseEntity<Map<String, Object>> authorResponse = restTemplate.exchange(
                            authorUrl, HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, Object>>() {}
                    );

                    Map<String, Object> authorData = authorResponse.getBody();
                    if (authorData != null) {
                       authorNames.add((String) authorData.getOrDefault("name", "Unknown Author"));
                    }
                }
            }
        dto.setAuthors(authorNames);

        // Published Year
        @SuppressWarnings("unchecked")
        Map<String, Object> created = (Map<String, Object>) data.get("created");
        if (created != null && created.get("value") instanceof String createdDate) {
            dto.setPublishedYear(Integer.parseInt(createdDate.substring(0, 4)));
        }

        // Cover
        @SuppressWarnings("unchecked")
        List<Integer> covers = (List<Integer>) data.get("covers");
        if (covers != null && !covers.isEmpty()) {
            dto.setCoverUrl("https://covers.openlibrary.org/b/id/" + covers.getFirst() + "-M.jpg");
        } else {
            dto.setCoverUrl("https://covers.openlibrary.org/b/olid/" + openLibraryId + "-M.jped");
        }

        // Description
        Object desc = data.get("description");
        if (desc instanceof String description) dto.setDescription(description);
        else if (desc instanceof Map<?, ?> map) dto.setDescription((String) map.get("value"));

        // First Sentence
        Object firstSentence = data.get("first_sentence");
        if (firstSentence instanceof String f) dto.setFirstSentence(f);
        else if (firstSentence instanceof Map<?, ?> map) dto.setFirstSentence((String) map.get("value"));

        return dto;
    }






    // Mapper for search

    private BookDto fromDocToBookDto(Map<String, Object> doc) {
        BookDto dto = new BookDto();

        String key = (String) doc.get("key");
        if (key != null && key.startsWith("/works/")) {
            dto.setOpenLibraryId(key.substring("/works/".length()));
        } else {
            dto.setOpenLibraryId("unknown");
        }

        dto.setTitle((String) doc.getOrDefault("title", "Unknown title"));

        @SuppressWarnings("unchecked")
        List<String> authors = (List<String>) doc.get("author_name");
        if (authors != null && !authors.isEmpty()) {dto.setAuthors(authors);
        } else {dto.setAuthors(List.of("Unknown Author"));
        }

        Object year = doc.get("first_publish_year");
        if (year instanceof Integer) {dto.setPublishedYear((Integer) year);
        } else {dto.setPublishedYear(null);
        }

        Object coverId = doc.get("cover_i");
        if (coverId instanceof Integer) {
            dto.setCoverUrl("https://covers.openlibrary.org/b/id/" + coverId + "-M.jpg");
        } else {dto.setCoverUrl("/images/no-cover.png");
        }

        return dto;
    }

    public AuthorDto fetchAuthor(String authorId) {
        String url = BASE_URL + "/authors" + authorId + ".json";
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                url, HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, Object>>() {}
        );

        Map<String, Object> data = response.getBody();
        if (data == null) throw new RuntimeException("No author found");

        AuthorDto dto = new AuthorDto();
        dto.setId(authorId);
        dto.setName((String) data.getOrDefault("name", "Unknown Author"));
        dto.setBio(data.get("bio") instanceof String ? (String) data.get("bio") : null);
        dto.setBirthDate((String) data.get("birth_date"));
        dto.setDeathDate((String) data.get("death_date"));
        return dto;

    }
}
