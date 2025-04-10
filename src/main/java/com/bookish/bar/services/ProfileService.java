package com.bookish.bar.services;

import com.bookish.bar.dtos.inputDtos.ProfileInputDto;
import com.bookish.bar.dtos.mappers.ProfileMapper;
import com.bookish.bar.dtos.outputDtos.ProfileOutputDto;
import com.bookish.bar.dtos.outputDtos.UserOutputDto;
import com.bookish.bar.exceptions.BadRequestException;
import com.bookish.bar.exceptions.ResourceNotFoundException;
import com.bookish.bar.models.Profile;
import com.bookish.bar.models.User;
import com.bookish.bar.repositories.ProfileRepository;
import com.bookish.bar.repositories.UserRepository;
import com.bookish.bar.utils.SecurityUtils;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import org.springframework.expression.AccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
public class ProfileService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    public ProfileService(UserRepository userRepository, ProfileRepository profileRepository) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
    }


    @Transactional
    public ProfileOutputDto createProfile(Long id, ProfileInputDto profileInputDto) throws AccessDeniedException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        SecurityUtils.assertAuthenticatedUser(user.getUsername());

        if (user.getProfile() != null) {
            throw new IllegalStateException("User already has a profile");
        }

        Profile profile = ProfileMapper.profileToModel(profileInputDto, user);
        profile.setUser(user);

        Profile savedProfile = profileRepository.save(profile);
        user.setProfile(savedProfile);
        userRepository.save(user);

        return ProfileMapper.profileToDto(savedProfile);
    }


    @Transactional
    public List<ProfileOutputDto> getProfiles() {
        List<Profile> allProfiles = profileRepository.findAll();
        return ProfileMapper.profileToList(allProfiles);
    }


    @Transactional
    public ProfileOutputDto getProfileByUsername(String username) {

        Profile profile = profileRepository.findByUser_Username(username)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        if (profile == null) {
            throw new ResourceNotFoundException("Profile not found");
        }

        return ProfileMapper.profileToDto(profile);
    }


    @Transactional
    public ProfileOutputDto getProfileByUserId(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Profile profile = user.getProfile();

        if (profile == null) {
            throw new ResourceNotFoundException("Profile not found for user ID: " + id);
        }

        return ProfileMapper.profileToDto(profile);
    }


    @Transactional
    public ProfileOutputDto updateProfile(Long id, ProfileInputDto profileInputDto) throws AccessDeniedException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        SecurityUtils.assertAuthenticatedUser(user.getUsername());

        Profile existingProfile = user.getProfile();

        if (existingProfile == null) {
            throw new ResourceNotFoundException("No profile exists");
        }

        existingProfile.setFirstname(profileInputDto.getFirstname());
        existingProfile.setLastname(profileInputDto.getLastname());
        existingProfile.setAbout(profileInputDto.getAbout());

        Profile updated = profileRepository.save(existingProfile);
        return ProfileMapper.profileToDto(updated);
    }


    @Transactional
    public void deleteProfileByUserId(Long id) throws AccessDeniedException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        SecurityUtils.assertAuthenticatedUser(user.getUsername());

        Profile profile = user.getProfile();

        if (profile == null) {
            throw new ResourceNotFoundException("No profile exists for this user");
        }

        user.setProfile(null);
        userRepository.save(user);
        profileRepository.delete(profile);
    }


}
