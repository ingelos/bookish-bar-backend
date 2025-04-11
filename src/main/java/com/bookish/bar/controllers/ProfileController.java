package com.bookish.bar.controllers;

import com.bookish.bar.dtos.inputDtos.ProfileInputDto;
import com.bookish.bar.dtos.outputDtos.ProfileOutputDto;
import com.bookish.bar.services.PhotoService;
import com.bookish.bar.services.ProfileService;
import io.jsonwebtoken.io.IOException;
import jakarta.validation.Valid;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/profiles")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }


    @GetMapping
    public ResponseEntity<List<ProfileOutputDto>> getAllProfiles() {
        List<ProfileOutputDto> allProfiles = profileService.getProfiles();
        return ResponseEntity.ok(allProfiles);
    }

    @GetMapping("/{username}")
    public ResponseEntity<ProfileOutputDto> getProfileByUsername(@PathVariable("username") String username) {
        return ResponseEntity.ok(profileService.getProfileByUsername(username));
    }

}

