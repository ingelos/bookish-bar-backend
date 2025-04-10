package com.bookish.bar.services;

import com.bookish.bar.dtos.dtos.UpdatePasswordDto;
import com.bookish.bar.dtos.dtos.UpdateUserDetailsDto;
import com.bookish.bar.dtos.inputDtos.UserInputDto;
import com.bookish.bar.dtos.mappers.UserMapper;
import com.bookish.bar.dtos.outputDtos.UserOutputDto;
import com.bookish.bar.exceptions.BadRequestException;
import com.bookish.bar.exceptions.ResourceNotFoundException;
import com.bookish.bar.exceptions.UsernameAlreadyExistsException;
import com.bookish.bar.exceptions.UsernameNotFoundException;
import com.bookish.bar.models.Authority;
import com.bookish.bar.models.User;
import com.bookish.bar.repositories.UserRepository;
import com.bookish.bar.utils.SecurityUtils;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Transactional
    public UserOutputDto createUser(UserInputDto userInputDto) {
        if(userRepository.existsByUsername(userInputDto.getUsername())) {
            throw new UsernameAlreadyExistsException("Username is already taken, try another username.");
        }

        String encodedPassword = passwordEncoder.encode(userInputDto.getPassword());
        User user = UserMapper.userToModel(userInputDto, encodedPassword);

        Authority defaultAuthority = new Authority("USER");
        user.getAuthorities().add(defaultAuthority);

        User savedUser = userRepository.save(user);
        return UserMapper.userFromModelToOutputDto(savedUser);
    }

    @Transactional
    public UserOutputDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return UserMapper.userFromModelToOutputDto(user);
    }

//    public User getUserEntityById(Long id) {
//        return userRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
//    }


    public User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public List<UserOutputDto> getAllUsers() {
        List<User> allUsers = userRepository.findAll();
        return UserMapper.userToList(allUsers);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        userRepository.delete(user);
    }

    @Transactional
    public UserOutputDto updateUserDetails(Long id, UpdateUserDetailsDto updateUserDto) throws AccessDeniedException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        SecurityUtils.assertAuthenticatedUser(user.getUsername());


        if (updateUserDto.getUsername() != null) {
            user.setUsername(updateUserDto.getUsername());
        }
        if (updateUserDto.getEmail() != null) {
            user.setEmail(updateUserDto.getEmail());
        }

        User updatedUser = UserMapper.updateUserToModel(user, updateUserDto);
        updatedUser = userRepository.save(updatedUser);
        return UserMapper.userFromModelToOutputDto(updatedUser);
    }

    @Transactional
    public void updatePassword(Long id, @Valid UpdatePasswordDto updatePasswordDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(updatePasswordDto.getCurrentPassword(), user.getPassword())) {
            throw new BadRequestException("Current password is not correct");
        }
        if (!updatePasswordDto.getNewPassword().equals(updatePasswordDto.getConfirmPassword())) {
            throw new BadRequestException("Passwords do not match");
        }

        String encodedPassword = passwordEncoder.encode(updatePasswordDto.getNewPassword());
        UserMapper.updatePasswordToModel(user, encodedPassword);
        userRepository.save(user);
    }
}
