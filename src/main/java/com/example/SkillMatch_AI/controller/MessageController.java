package com.example.SkillMatch_AI.controller;

import com.example.SkillMatch_AI.controller.request.MessageRequest;
import com.example.SkillMatch_AI.model.Message;
import com.example.SkillMatch_AI.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    @PreAuthorize("hasAnyRole('JOB_SEEKER', 'EMPLOYER', 'ADMIN')")
    public Message send(@Valid @RequestBody MessageRequest request) {
        return messageService.send(request);
    }

    @GetMapping("/conversation/{otherUserId}")
    @PreAuthorize("hasAnyRole('JOB_SEEKER', 'EMPLOYER', 'ADMIN')")
    public List<Message> conversation(@PathVariable Long otherUserId) {
        return messageService.conversation(otherUserId);
    }
}