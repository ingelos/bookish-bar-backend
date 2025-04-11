package com.bookish.bar.services;

import com.bookish.bar.dtos.dtos.ChangePasswordDto;
import com.bookish.bar.dtos.dtos.ChangeEmailDto;
import com.bookish.bar.dtos.dtos.ChangeUsernameDto;
import com.bookish.bar.dtos.dtos.UserResponseDto;
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
import com.bookish.bar.utils.JwtUtil;
import com.bookish.bar.utils.SecurityUtils;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecurityUtils securityUtils;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, SecurityUtils securityUtils, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.securityUtils = securityUtils;
        this.jwtUtil = jwtUtil;
    }


    @Transactional
    public UserOutputDto createUser(UserInputDto userInputDto) {
        if (userRepository.existsByUsername(userInputDto.getUsername())) {
            throw new UsernameAlreadyExistsException("Username is already taken, try another username.");
        }

        String encodedPassword = passwordEncoder.encode(userInputDto.getPassword());
        User user = UserMapper.userToModel(userInputDto, encodedPassword);

        Authority defaultAuthority = new Authority("USER");
        user.getAuthorities().add(defaultAuthority);

        User savedUser = userRepository.save(user);
        return UserMapper.userModelToDto(savedUser);
    }

    @Transactional
    public UserOutputDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return UserMapper.userModelToDto(user);
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
    public UserResponseDto changeUsername(Long id, ChangeUsernameDto changeUsernameDto) throws AccessDeniedException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        securityUtils.assertUserIsOwner(id);

        if (!passwordEncoder.matches(changeUsernameDto.getPassword(), user.getPassword())) {
            throw new AccessDeniedException("Incorrect password");
        }

        if (userRepository.existsByUsername(changeUsernameDto.getUsername())) {
            throw new BadRequestException("Username already in use");
        }

        user.setUsername(changeUsernameDto.getUsername());
        userRepository.save(user);

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
                .toList());

        String newToken = jwtUtil.generateToken(userDetails);

        return new UserResponseDto(user, newToken);
    }


    @Transactional
    public UserOutputDto changeEmail(Long id, ChangeEmailDto changeEmailDto) throws AccessDeniedException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        securityUtils.assertUserIsOwner(id);

        if (!passwordEncoder.matches(changeEmailDto.getPassword(), user.getPassword())) {
            throw new AccessDeniedException("Incorrect password");
        }

        user.setEmail(changeEmailDto.getEmail());
        userRepository.save(user);

        return UserMapper.userModelToDto(user);
    }


    @Transactional
    public void changePassword(Long id, @Valid ChangePasswordDto changePasswordDto) throws AccessDeniedException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        securityUtils.assertUserIsOwner(id);

        if (!passwordEncoder.matches(changePasswordDto.getCurrentPassword(), user.getPassword())) {
            throw new AccessDeniedException("Current password is not correct");
        }
        if (!changePasswordDto.getNewPassword().equals(changePasswordDto.getConfirmPassword())) {
            throw new BadRequestException("Passwords do not match");
        }

        user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
        userRepository.save(user);
    }
}
