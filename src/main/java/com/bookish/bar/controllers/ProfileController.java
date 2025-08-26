package com.bookish.bar.controllers;

import com.bookish.bar.dtos.inputDtos.ProfileInputDto;
import com.bookish.bar.dtos.outputDtos.ProfileOutputDto;
import com.bookish.bar.exceptions.ResourceNotFoundException;
import com.bookish.bar.models.Profile;
import com.bookish.bar.models.User;
import com.bookish.bar.repositories.ProfileRepository;
import com.bookish.bar.repositories.UserRepository;
import com.bookish.bar.services.PhotoService;
import com.bookish.bar.services.ProfileService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/profiles")
public class ProfileController {

    private final ProfileService profileService;
    private final PhotoService photoService;
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    public ProfileController(ProfileService profileService, PhotoService photoService, UserRepository userRepository, ProfileRepository profileRepository) {
        this.profileService = profileService;
        this.photoService = photoService;
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
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

    @GetMapping("/{username}/photo")
    public ResponseEntity<Resource> getProfilePhoto(@PathVariable("username") String username,  HttpServletRequest request) throws FileNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Profile profile = profileRepository.findById(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        String photoFileName = profile.getProfilePhotoUrl();
        if (photoFileName == null) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = photoService.downLoadFile(photoFileName);

        String mimeType;
        try {
            mimeType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException e) {
            mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(mimeType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline;fileName=" + resource.getFilename())
                .body(resource);
    }

}

