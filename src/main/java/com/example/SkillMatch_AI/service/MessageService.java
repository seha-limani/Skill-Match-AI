package com.example.SkillMatch_AI.service;

import com.example.SkillMatch_AI.controller.request.MessageRequest;
import com.example.SkillMatch_AI.model.Message;

import java.util.List;

public interface MessageService {
    Message send(MessageRequest request);
    List<Message> conversation(Long otherUserId);
}