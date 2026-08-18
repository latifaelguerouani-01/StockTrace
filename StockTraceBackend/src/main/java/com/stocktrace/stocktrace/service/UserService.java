package com.stocktrace.stocktrace.service;

import com.stocktrace.stocktrace.dto.ChangeCredentialsRequest;
import com.stocktrace.stocktrace.entity.User;
import com.stocktrace.stocktrace.exception.BadRequestException;
import com.stocktrace.stocktrace.exception.ResourceNotFoundException;
import com.stocktrace.stocktrace.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + id
                        ));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with email: " + email
                        ));
    }

    /**
     * Change email and/or password of the currently
     * authenticated shared account.
     */
    public User updateMyCredentials(
            String currentEmail,
            ChangeCredentialsRequest request) {

        User user = getUserByEmail(currentEmail);

        if (request.getCurrentPassword() == null
                || request.getCurrentPassword().isBlank()) {

            throw new BadRequestException(
                    "Current password is required"
            );
        }

        // Verify current password
        if (!passwordEncoder.matches(
                request.getCurrentPassword(),
                user.getPassword())) {

            throw new BadRequestException(
                    "Current password is incorrect"
            );
        }

        boolean emailChanged =
                request.getNewEmail() != null
                        && !request.getNewEmail().isBlank()
                        && !user.getEmail()
                        .equalsIgnoreCase(request.getNewEmail());

        boolean passwordChanged =
                request.getNewPassword() != null
                        && !request.getNewPassword().isBlank();

        if (!emailChanged && !passwordChanged) {
            throw new BadRequestException(
                    "No changes were provided"
            );
        }

        // Change email
        if (emailChanged) {

            if (userRepository.existsByEmail(
                    request.getNewEmail())) {

                throw new BadRequestException(
                        "Email '" +
                                request.getNewEmail() +
                                "' is already in use"
                );
            }

            user.setEmail(
                    request.getNewEmail().trim()
            );
        }

        // Change password
        if (passwordChanged) {
            user.setPassword(
                    passwordEncoder.encode(
                            request.getNewPassword()
                    )
            );
        }

        return userRepository.save(user);
    }
}