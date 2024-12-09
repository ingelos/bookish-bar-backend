package com.bookish.bar.services;

import com.bookish.bar.dtos.inputDtos.AuthorInputDto;
import com.bookish.bar.dtos.mappers.AuthorMapper;
import com.bookish.bar.dtos.outputDtos.AuthorOutputDto;
import com.bookish.bar.exceptions.ResourceNotFoundException;
import com.bookish.bar.models.Author;
import com.bookish.bar.repositories.AuthorRepository;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final RestTemplate restTemplate;

    public AuthorService(AuthorRepository authorRepository, RestTemplate restTemplate) {
        this.authorRepository = authorRepository;
        this.restTemplate = restTemplate;
    }


    public AuthorOutputDto getAuthorById(String id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found"));
        return AuthorMapper.toDto(author);
    }

    public void fetchAndSaveAuthor(String authorId) {
        Map<String, Object> authorData = fetchAuthorDataFromApi(authorId);

        AuthorInputDto inputDto = mapApiDataToAuthorInputDto(authorData);
        Author author = AuthorMapper.toModel(inputDto);

        authorRepository.save(author);
    }

    public Map<String, Object> fetchAuthorDataFromApi(String authorId) {
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                "http://openlibrary.org/authors/" + authorId + ".json",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );
        return response.getBody();
    }


    public AuthorInputDto mapApiDataToAuthorInputDto(Map<String, Object> authorData) {
        AuthorInputDto inputDto = new AuthorInputDto();
        assert authorData != null;
        inputDto.setId((String) authorData.get("key"));
        inputDto.setName((String) authorData.get("name"));
        inputDto.setBio((String) authorData.get("bio"));
        inputDto.setBirthDate((String) authorData.get("birth_date"));

        Object photosObject = authorData.get("photos");
        if (photosObject instanceof List<?> photosList) {
            List<Integer> photos = photosList.stream()
                    .filter(item -> item instanceof Integer)
                    .map(item -> (Integer) item)
                    .collect(Collectors.toList());
            inputDto.setPhotos(photos);
        } else {
            inputDto.setPhotos(Collections.emptyList());
        }
        return inputDto;
    }



}
