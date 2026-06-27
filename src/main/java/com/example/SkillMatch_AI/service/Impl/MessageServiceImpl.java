package com.example.SkillMatch_AI.service.Impl;

import com.example.SkillMatch_AI.controller.request.MessageRequest;
import com.example.SkillMatch_AI.model.Message;
import com.example.SkillMatch_AI.repository.MessageRepository;
import com.example.SkillMatch_AI.repository.UserRepository;
import com.example.SkillMatch_AI.service.CurrentUserService;
import com.example.SkillMatch_AI.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;

    @Override
    @Transactional
    public Message send(MessageRequest request) {
        var sender = currentUserService.get();
        var receiver = userRepository.findById(request.getReceiverId()).orElseThrow(() -> new IllegalArgumentException("Receiver not found"));
        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(request.getContent());
        message.setConversationKey(conversationKey(sender.getId(), receiver.getId()));
        return messageRepository.save(message);
    }

    @Override
    public List<Message> conversation(Long otherUserId) {
        var currentUser = currentUserService.get();
        return messageRepository.findByConversationKeyOrderBySentAtAsc(conversationKey(currentUser.getId(), otherUserId));
    }

    private String conversationKey(Long a, Long b) {
        return a < b ? a + "_" + b : b + "_" + a;
    }
}