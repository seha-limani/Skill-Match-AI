package com.example.SkillMatch_AI.service;

import com.example.SkillMatch_AI.controller.request.NotificationRequest;
import com.example.SkillMatch_AI.model.Notification;

import java.util.List;

public interface NotificationService {
    Notification create(NotificationRequest request);
    List<Notification> mine();
    Notification markRead(Long id);
}