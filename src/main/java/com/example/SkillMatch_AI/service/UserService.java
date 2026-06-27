package com.example.SkillMatch_AI.service;

import com.example.SkillMatch_AI.controller.request.UserRequest;
import com.example.SkillMatch_AI.model.User;

import java.util.List;

public interface UserService {

    User addUser(UserRequest userRequest);
    User getById(Long id);
    User getByEmail(String email);
    List<User> getAll();
    User updateCurrentUser(UserRequest userRequest);
}
