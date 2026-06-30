package com.example.SkillMatch_AI.service.Impl;

import com.example.SkillMatch_AI.controller.request.UserRequest;
import com.example.SkillMatch_AI.model.User;
import com.example.SkillMatch_AI.repository.UserRepository;
import com.example.SkillMatch_AI.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    @org.springframework.context.annotation.Lazy
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User addUser(UserRequest userRequest) {
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new IllegalArgumentException("Email is already registered");
        }

        User user = new User();
        user.setFirstName(userRequest.getFirstName().trim());
        user.setLastName(userRequest.getLastName().trim());
        user.setEmail(userRequest.getEmail().trim().toLowerCase());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setPhone(userRequest.getPhone().trim());
        user.setProfileImage(userRequest.getProfileImage());
        user.setBio(userRequest.getBio());
        user.setRole(userRequest.getRole());

        return userRepository.save(user);
    }

    @Override
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public User updateCurrentUser(UserRequest userRequest) {
        User currentUser = getByEmail(org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName());
        currentUser.setFirstName(userRequest.getFirstName().trim());
        currentUser.setLastName(userRequest.getLastName().trim());
        currentUser.setPhone(userRequest.getPhone().trim());
        currentUser.setProfileImage(userRequest.getProfileImage());
        currentUser.setBio(userRequest.getBio());
        return userRepository.save(currentUser);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
        );
    }
}
