package com.bookish.bar.dtos.mappers;

import com.bookish.bar.dtos.inputDtos.UserInputDto;
import com.bookish.bar.dtos.outputDtos.UserOutputDto;
import com.bookish.bar.exceptions.ResourceNotFoundException;
import com.bookish.bar.models.User;

import java.util.ArrayList;
import java.util.List;

public class UserMapper {

    public static User userFromInputDtoToModel(UserInputDto dto, String encodedPassword) {
        return new User(
                dto.getUsername(),
                dto.getEmail(),
                encodedPassword,
                dto.getFirstname(),
                dto.getLastname(),
                dto.getBio(),
                AuthorityMapper.fromDtoToSet(dto.getAuthorities())
        );
    }

    public static UserOutputDto userFromModelToOutputDto(User user) {
        UserOutputDto dto = new UserOutputDto();

        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setFirstname(user.getFirstname());
        dto.setLastname(user.getLastname());
        dto.setBio(user.getBio());
        dto.setBookLists(user.getBookLists());
        dto.setAuthorities(user.getAuthorities());
        return dto;
    }

    public static List<UserOutputDto> userModelListToOutputList(List<User> users) {
        if(users.isEmpty()) {
            throw new ResourceNotFoundException("No users found.");
        }
        List<UserOutputDto> userOutputDtoList = new ArrayList<>();
        users.forEach((user) -> userOutputDtoList.add(userFromModelToOutputDto(user)));
        return userOutputDtoList;
    }

}
