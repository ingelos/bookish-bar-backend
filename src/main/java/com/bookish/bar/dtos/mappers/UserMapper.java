package com.bookish.bar.dtos.mappers;

import com.bookish.bar.dtos.inputDtos.UserInputDto;
import com.bookish.bar.dtos.outputDtos.UserOutputDto;
import com.bookish.bar.exceptions.ResourceNotFoundException;
import com.bookish.bar.models.User;

import java.util.ArrayList;
import java.util.List;

public class UserMapper {

    public static User userToModel(UserInputDto dto, String encodedPassword) {
        return new User(
                dto.getUsername(),
                dto.getEmail(),
                encodedPassword,
                AuthorityMapper.fromDtoToSet(dto.getAuthorities())
        );
    }

    public static UserOutputDto userModelToDto(User user) {
        UserOutputDto dto = new UserOutputDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setAuthorities(user.getAuthorities());
        return dto;
    }


    public static List<UserOutputDto> userToList(List<User> users) {
        if(users.isEmpty()) {
            throw new ResourceNotFoundException("No users found.");
        }
        List<UserOutputDto> userOutputDtoList = new ArrayList<>();
        users.forEach((user) -> userOutputDtoList.add(userModelToDto(user)));
        return userOutputDtoList;
    }


}
