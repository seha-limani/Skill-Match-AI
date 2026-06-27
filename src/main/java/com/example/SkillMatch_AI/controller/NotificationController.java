package com.example.SkillMatch_AI.controller;

import com.example.SkillMatch_AI.controller.request.NotificationRequest;
import com.example.SkillMatch_AI.model.Notification;
import com.example.SkillMatch_AI.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public Notification create(@Valid @RequestBody NotificationRequest request) {
        return notificationService.create(request);
    }

    @GetMapping("/me")
    public List<Notification> mine() {
        return notificationService.mine();
    }

    @PatchMapping("/{id}/read")
    public Notification markRead(@PathVariable Long id) {
        return notificationService.markRead(id);
    }
}