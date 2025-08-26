package com.bookish.bar.controllers;

import com.bookish.bar.dtos.dtos.ChangePasswordDto;
import com.bookish.bar.dtos.dtos.ChangeEmailDto;
import com.bookish.bar.dtos.dtos.ChangeUsernameDto;
import com.bookish.bar.dtos.dtos.UserResponseDto;
import com.bookish.bar.dtos.inputDtos.ProfileInputDto;
import com.bookish.bar.dtos.inputDtos.UserInputDto;
import com.bookish.bar.dtos.outputDtos.ProfileOutputDto;
import com.bookish.bar.dtos.outputDtos.UserOutputDto;
import com.bookish.bar.exceptions.ResourceNotFoundException;
import com.bookish.bar.models.Profile;
import com.bookish.bar.repositories.ProfileRepository;
import com.bookish.bar.services.PhotoService;
import com.bookish.bar.services.ProfileService;
import com.bookish.bar.services.UserService;
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
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final ProfileService profileService;
    private final PhotoService photoService;
    private final ProfileRepository profileRepository;

    public UserController(UserService userService, ProfileService profileService, PhotoService photoService, ProfileRepository profileRepository) {
        this.userService = userService;
        this.profileService = profileService;
        this.photoService = photoService;
        this.profileRepository = profileRepository;
    }

    // USER

    @PostMapping
    public ResponseEntity<UserOutputDto> createUser(@Valid @RequestBody UserInputDto userInputDto) {
        UserOutputDto createdUser = userService.createUser(userInputDto);

        URI uri = URI.create(ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/" + createdUser.getUsername())
                .toUriString());

        return ResponseEntity.created(uri).body(createdUser);
    }

    @GetMapping
    public ResponseEntity<List<UserOutputDto>> getUsers() {
        List<UserOutputDto> allUsers = userService.getAllUsers();
        return ResponseEntity.ok(allUsers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserOutputDto> getUserById(@PathVariable("id") Long id) {
        UserOutputDto userDto = userService.getUserById(id);
        return ResponseEntity.ok().body(userDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/change-username")
    public ResponseEntity<UserResponseDto> changeUsername(@PathVariable("id") Long id, @Valid @RequestBody ChangeUsernameDto changeUsernameDto) throws AccessDeniedException {
        UserResponseDto responseDto = userService.changeUsername(id, changeUsernameDto);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{id}/change-email")
    public ResponseEntity<UserOutputDto> changeEmail(@PathVariable("id") Long id, @Valid @RequestBody ChangeEmailDto changeEmailDto) throws AccessDeniedException {
        UserOutputDto updatedUser = userService.changeEmail(id, changeEmailDto);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/{id}/change-password")
    public ResponseEntity<Void> updatePassword(@PathVariable("id") Long id, @Valid @RequestBody ChangePasswordDto changePasswordDto) throws AccessDeniedException {
        userService.changePassword(id, changePasswordDto);
        return ResponseEntity.noContent().build();
    }

    // USER PROFILE


    @PostMapping("/{id}/profile")
    public ResponseEntity<ProfileOutputDto> createProfile(
            @PathVariable Long id, @Valid @RequestBody ProfileInputDto profileInputDto
    ) throws AccessDeniedException {

        ProfileOutputDto createdProfile = profileService.createProfile(id, profileInputDto);
        URI uri = URI.create(ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/" + createdProfile.getUsername())
                .toUriString());
        return ResponseEntity.created(uri).body(createdProfile);
    }


    @GetMapping("/{id}/profile")
    public ResponseEntity<ProfileOutputDto> getProfileByUserId(@PathVariable("id") Long id) throws AccessDeniedException {
        ProfileOutputDto profileDto = profileService.getProfileByUserId(id);
        return ResponseEntity.ok(profileDto);
    }

    @PutMapping("/{id}/profile")
    public ResponseEntity<ProfileOutputDto> updateProfile(
            @PathVariable("id") Long id,
            @Valid @RequestBody ProfileInputDto profileInputDto
    ) throws AccessDeniedException {
        ProfileOutputDto updatedProfile = profileService.updateProfile(id, profileInputDto);
        return ResponseEntity.ok(updatedProfile);
    }


    @DeleteMapping("/{id}/profile")
    public ResponseEntity<Void> deleteProfile(@PathVariable("id") Long id) throws AccessDeniedException {
        profileService.deleteProfileByUserId(id);
        return ResponseEntity.noContent().build();
    }

    // PROFILE PHOTO

    @PostMapping("/{id}/profile/photo")
    public ResponseEntity<String> uploadProfilePicture(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            String fileName = photoService.storeFile(file, id);

            Profile profile = profileRepository.findByUserId(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));
            profile.setProfilePhotoUrl(fileName);
            profileRepository.save(profile);

            return ResponseEntity.ok("File uploaded successfully: " + fileName);
        } catch(IOException | java.io.IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed.");
        }
    }

    @DeleteMapping("/{id}/profile/photo")
    public ResponseEntity<String> deleteProfilePhoto(@PathVariable Long id) {
        photoService.deleteFile(id);
        return ResponseEntity.ok("File deleted successfully");
    }
}
