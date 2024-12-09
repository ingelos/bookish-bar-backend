package com.bookish.bar.services;

import com.bookish.bar.config.CustomPasswordEncoder;
import com.bookish.bar.dtos.inputDtos.UserInputDto;
import com.bookish.bar.dtos.mappers.UserMapper;
import com.bookish.bar.dtos.outputDtos.UserOutputDto;
import com.bookish.bar.exceptions.UsernameAlreadyExistsException;
import com.bookish.bar.exceptions.UsernameNotFoundException;
import com.bookish.bar.models.Authority;
import com.bookish.bar.models.User;
import com.bookish.bar.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        User user = UserMapper.userFromInputDtoToModel(userInputDto, encodedPassword);

        Authority defaultAuthority = new Authority("READER");
        user.getAuthorities().add(defaultAuthority);

        User savedUser = userRepository.save(user);
        return UserMapper.userFromModelToOutputDto(savedUser);
    }

    public User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
