package com.bookish.bar.dtos.mappers;

import com.bookish.bar.dtos.inputDtos.ProfileInputDto;
import com.bookish.bar.dtos.outputDtos.ProfileOutputDto;
import com.bookish.bar.dtos.outputDtos.UserOutputDto;
import com.bookish.bar.exceptions.ResourceNotFoundException;
import com.bookish.bar.models.Profile;
import com.bookish.bar.models.User;

import java.util.ArrayList;
import java.util.List;

public class ProfileMapper {

    public static Profile profileToModel(ProfileInputDto inputDto, User user) {
        Profile profile= new Profile();
        profile.setFirstname(inputDto.getFirstname());
        profile.setLastname(inputDto.getLastname());
        profile.setAbout(inputDto.getAbout());
        profile.setUser(user);
        return profile;
    }

    public static ProfileOutputDto profileToDto(Profile profile) {
        ProfileOutputDto dto = new ProfileOutputDto();
        dto.setId(profile.getId());
        dto.setUsername(profile.getUser().getUsername());
        dto.setFirstname(profile.getFirstname());
        dto.setLastname(profile.getLastname());
        dto.setAbout(profile.getAbout());
        dto.setProfilePhotoUrl(profile.getProfilePhotoUrl());

        return dto;
    }

    public static List<ProfileOutputDto> profileToList(List<Profile> profiles) {
        if(profiles.isEmpty()) {
            throw new ResourceNotFoundException("No profiles found.");
        }
        List<ProfileOutputDto> profileOutputDtoList = new ArrayList<>();
        profiles.forEach((profile) -> profileOutputDtoList.add(profileToDto(profile)));
        return profileOutputDtoList;
    }


}
