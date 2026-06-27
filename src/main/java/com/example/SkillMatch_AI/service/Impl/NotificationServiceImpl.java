package com.example.SkillMatch_AI.service.Impl;

import com.example.SkillMatch_AI.controller.request.NotificationRequest;
import com.example.SkillMatch_AI.model.Notification;
import com.example.SkillMatch_AI.repository.NotificationRepository;
import com.example.SkillMatch_AI.repository.UserRepository;
import com.example.SkillMatch_AI.service.CurrentUserService;
import com.example.SkillMatch_AI.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;

    @Override
    @Transactional
    public Notification create(NotificationRequest request) {
        Notification notification = new Notification();
        notification.setTitle(request.getTitle());
        notification.setMessage(request.getMessage());
        notification.setRecipient(userRepository.findById(request.getRecipientId()).orElseThrow(() -> new IllegalArgumentException("User not found")));
        return notificationRepository.save(notification);
    }

    @Override
    public List<Notification> mine() {
        return notificationRepository.findByRecipientIdOrderByCreatedAtDesc(currentUserService.get().getId());
    }

    @Override
    @Transactional
    public Notification markRead(Long id) {
        Notification notification = notificationRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Notification not found"));
        notification.setIsRead(true);
        return notificationRepository.save(notification);
    }
}