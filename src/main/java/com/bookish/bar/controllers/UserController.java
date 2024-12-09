package com.bookish.bar.controllers;

import com.bookish.bar.dtos.inputDtos.UserInputDto;
import com.bookish.bar.dtos.outputDtos.UserOutputDto;
import com.bookish.bar.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserOutputDto> createUser(@Valid @RequestBody UserInputDto userInputDto) {
        UserOutputDto createdUser = userService.createUser(userInputDto);

        URI uri = URI.create(ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/" + createdUser.getUsername())
                .toUriString());

        return ResponseEntity.created(uri).body(createdUser);
    }

}
